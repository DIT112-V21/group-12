package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SmartcarMqttController";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883"; //Coonnect local
    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private String direction;
    Mode speedMode = new Mode("speed");
    Mode angleMode = new Mode("angle");
    Mode brakeMode = new Mode("brake");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        connectToMqttBroker();
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
                        showSpeed(message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    public void driveForward(View view){
        int speed = 60;
        direction = "forward";
        updateArrowButtons(direction);
        mMqttClient.publish("smartcar/forward", Integer.toString(speed), 1, null);
    }

    public void driveBackward(View view){
        int backSpeed = -80;
        direction = "backward";
        updateArrowButtons(direction);
        mMqttClient.publish("smartcar/backward", Integer.toString(backSpeed),1, null );
    }

    public void driveLeft(View view){
        int leftAngle = -60;
        direction = "left";
        updateArrowButtons(direction);
        mMqttClient.publish("smartcar/left", Integer.toString(leftAngle), 1, null);
    }

    public void driveRight(View view){
        int rightAngle = 60;
        direction = "right";
        updateArrowButtons(direction);
        mMqttClient.publish("smartcar/right", Integer.toString(rightAngle), 1, null);
    }

    public void driveStop(View view){
        int stop = 0;
        mMqttClient.publish("smartcar/stop", Integer.toString(stop), 1, null);
    }

    public void updateArrowButtons(String direction){
        ImageButton forward = findViewById(R.id.arrowUp);
        ImageButton backward = findViewById(R.id.arrowDown);
        ImageButton left = findViewById(R.id.arrowLeft);
        ImageButton right = findViewById(R.id.arrowRight);
        switch (direction) {
            case "forward":
                pressArrowButton(forward);
                unPressArrowButton(backward, left, right);
                break;
            case "backward":
                pressArrowButton(backward);
                unPressArrowButton(forward, left, right);
                break;
            case "left":
                pressArrowButton(left);
                unPressArrowButton(forward, backward, right);
                break;
            case "right":
                pressArrowButton(right);
                unPressArrowButton(forward, backward, left);
                break;
        }
    }

    public void pressArrowButton(ImageButton button){
        button.setColorFilter(Color.parseColor("#ED7D9F88"));
    }

    public void unPressArrowButton(ImageButton button1, ImageButton button2, ImageButton button3){
        button1.setColorFilter(Color.TRANSPARENT);
        button2.setColorFilter(Color.TRANSPARENT);
        button3.setColorFilter(Color.TRANSPARENT);
    }

    public void speedModeOne(View view){
        updateModeNumber(1);
        updateNumberButtons("one");
        showMode();
        int speed = 0;
        mMqttClient.publish("smartcar/speedOne", Integer.toString(speed), 1, null);
    }
    public void speedModeTwo(View view){
        updateModeNumber(2);
        updateNumberButtons("two");
        showMode();
        int speed = 0;
        mMqttClient.publish("smartcar/speedTwo", Integer.toString(speed), 1, null);
    }
    public void speedModeThree(View view){
        updateModeNumber(3);
        updateNumberButtons("three");
        showMode();
        int speed = 0;
        mMqttClient.publish("smartcar/speedThree", Integer.toString(speed), 1, null);
    }
    public void speedModeFour(View view){
        updateModeNumber(4);
        updateNumberButtons("four");
        showMode();
        int speed = 0;
        mMqttClient.publish("smartcar/speedFour", Integer.toString(speed), 1, null);
    }

    public void updateNumberButtons(String number){
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        Button four = findViewById(R.id.button4);
        switch (number) {
            case "one":
                pressModeButton(one);
                unPressNumberButton(two, three, four);
                break;
            case "two":
                pressModeButton(two);
                unPressNumberButton(one, three, four);
                break;
            case "three":
                pressModeButton(three);
                unPressNumberButton(one, two, four);
                break;
            case "four":
                pressModeButton(four);
                unPressNumberButton(one, two, three);
                break;
        }
    }

    public void updateModeNumber(int number) {
        if (speedMode.isActivated()) {
            speedMode.setNumber(number);
        } else if (angleMode.isActivated()) {
            angleMode.setNumber(number);
        } else if (brakeMode.isActivated()) {
            brakeMode.setNumber(number);
        } else {
            //TODO: if no mode is activated, what should happen?
        }
    }

    /* 
    When a mode has already been activated earlier, 
    when choosing that mode again, the current number 
    that mode is in shows as pressed/colored.
     */
    public void checkNumberButtons() {
        if (speedMode.isActivated()) {
            int number = speedMode.getNumber();
            numberActivated(number);
        } else if (angleMode.isActivated()) {
            int number = angleMode.getNumber();
            numberActivated(number);
        } else if (brakeMode.isActivated()) {
            int number = brakeMode.getNumber();
            numberActivated(number);
        }
    }

    public void numberActivated(int number) {
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        Button four = findViewById(R.id.button4);

        if (number == 1) {
            pressModeButton(one);
            unPressNumberButton(two, three, four);
        } else if (number == 2) {
            pressModeButton(two);
            unPressNumberButton(one, three, four);
        } else if (number == 3) {
            pressModeButton(three);
            unPressNumberButton(two, one, four);
        } else if (number == 4) {
            pressModeButton(four);
            unPressNumberButton(two, three, one);
        } else if (number == 0) {
            one.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            two.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            three.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            four.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        }
    }

    public void speedPress(View view){
        speedMode.setActivated(true);
        angleMode.setActivated(false);
        brakeMode.setActivated(false);
        checkNumberButtons();
        int message = 0;
        updateModeButtons();
        mMqttClient.publish("smartcar/speedPress", Integer.toString(message), 1, null);
    }

    public void anglePress(View view){
        angleMode.setActivated(true);
        brakeMode.setActivated(false);
        speedMode.setActivated(false);
        checkNumberButtons();
        int message = 0;
        updateModeButtons();
        mMqttClient.publish("smartcar/anglePress", Integer.toString(message), 1, null);
    }

    public void brakePress(View view){
        brakeMode.setActivated(true);
        speedMode.setActivated(false);
        angleMode.setActivated(false);
        checkNumberButtons();
        int message = 0;
        updateModeButtons();
        mMqttClient.publish("smartcar/brakePress", Integer.toString(message), 1, null);
    }

    public void updateModeButtons(){
        Button speed = findViewById(R.id.speed);
        Button angle = findViewById(R.id.angle);
        Button brake = findViewById(R.id.brake);

        if (speedMode.isActivated()) {
            pressModeButton(speed);
            unPressModeButton(angle, brake);
        } else if (angleMode.isActivated()) {
            pressModeButton(angle);
            unPressModeButton(speed, brake);
        } else if (brakeMode.isActivated()) {
            pressModeButton(brake);
            unPressModeButton(speed, angle);
        } else {
            speed.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            angle.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            brake.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        }
    }

    public void pressModeButton(Button button){
        button.setBackgroundColor(Color.parseColor("#ED2E3C34"));
    }

    public void unPressModeButton(Button button1, Button button2){
        button1.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button2.setBackgroundColor(Color.parseColor("#ED7D9F88"));
    }

    public void unPressNumberButton(Button button1, Button button2, Button button3){
        button1.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button2.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button3.setBackgroundColor(Color.parseColor("#ED7D9F88"));
    }

    public void showMode(){
        Button currentSpeedMode = findViewById(R.id.currentSpeedMode);
        Button currentAngleMode = findViewById(R.id.currentAngleMode);
        Button currentBrakeMode = findViewById(R.id.currentBrakeMode);
        if (speedMode.isActivated()){
            String number = Integer.toString(speedMode.getNumber());
            currentSpeedMode.setText(number);
        } else if (angleMode.isActivated()){
            String number = Integer.toString(angleMode.getNumber());
            currentAngleMode.setText(number);
        } else if (brakeMode.isActivated()){
            String number = Integer.toString(brakeMode.getNumber());
            currentBrakeMode.setText(number);
        } else { //TODO: if the car is standing still
            currentSpeedMode.setText("");
            currentAngleMode.setText("");
            currentBrakeMode.setText("");
        }
    }

    public void showSpeed(String message){
        Button currentSpeed = findViewById(R.id.currentSpeed);
        currentSpeed.setText(message);
    }
}