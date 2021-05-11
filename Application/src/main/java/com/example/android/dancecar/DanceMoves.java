package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class DanceMoves extends AppCompatActivity {
    ArrayList danceMoves = new ArrayList();
    ArrayList choreography = new ArrayList();
    ArrayList selectedDance = new ArrayList();
    LinearLayout lLayout;
    LinearLayout rLayout;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_moves);
        DaneMoveObject dance1  = new DaneMoveObject("Dance 1",1);
        danceMoves.add(dance1.getDanceName());
        DaneMoveObject dance2  = new DaneMoveObject("Dance 2",2);
        danceMoves.add(dance2.getDanceName());
        DaneMoveObject dance3  = new DaneMoveObject("Dance 3",3);
        danceMoves.add(dance3.getDanceName());
        DaneMoveObject dance4  = new DaneMoveObject("Dance 4",4);
        danceMoves.add(dance4.getDanceName());

        lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
        rLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);

        for (int i = 0; i < danceMoves.size(); i++) {

            checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(danceMoves.get(i).toString());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            lLayout.addView(checkBox);
        }

        for (int i = 0; i < choreography.size(); i++) {

            checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(choreography.get(i).toString());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            rLayout.addView(checkBox);

        }
    }

    View.OnClickListener getOnClickDoSomething(final CheckBox checkBox){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    selectedDance.add(checkBox.getText().toString());
                    TextView current = findViewById(R.id.currentDances);
                    current.setText(selectedDance.toString());
                }else{
                    selectedDance.remove(checkBox.getText().toString());
                    TextView current = findViewById(R.id.currentDances);
                    current.setText(selectedDance.toString());
                }
                //Add what to do when clicking
                Log.d("onClick: ", "CheckBox ID: " + checkBox.getId() + " Text: " + checkBox.getText().toString());
            }
        };
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createChoreography(View view){
        if(!selectedDance.isEmpty()) {
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
            int id = 1;
            checkBox = new CheckBox(this);
            checkBox.setId(id);
            checkBox.setText("New Chor!!!");
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            lLayout.addView(checkBox);
            Toast.makeText(this, "Choreography successfully created ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please select a dance move", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBackToDanceMenu(View view){
        Intent intent = new Intent(this, DanceMode.class);
        startActivity(intent);
    }

    public void makeCarDance(View view){
        //Send message via broker of what dance to preform..
        //mgtt.pup(Smarcar/Dance/dance2)
    }

    public void addNewDance(View view){
        if(!selectedDance.isEmpty()) {
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
            checkBox = new CheckBox(this);
            int id = 1;
            checkBox.setId(id);
            checkBox.setText("New dance!!!");
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            lLayout.addView(checkBox);
            Toast.makeText(this, "Choreography successfully created ", Toast.LENGTH_SHORT).show();
            DaneMoveObject newDance = new DaneMoveObject("newD", 10);
            danceMoves.add(newDance);
        }else{
            Toast.makeText(this, "Please select a dance move", Toast.LENGTH_SHORT).show();
        }
        }

        public void recordNewDance(View view){
            Intent intent = new Intent(this, NewMoves.class);
            startActivity(intent);
        }


    }

