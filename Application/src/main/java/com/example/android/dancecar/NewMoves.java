package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class NewMoves extends AppCompatActivity{

    private MqttClient mMqttClient;
    EditText move_name, instructions, duration;
    Button save;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_moves);

        mMqttClient.publish("smartcar/", "instructions", 1, null);
        mMqttClient.publish("smartcar/", "duration", 1, null);

        DB = new DBHelper(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String move_nameTXT = move_name.getText().toString();
                String directionTXT = instructions.getText().toString();
                int durationINT = Integer.parseInt(duration.getText().toString());

                boolean checkInsertData = DB.saveMove(move_nameTXT, directionTXT, durationINT);
                if(checkInsertData)
                    Toast.makeText(NewMoves.this, "New move inserted!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(NewMoves.this, "New move not inserted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startDance(View view){
        new startTimer(5);
    }

    public void saveDance(View view){
    }

    public void goToDance(View view){
    }

    public class startTimer {
        Timer timer;

        public startTimer(int seconds) {
            timer = new Timer();
            timer.schedule(new StartMoves(), seconds * 1000);
        }

        class StartMoves extends TimerTask {
            public void run() {
                System.out.println("Time's up!");
                timer.cancel(); //Terminate the timer thread
            }
        }
    }

    public void driveForward(View view){
        int speed = 60;
        mMqttClient.publish("smartcar/forward", Integer.toString(speed), 1, null);
    }

    public void driveLeft(View view){
        int leftAngle = -60;
        mMqttClient.publish("smartcar/left", Integer.toString(leftAngle), 1, null);
    }

    public void driveBackward(View view){
        int backSpeed = -80;
        mMqttClient.publish("smartcar/backward", Integer.toString(backSpeed),1, null );
    }

    public void driveRight(View view){
        int rightAngle = 60;
        mMqttClient.publish("smartcar/right", Integer.toString(rightAngle), 1, null);
    }

    public void driveStop(View view){
        int stop = 0;
        mMqttClient.publish("smartcar/stop", Integer.toString(stop), 1, null);
    }

    public void speedModeOne(View view){
        int speed = 0;
        mMqttClient.publish("smartcar/speedOne", Integer.toString(speed), 1, null);
    }
    public void speedModeTwo(View view){
        int speed = 0;
        mMqttClient.publish("smartcar/speedTwo", Integer.toString(speed), 1, null);
    }
    public void speedModeThree(View view){
        int speed = 0;
        mMqttClient.publish("smartcar/speedThree", Integer.toString(speed), 1, null);
    }
    public void speedModeFour(View view){
        int speed = 0;
        mMqttClient.publish("smartcar/speedFour", Integer.toString(speed), 1, null);
    }

    public void speedPress(View view){
        int message = 0;
        mMqttClient.publish("smartcar/speedPress", Integer.toString(message), 1, null);
    }

    public void brakePress(View view){
        int message = 0;
        mMqttClient.publish("smartcar/brakePress", Integer.toString(message), 1, null);
    }

    public void anglePress(View view){
        int message = 0;
        mMqttClient.publish("smartcar/anglePress", Integer.toString(message), 1, null);
    }
}