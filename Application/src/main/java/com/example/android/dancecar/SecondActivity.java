package com.example.android.dancecar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.darktheme.R;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton arrowUp;
    private ImageButton arrowDown;
    private ImageButton arrowLeft;
    private ImageButton arrowRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrowUp = findViewById(R.id.arrowUp);
        arrowDown = findViewById(R.id.arrowDown);
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowRight = findViewById(R.id.arrowRight);

        arrowUp.setOnClickListener(this);
        arrowDown.setOnClickListener(this);
        arrowLeft.setOnClickListener(this);
        arrowRight.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //TODO: create connection with MQTT
            case R.id.arrowUp:
                //Toast is just for testing
                Toast.makeText(this, "arrow up", Toast.LENGTH_SHORT).show();
                break;
            case R.id.arrowDown:
                Toast.makeText(this, "arrow down", Toast.LENGTH_SHORT).show();
                break;
            case R.id.arrowLeft:
                Toast.makeText(this, "arrow left", Toast.LENGTH_SHORT).show();
                break;
            case R.id.arrowRight:
                Toast.makeText(this, "arrow right", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}