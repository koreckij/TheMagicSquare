package com.example.themagicsquare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class WelcomeActivity extends AppCompatActivity {
    private static final int PARAMETER_ACTIVITY_CODE = 416;
    private static final int GAME_ACTIVITY_CODE = 417;
    private Button playButton;
    private Button aboutButton;
    private Button quitButton;
    private Button parametersButton;
    private int level = 9;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_welcome);
        } else {
            setContentView(R.layout.activity_welcome_horizontal);
        }
        playButton = findViewById(R.id.play_game_button);
        playButton.setOnClickListener(onPlayClickListener);
        aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(onAboutClickListener);
        quitButton = findViewById(R.id.quit_button);
        quitButton.setOnClickListener(onQuitClickListener);
        parametersButton = findViewById(R.id.params_button);
        parametersButton.setOnClickListener(onParamsClickListener);
        if(savedInstanceState!=null){
            playerName = savedInstanceState.getString("PlayerName");
            level = savedInstanceState.getInt("Level");
        }
    }

    private View.OnClickListener onPlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("PlayerName",playerName);
            intent.putExtra("Level",level);
            startActivityForResult(intent, GAME_ACTIVITY_CODE);
        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PlayerName", playerName);
        outState.putInt("Level", level);
    }

    private View.OnClickListener onAboutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Magic_square"));
            startActivity(browserIntent);
        }
    };

    private View.OnClickListener onQuitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAndRemoveTask();
        }
    };

    private View.OnClickListener onParamsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ParametersActivity.class);
            intent.putExtra("PlayerName",playerName);
            intent.putExtra("Level",level);
            startActivityForResult(intent, PARAMETER_ACTIVITY_CODE);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PARAMETER_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                playerName = data.getStringExtra("PlayerName");
                level = data.getIntExtra("Level", 9);
            }
        }
    }
}
