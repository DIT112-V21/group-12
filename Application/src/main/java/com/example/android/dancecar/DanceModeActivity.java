package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DanceModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_mode);
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, DrivingActivity.class);
        startActivity(intent);
    }

    public void danceMovesPage(View view){
        Intent intent = new Intent(this, DancingActivity.class);
        startActivity(intent);
    }
}