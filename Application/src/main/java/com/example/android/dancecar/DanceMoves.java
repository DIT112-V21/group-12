package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DanceMoves extends AppCompatActivity {
    ArrayList danceMoves = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_moves);
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void danceMove1(View view){
         final CheckBox danceMove1 = findViewById(R.id.danceMove1);
        if(danceMove1.isChecked()){
            danceMoves.add("danceMove1");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove1");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());

        }
    }

    public void danceMove2(View view){
        final CheckBox danceMove2 = findViewById(R.id.danceMove2);
        if(danceMove2.isChecked()){
            danceMoves.add("danceMove2");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove2");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }

    public void danceMove3(View view){
        final CheckBox danceMove3 = findViewById(R.id.danceMove3);
        if(danceMove3.isChecked()){
            danceMoves.add("danceMove3");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove3");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }


    public void danceMove4(View view){
        final CheckBox danceMove4 = findViewById(R.id.danceMove4);
        if(danceMove4.isChecked()){
            danceMoves.add("danceMove4");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove4");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }

    public void danceMove5(View view){
        final CheckBox danceMove5 = findViewById(R.id.danceMove4);
        if(danceMove5.isChecked()){
            danceMoves.add("danceMove5");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove5");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }

    public void danceMove6(View view){
        final CheckBox danceMove6 = findViewById(R.id.danceMove4);
        if(danceMove6.isChecked()){
            danceMoves.add("danceMove6");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove6");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }

    public void danceMove7(View view){
        final CheckBox danceMove7 = findViewById(R.id.danceMove4);
        if(danceMove7.isChecked()){
            danceMoves.add("danceMove7");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove7");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }

    public void danceMove8(View view){
        final CheckBox danceMove8 = findViewById(R.id.danceMove4);
        if(danceMove8.isChecked()){
            danceMoves.add("danceMove8");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove8");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }
    public void danceMove9(View view){
        final CheckBox danceMove9 = findViewById(R.id.danceMove4);
        if(danceMove9.isChecked()){
            danceMoves.add("danceMove9");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }else{
            danceMoves.remove("danceMove9");
            TextView current = findViewById(R.id.currentDances);
            current.setText(danceMoves.toString());
        }
    }
    public void createChoreography(View view){
        //Add to Choreography list later

    }

    public void createDanceMove(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}