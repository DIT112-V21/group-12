package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DanceMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_mode);
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void danceMovesPage(View view){
        Intent intent = new Intent(this,DanceMoves.class);
        startActivity(intent);
    }

    public void choreographyPage(View view){
        Intent intent = new Intent(this, Choreographies.class);
        startActivity(intent);

    }
}