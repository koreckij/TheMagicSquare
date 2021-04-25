package com.example.themagicsquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ParametersActivity extends AppCompatActivity {
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 9;
    private static final int DEFAULT_LEVEL = 9;

    private Button saveButton;
    private Button increaseLevel;
    private Button decreaseLevel;
    private TextView levelText;
    private EditText playerName;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(onSaveClicked);
        increaseLevel = findViewById(R.id.increase_level);
        increaseLevel.setOnClickListener(onIncreaseLevel);
        decreaseLevel = findViewById(R.id.decrease_level);
        decreaseLevel.setOnClickListener(onDecreaseLevel);
        levelText = findViewById(R.id.level_text);
        playerName = findViewById(R.id.player_name);
        String userName = getIntent().getStringExtra("PlayerName");
        if(userName != null)
            playerName.setText(userName);
        if(savedInstanceState ==null || !savedInstanceState.containsKey("level"))
        {
            level = getIntent().getIntExtra("Level", DEFAULT_LEVEL);
        }
        else
            level = savedInstanceState.getInt("level");
        setLevel(level);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level",level);
    }

    private View.OnClickListener onIncreaseLevel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLevel(++level);
        }
    };

    private View.OnClickListener onDecreaseLevel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLevel(--level);
        }
    };

    private void setLevel(int level){
        if (level == MIN_LEVEL)
            decreaseLevel.setEnabled(false);
        else
            decreaseLevel.setEnabled(true);
        if (level == MAX_LEVEL)
            increaseLevel.setEnabled(false);
        else
            increaseLevel.setEnabled(true);
        levelText.setText(Integer.toString(level));
    }

    private View.OnClickListener onSaveClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int level = Integer.parseInt(levelText.getText().toString());
            String userName = playerName.getText().toString();
            Intent data =new Intent();
            data.putExtra("PlayerName",userName);
            data.putExtra("Level",level);
            setResult(RESULT_OK,data);
            finish();
        }
    };
}