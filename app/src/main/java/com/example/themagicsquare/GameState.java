package com.example.themagicsquare;

import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private boolean submitButtonEnabled;
    private boolean newGameButtonEnabled;
    private boolean continueButtonEnabled;
    private long stopedTime;
    private boolean timerStoped;
    private Integer[][] valuesFilled;
    private MagicSquare magicSquare;
    private int level;

    public GameState(Button submit, Button newGame, Button resume, boolean isTimerStopped, long timeWhenStopped, EditText[][] userInput, MagicSquare _magicSquare, int currentLevel) {
        submitButtonEnabled = submit.isEnabled();
        newGameButtonEnabled = newGame.isEnabled();
        continueButtonEnabled = resume.isEnabled();
        timerStoped = isTimerStopped;
        stopedTime = timeWhenStopped;
        valuesFilled = new Integer[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!userInput[i][j].getText().toString().isEmpty()) {
                    valuesFilled[i][j] = Integer.parseInt(userInput[i][j].getText().toString());
                }
            }
        }
        try {
            magicSquare = (MagicSquare) _magicSquare.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        level = currentLevel;
    }

    public Boolean IsSubmitEnabled() {
        return submitButtonEnabled;
    }

    public Boolean IsResumeEnabled() {
        return continueButtonEnabled;
    }

    public Boolean IsNewGameEnabled() {
        return newGameButtonEnabled;
    }

    public Integer getBoardState(int i, int j) {
        return valuesFilled[i][j];
    }

    public int[] getRowSums() {
        return magicSquare.getRowSums();
    }

    public int[] getColumnSums() {
        return magicSquare.getColumnSums();
    }

    public MagicSquare getMagicSquare() {
        return magicSquare;
    }

    public long getStopedTime() {
        return stopedTime;
    }

    public boolean IsTimerStoped() {
        return timerStoped;
    }

    public int getLevel() {
        return level;
    }
}
