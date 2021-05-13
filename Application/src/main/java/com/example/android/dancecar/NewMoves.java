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


public class NewMoves extends AppCompatActivity {
    private static final String TAG = "SmartcarMqttController";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883"; //Coonnect local
    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private String direction;
    private String currentSpeed;

    private CountDownTimer countDownTimer;
    private long timeLeft = 10000;
    private boolean timerIsRunning = false;
    private String timerText = "";

    private Mode speedMode = new Mode("speed");
    private Mode angleMode = new Mode("angle");
    private Mode brakeMode = new Mode("brake");

    private ImageButton forward;
    private ImageButton backward;
    private ImageButton left;
    private ImageButton right;

    private Button speed;
    private Button angle;
    private Button brake;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button currentSpeedMode;
    private Button currentAngleMode;
    private Button currentBrakeMode;
    private Button speedometer;
    private TextView recordingTimer;

    private ToggleButton startStop;

    EditText move_name, instructions, duration;
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


    // Timer code partially derived from https://www.youtube.com/watch?v=zmjfAcnosS0
    public void startStopTimer() {
        if (timerIsRunning) {
            stopTimer();
        } else {
            countDown();
        }
    }

    public void stopTimer () {
        countDownTimer.cancel();
        timerIsRunning = false;
        timeLeft = 10000;
        timerText = "";
        recordingTimer.setText(timerText);
    }

    public void countDown() {
        timerIsRunning = true;
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
        speed = findViewById(R.id.speed);
        angle = findViewById(R.id.angle);
        brake = findViewById(R.id.brake);
        one = findViewById(R.id.button1);
        two = findViewById(R.id.button2);
        three = findViewById(R.id.button3);
        four = findViewById(R.id.button4);
        currentSpeedMode = findViewById(R.id.currentSpeedMode);
        currentAngleMode = findViewById(R.id.currentAngleMode);
        currentBrakeMode = findViewById(R.id.currentBrakeMode);
        speedometer = findViewById(R.id.currentSpeed);
        recordingTimer = findViewById(R.id.recordingTimer);
        startStop = findViewById(R.id.startstopButton);
    }

    public void driveForward(View view){
        int message = 0;
        direction = "forward";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/forward", Integer.toString(message), 1, null);
    }

    public void driveBackward(View view){
        int message = 0;
        direction = "backward";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/backward", Integer.toString(message),1, null );
    }

    public void driveLeft(View view){
        int message = 0;
        direction = "left";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/left", Integer.toString(message), 1, null);
    }

    public void driveRight(View view){
        int message = 0;
        direction = "right";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/right", Integer.toString(message), 1, null);
    }

    public void driveStop(View view){
        int message = 0;
        mMqttClient.publish("smartcar/stop", Integer.toString(message), 1, null);
    }

    /*
    Only one arrow button can show as pressed/color at a time.
    The other buttons will get unpressed when one button is pressed.
    */
    public void colorArrowButtons(String direction){
        uncolorButtons("arrow");
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

    public void numberModeOne(View view){
        setCurrentModeNumber(1);
        colorNumberButtons("one");
        updateAndShowModeNumbers();
        int message = 0;
        mMqttClient.publish("smartcar/speedOne", Integer.toString(message), 1, null);
    }
    public void numberModeTwo(View view){
        setCurrentModeNumber(2);
        colorNumberButtons("two");
        updateAndShowModeNumbers();
        int message = 0;
        mMqttClient.publish("smartcar/speedTwo", Integer.toString(message), 1, null);
    }
    public void numberModeThree(View view){
        setCurrentModeNumber(3);
        colorNumberButtons("three");
        updateAndShowModeNumbers();
        int message = 0;
        mMqttClient.publish("smartcar/speedThree", Integer.toString(message), 1, null);
    }
    public void numberModeFour(View view){
        setCurrentModeNumber(4);
        colorNumberButtons("four");
        updateAndShowModeNumbers();
        int message = 0;
        mMqttClient.publish("smartcar/speedFour", Integer.toString(message), 1, null);
    }

    public void setCurrentModeNumber(int number) {
        if (speedMode.isActivated()) {
            speedMode.setNumber(number);
        } else if (angleMode.isActivated()) {
            angleMode.setNumber(number);
        } else if (brakeMode.isActivated()) {
            brakeMode.setNumber(number);
        } else {
            uncolorButtons("number");
        }
    }

    /*
    Only one number button can show as pressed/colored at a time.
    The other buttons will get unpressed when one button is pressed.
     */
    public void colorNumberButtons(String number){
        uncolorButtons("number");
        if(number.equals("one")) {
            colorButton(one);
        } else if (number.equals("two")) {
            colorButton(two);
        } else if (number.equals("three")) {
            colorButton(three);
        } else if (number.equals("four")) {
            colorButton(four);
        }
    }

    /*
    A number is shown underneath the three different mode buttons
    to show in which number that mode is currently in.
    */
    public void updateAndShowModeNumbers(){
        if (speedMode.isActivated()){
            String number = Integer.toString(speedMode.getNumber());
            currentSpeedMode.setText(number);
        } else if (angleMode.isActivated()){
            String number = Integer.toString(angleMode.getNumber());
            currentAngleMode.setText(number);
        } else if (brakeMode.isActivated()){
            String number = Integer.toString(brakeMode.getNumber());
            currentBrakeMode.setText(number);
        } else {
            currentSpeedMode.setText("");
            currentAngleMode.setText("");
            currentBrakeMode.setText("");
        }
    }

    public void speedPress(View view){
        speedMode.setActivated(true);
        angleMode.setActivated(false);
        brakeMode.setActivated(false);
        int message = 0;
        numberAlreadyActivated();
        colorModeButtons();
        mMqttClient.publish("smartcar/speedPress", Integer.toString(message), 1, null);
    }

    public void anglePress(View view){
        angleMode.setActivated(true);
        brakeMode.setActivated(false);
        speedMode.setActivated(false);
        int message = 0;
        numberAlreadyActivated();
        colorModeButtons();
        mMqttClient.publish("smartcar/anglePress", Integer.toString(message), 1, null);
    }

    public void brakePress(View view){
        brakeMode.setActivated(true);
        speedMode.setActivated(false);
        angleMode.setActivated(false);
        int message = 0;
        numberAlreadyActivated();
        colorModeButtons();
        mMqttClient.publish("smartcar/brakePress", Integer.toString(message), 1, null);
    }

    /*
    When a mode has already been activated earlier,
    when choosing that mode again, the current number
    that mode is in shows as pressed/colored.
    */
    public void numberAlreadyActivated() {
        int number = getCurrentModeNumber();
        uncolorButtons("number");
        if (number == 1) {
            colorButton(one);
        } else if (number == 2) {
            colorButton(two);
        } else if (number == 3) {
            colorButton(three);
        } else if (number == 4) {
            colorButton(four);
        }
    }

    public int getCurrentModeNumber() {
        if (speedMode.isActivated()) {
            return speedMode.getNumber();
        } else if (angleMode.isActivated()) {
            return angleMode.getNumber();
        } else if (brakeMode.isActivated()) {
            return brakeMode.getNumber();
        } else {
            return 0;
        }
    }

    public void colorModeButtons(){
        uncolorButtons("mode");
        if (speedMode.isActivated()) {
            colorButton(speed);
        } else if (angleMode.isActivated()) {
            colorButton(angle);
        } else if (brakeMode.isActivated()) {
            colorButton(brake);
        }
    }

    public void showSpeed(String message){
        speedometer.setText(message);
    }

    public void resetSettings(){
        speedMode.setActivated(false);
        angleMode.setActivated(false);
        brakeMode.setActivated(false);
        speedMode.setNumber(0);
        angleMode.setNumber(0);
        brakeMode.setNumber(0);
        updateAndShowModeNumbers();
        numberAlreadyActivated();
        uncolorButtons("all");
    }

    public void colorImageButton(ImageButton button){
        button.setColorFilter(Color.parseColor("#8BC34A"));
    }

    public void colorButton(Button button){
        button.setBackgroundColor(Color.parseColor("#ED2E3C34"));
    }

    public void uncolorButtons(String type) {
        if (type.equals("mode")) {
            speed.setBackgroundColor(Color.parseColor("#8BC34A"));
            angle.setBackgroundColor(Color.parseColor("#8BC34A"));
            brake.setBackgroundColor(Color.parseColor("#8BC34A"));
        } else if (type.equals("number")) {
            one.setBackgroundColor(Color.parseColor("#8BC34A"));
            two.setBackgroundColor(Color.parseColor("#8BC34A"));
            three.setBackgroundColor(Color.parseColor("#8BC34A"));
            four.setBackgroundColor(Color.parseColor("#8BC34A"));
        } else if (type.equals("arrow")) {
            forward.setColorFilter(Color.TRANSPARENT);
            backward.setColorFilter(Color.TRANSPARENT);
            left.setColorFilter(Color.TRANSPARENT);
            right.setColorFilter(Color.TRANSPARENT);
        } else {
            speed.setBackgroundColor(Color.parseColor("#8BC34A"));
            angle.setBackgroundColor(Color.parseColor("#8BC34A"));
            brake.setBackgroundColor(Color.parseColor("#8BC34A"));
            one.setBackgroundColor(Color.parseColor("#8BC34A"));
            two.setBackgroundColor(Color.parseColor("#8BC34A"));
            three.setBackgroundColor(Color.parseColor("#8BC34A"));
            four.setBackgroundColor(Color.parseColor("#8BC34A"));
            forward.setColorFilter(Color.TRANSPARENT);
            backward.setColorFilter(Color.TRANSPARENT);
            left.setColorFilter(Color.TRANSPARENT);
            right.setColorFilter(Color.TRANSPARENT);
        }
    }
}