package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class Choreographies extends AppCompatActivity {

    ArrayList choreography = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choreographies);
    }

    public void goToDrive(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void perviousScreen(View view){
        Intent intent = new Intent(this, DanceMoves.class);
        startActivity(intent);
    }
    public void choreography1(View view){
        final CheckBox choreography1 = findViewById(R.id.choreography1);
        if(choreography1.isChecked()){
            choreography.add("Choreography 1");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 1");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }

    public void choreography2(View view){
        final CheckBox choreography2 = findViewById(R.id.choreography2);
        if(choreography2.isChecked()){
            choreography.add("Choreography 2");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 2");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }
    public void choreography3(View view){
        final CheckBox choreography3 = findViewById(R.id.choreography3);
        if(choreography3.isChecked()){
            choreography.add("Choreography 3");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 3");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }

    public void choreography4(View view){
        final CheckBox choreography4 = findViewById(R.id.choreography4);
        if(choreography4.isChecked()){
            choreography.add("Choreography 4");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 4");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }

    public void choreography5(View view){
        final CheckBox choreography5 = findViewById(R.id.choreography5);
        if(choreography5.isChecked()){
            choreography.add("Choreography 5");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 5");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }

    public void choreography6(View view){
        final CheckBox choreography6 = findViewById(R.id.choreography6);
        if(choreography6.isChecked()){
            choreography.add("Choreography 6");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 6");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }

    public void choreography7(View view){
        final CheckBox choreography7 = findViewById(R.id.choreography7);
        if(choreography7.isChecked()){
            choreography.add("Choreography 7");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());
        }else{
            choreography.remove("Choreography 7");
            TextView current = findViewById(R.id.currentChoreographies);
            current.setText(choreography.toString());

        }
    }

    public void createDance(View view){
        //Add to Choreography list later

    }

    }