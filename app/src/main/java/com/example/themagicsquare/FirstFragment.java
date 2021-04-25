package com.example.themagicsquare;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class FirstFragment extends Fragment {
    private int START_LEVEL = 9;
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 9;

    private EditText[][] userInput;
    private MagicSquare magicSquare;
    private Button submitButton;
    private Button exitButton;
    private Button resumeButton;
    private Button newGameButton;
    private Button decreaseLevel;
    private Button increaseLevel;
    private Button help;
    private TextView levelText;
    private Chronometer chronometer;
    private long timeWhenStopped;
    private GameState savedState;
    private boolean isTimerStopped;
    private int currentLevel = START_LEVEL;
    private String userName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
//        userName = getArguments().getString("PlayerName");
//        START_LEVEL = getArguments().getInt("Level");
        // Inflate the layout for this fragment
        if (getActivity().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            return inflater.inflate(R.layout.fragment_first, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_second, container, false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        savedState = new GameState(submitButton, newGameButton, resumeButton, isTimerStopped, isTimerStopped ? timeWhenStopped : chronometer.getBase() - SystemClock.elapsedRealtime(), userInput, magicSquare, currentLevel);
        super.onConfigurationChanged(newConfig);
        if (getActivity().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            getActivity().setContentView(R.layout.fragment_first);
        } else {
            getActivity().setContentView(R.layout.fragment_second);
        }
        Create();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Create();
    }

    private void Create() {
        FragmentActivity view = getActivity();
        magicSquare = savedState == null ? new MagicSquare() : savedState.getMagicSquare();
        userInput = new EditText[3][3];
        int row = 0;
        for (int i = 0; i < 9; i++) {
            if (i != 0 && i % 3 == 0)
                row++;
            String userInputId = "user_input_" + i;
            int resID = getResources().getIdentifier(userInputId, "id", getActivity().getPackageName());
            userInput[row][i % 3] = ((EditText) view.findViewById(resID));
            if (savedState != null && savedState.getBoardState(row, i % 3) != null)
                userInput[row][i % 3].setText(savedState.getBoardState(row, i % 3).toString());
        }

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(onSubmitClickListener);
        exitButton = view.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(onExitClick);
        resumeButton = view.findViewById(R.id.continue_button);
        resumeButton.setOnClickListener(onResumeClick);
        newGameButton = view.findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(onNewGameClick);
        increaseLevel = view.findViewById(R.id.increase_level);
        increaseLevel.setOnClickListener(onIncreaseLevel);
        decreaseLevel = view.findViewById(R.id.decrease_level);
        decreaseLevel.setOnClickListener(onDecreaseLevel);
        help = view.findViewById(R.id.help_button);
        help.setOnClickListener(onHelpClick);
        levelText = view.findViewById(R.id.level_text);

        if (savedState != null) {
            submitButton.setEnabled(savedState.IsSubmitEnabled());
            newGameButton.setEnabled(savedState.IsNewGameEnabled());
            resumeButton.setEnabled(savedState.IsResumeEnabled());
            timeWhenStopped = savedState.getStopedTime();
            isTimerStopped = savedState.IsTimerStoped();
            currentLevel = savedState.getLevel();
        }
        InitState();
    }

    private View.OnClickListener onSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int[][] inputValues = new int[3][3];
            List<Integer> values = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (userInput[i][j].getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter solution first", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        inputValues[i][j] = Integer.parseInt(userInput[i][j].getText().toString());
                        if (values.contains(inputValues[i][j])) {
                            Toast.makeText(getActivity().getApplicationContext(), "No duplication allowed", Toast.LENGTH_LONG).show();
                            return;
                        }
                        values.add(inputValues[i][j]);
                    }
                }
            }
            newGameButton.setEnabled(true);
            v.setEnabled(false);
            chronometer.stop();
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            isTimerStopped = true;
            if (magicSquare.checkSulution(inputValues)) {
                int score = CalculateScore();
                Toast.makeText(getActivity().getApplicationContext(), "Congratulations, you win :o . Score: " + score, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Nope, try again.", Toast.LENGTH_LONG).show();
                resumeButton.setEnabled(true);
            }
        }
    };

    private int CalculateScore() {
        int helpNeeded = (START_LEVEL - currentLevel + 1) * (MAX_LEVEL - START_LEVEL + 1);
        int maxTimeMilis = 300000;//5min
        int scale = 3000;// max score 100 scale
        return (int) (maxTimeMilis + timeWhenStopped) / (helpNeeded * scale);
    }

    private View.OnClickListener onExitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finishAndRemoveTask();
        }
    };

    private View.OnClickListener onResumeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setEnabled(false);
            newGameButton.setEnabled(false);
            submitButton.setEnabled(true);
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
            isTimerStopped = false;
            ResetButtons();
        }
    };

    private View.OnClickListener onNewGameClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    userInput[i][j].getText().clear();
            }
            magicSquare = new MagicSquare();
            isTimerStopped = false;
            timeWhenStopped = 0;
            currentLevel = START_LEVEL;
            ResetButtons();
            InitState();
        }
    };

    private void ResetButtons() {
        newGameButton.setEnabled(false);
        resumeButton.setEnabled(false);
        submitButton.setEnabled(true);
    }

    private void InitState() {
        int[] rowSums = magicSquare.getRowSums();
        int[] columnSums = magicSquare.getColumnSums();
        for (int i = 0; i < 3; i++) {
            String resultRowId = "result_row_" + i;
            String resultColumnId = "result_column_" + i;
            int resRowID = getResources().getIdentifier(resultRowId, "id", getActivity().getPackageName());
            int resColID = getResources().getIdentifier(resultColumnId, "id", getActivity().getPackageName());
            ((TextView) getActivity().findViewById(resRowID)).setText(Integer.toString(rowSums[i]));
            ((TextView) getActivity().findViewById(resColID)).setText(Integer.toString(columnSums[i]));
        }
        for (int i = 9; i > currentLevel; i--)
            Help(i);
        chronometer = (Chronometer) getActivity().findViewById(R.id.simpleChronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        if (!isTimerStopped) {
            chronometer.start();
        }
        help.setEnabled(currentLevel > MIN_LEVEL);
        decreaseLevel.setEnabled(START_LEVEL > MIN_LEVEL);
        increaseLevel.setEnabled(START_LEVEL < MAX_LEVEL);
        levelText.setText(Integer.toString(START_LEVEL));
    }

    private View.OnClickListener onHelpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Help(currentLevel);
            currentLevel--;
            if (currentLevel == MIN_LEVEL) {
                help.setEnabled(false);
            }
        }
    };

    private void Help(int level) {
        int hintNumber = MAX_LEVEL - level;
        userInput[hintNumber / 3][hintNumber % 3].setText(Integer.toString(magicSquare.getHelp(hintNumber / 3, hintNumber % 3)));
    }

    private View.OnClickListener onIncreaseLevel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int level = Integer.parseInt(levelText.getText().toString());
            currentLevel = ++level;
            START_LEVEL = currentLevel;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    userInput[i][j].getText().clear();
            }
            magicSquare = new MagicSquare();
            if (currentLevel == MAX_LEVEL) {
                increaseLevel.setEnabled(false);
            }
            isTimerStopped = false;
            timeWhenStopped = 0;
            currentLevel = START_LEVEL;
            ResetButtons();
            InitState();
        }
    };

    private View.OnClickListener onDecreaseLevel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int level = Integer.parseInt(levelText.getText().toString());
            currentLevel = --level;
            START_LEVEL = currentLevel;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    userInput[i][j].getText().clear();
            }
            magicSquare = new MagicSquare();
            isTimerStopped = false;
            timeWhenStopped = 0;
            currentLevel = START_LEVEL;
            ResetButtons();
            InitState();
        }
    };
}