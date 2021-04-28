package com.example.android.dancecar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.darktheme.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton forwardButton = findViewById(R.id.arrowUp);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void driveForward(View view) {

    }

    public void driveBackward(View view) {

    }

    public void stop(View view) {

    }

    public void left(View view) {

    }

    public void right(View view) {
    }


    public void angleMode(View view) {
    }

    public void brake(View view) {
    }

    public void speedMode(View view) {
    }

    public void mode1(View view) {
    }

    public void mode2(View view) {
    }

    public void mode3(View view) {
    }

    public void mode4(View view) {
    }
}

