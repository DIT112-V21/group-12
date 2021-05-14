package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.ArrayList;
import jServe.Core.StopWatch;


public class NewMoves extends AppCompatActivity {
    private static final String TAG = "SmartcarMqttController";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883"; //Coonnect local
    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private String direction = "";
    private String lastDirection = "";
    private String currentSpeed;

    private CountDownTimer countDownTimer;
    private long timeLeft = 30000;
    private boolean isRecording = false;
    private String timerText = "";
    private long duration;

    private ImageButton forward;
    private ImageButton backward;
    private ImageButton left;
    private ImageButton right;

    private Button speedometer;

    private TextView recordingTimer;
    private TextView test;

    private ToggleButton startStop;

    private ArrayList<IndividualMove> danceSequence = new ArrayList<>();

    StopWatch stopWatch = new StopWatch();

    //EditText move_name, instructions, duration;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_moves);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        connectToMqttBroker();
        initialiseButtons();

        /*
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

         */

        //Source for the code below: https://developer.android.com/guide/topics/ui/controls/togglebutton
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // Start recording
                    startStopTimer();

                } else {
                    // Stop recording
                    startStopTimer();

                }
            }
        });
    }

    public void saveDance(View view){
    }


    @Override
    protected void onResume() {
        super.onResume();
        connectToMqttBroker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMqttClient.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, "Disconnected from broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    private void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    mMqttClient.subscribe("smartcar/odometerSpeed", 1, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(TAG, failedConnection);
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(TAG, connectionLost); //debug in logcat
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    //Add code for data to app.
                    Log.e(TAG, message.toString());
                    if (topic.equals("smartcar/odometerSpeed")){
                        currentSpeed = message.toString();
                        showSpeed(currentSpeed);
                        if (currentSpeed.equals("0.000000")) {
                            resetSettings();
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    public void initialiseButtons() {
        forward = findViewById(R.id.arrowUp);
        backward = findViewById(R.id.arrowDown);
        left = findViewById(R.id.arrowLeft);
        right = findViewById(R.id.arrowRight);
        speedometer = findViewById(R.id.currentSpeed);
        recordingTimer = findViewById(R.id.recordingTimer);
        startStop = findViewById(R.id.startstopButton);
        test = findViewById(R.id.test);
    }

    // Timer code partially derived from https://www.youtube.com/watch?v=zmjfAcnosS0
    public void startStopTimer() {
        if (isRecording) {
            stopTimer();
        } else {
            countDown();
        }
    }

    public void stopTimer () {
        countDownTimer.cancel();
        isRecording = false;
        timeLeft = 10000;
        timerText = "";
        recordingTimer.setText(timerText);
        uncolorButtons();
        direction = "";
        lastDirection = "";
        stopWatch.stop();
    }

    public void countDown() {
        isRecording = true;
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                startStop.setChecked(true);
                stopTimer();
            }
        };
        countDownTimer.start();
    }

    public void updateTimer() {
        int seconds = (int) timeLeft % 10000 / 1000;
        timerText = seconds + " sec";
        recordingTimer.setText(timerText);
    }

    public void saveIndividualMove() {
        //TODO: remove text view for testing
        stopWatch.stop();
        duration = stopWatch.elapsed();
        IndividualMove individualMove = new IndividualMove(lastDirection, duration);
        danceSequence.add(individualMove);
        String dur = Long.toString(duration);
        test.setText(dur);
        stopWatch.start();
        lastDirection = direction;
    }

    public void driveForward(View view){
        if (isRecording) {
            int message = 0;
            direction = "forward";
            //String dir = "forward";
            colorArrowButtons(direction);
            mMqttClient.publish("smartcar/forward", Integer.toString(message), 1, null);
            // First time the user presses an arrow button

            if (!lastDirection.equals(direction) && lastDirection.equals("")) {
                stopWatch.start();
                lastDirection = direction;
                // When the user presses the next arrow button, the individual move will be saved
            } else if (stopWatch.isRunning() && !lastDirection.equals(direction) && !lastDirection.equals("")) {
                saveIndividualMove();
            }
        }
    }

    public void driveBackward(View view){
        if (isRecording) {
            int message = 0;
            direction = "backward";
            colorArrowButtons(direction);
            mMqttClient.publish("smartcar/backward", Integer.toString(message), 1, null);
            // First time the user presses an arrow button
            if (!lastDirection.equals(direction) && lastDirection.equals("")) {
                stopWatch.start();
                lastDirection = direction;
                // When the user presses the next arrow button, the individual move will be saved
            } else if (stopWatch.isRunning() && !lastDirection.equals(direction) && !lastDirection.equals("")) {
                saveIndividualMove();
            }
        }
    }

    public void driveLeft(View view){
        if (isRecording) {
            int message = 0;
            direction = "left";
            colorArrowButtons(direction);
            mMqttClient.publish("smartcar/left", Integer.toString(message), 1, null);
            // First time the user presses an arrow button
            if (!lastDirection.equals(direction) && lastDirection.equals("")) {
                stopWatch.start();
                lastDirection = direction;
                // When the user presses the next arrow button, the individual move will be saved
            } else if (stopWatch.isRunning() && !lastDirection.equals(direction) && !lastDirection.equals("")) {
                saveIndividualMove();
            }
        }
    }

    public void driveRight(View view){
        if (isRecording) {
            int message = 0;
            direction = "right";
            colorArrowButtons(direction);
            mMqttClient.publish("smartcar/right", Integer.toString(message), 1, null);
            // First time the user presses an arrow button
            if (!lastDirection.equals(direction) && lastDirection.equals("")) {
                stopWatch.start();
                lastDirection = direction;
                // When the user presses the next arrow button, the individual move will be saved
            } else if (stopWatch.isRunning() && !lastDirection.equals(direction) && !lastDirection.equals("")) {
                saveIndividualMove();
            }
        }
    }

    public void driveStop(View view){
        if (isRecording) {
            int message = 0;
            direction = "stop";
            mMqttClient.publish("smartcar/stop", Integer.toString(message), 1, null);
            if (!lastDirection.equals(direction) && lastDirection.equals("")) {
                stopWatch.start();
                lastDirection = direction;
                // When the user presses the next arrow button, the individual move will be saved
            } else if (stopWatch.isRunning() && !lastDirection.equals(direction) && !lastDirection.equals("")) {
                saveIndividualMove();
            }
        }
    }


    /*
    Only one arrow button can show as pressed/color at a time.
    The other buttons will get unpressed when one button is pressed.
    */
    public void colorArrowButtons(String direction){
        uncolorButtons();
        if (direction.equals("forward")) {
            colorImageButton(forward);
        } else if (direction.equals("backward")) {
            colorImageButton(backward);
        } else if (direction.equals("left")) {
            colorImageButton(left);
        } else if (direction.equals("right")) {
            colorImageButton(right);
        }
    }

    public void showSpeed(String message){
        speedometer.setText(message);
    }

    public void resetSettings(){
        uncolorButtons();
    }

    public void colorImageButton(ImageButton button){
        button.setColorFilter(Color.parseColor("#8BC34A"));
    }

    public void colorButton(Button button){
        button.setBackgroundColor(Color.parseColor("#ED2E3C34"));
    }

    public void uncolorButtons() {
        forward.setColorFilter(Color.TRANSPARENT);
        backward.setColorFilter(Color.TRANSPARENT);
        left.setColorFilter(Color.TRANSPARENT);
        right.setColorFilter(Color.TRANSPARENT);
    }
}