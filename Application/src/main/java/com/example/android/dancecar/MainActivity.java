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
    private String currentSpeed;
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

    public void driveForward(View view){
        //speedMode.setNumber(1);
        //speedMode.setActivated(true);
        int speed = 60;
        direction = "forward";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/forward", Integer.toString(speed), 1, null);
    }

    public void driveBackward(View view){
        int backSpeed = -80;
        direction = "backward";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/backward", Integer.toString(backSpeed),1, null );
    }

    public void driveLeft(View view){
        int leftAngle = -60;
        direction = "left";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/left", Integer.toString(leftAngle), 1, null);
    }

    public void driveRight(View view){
        int rightAngle = 60;
        direction = "right";
        colorArrowButtons(direction);
        mMqttClient.publish("smartcar/right", Integer.toString(rightAngle), 1, null);
    }

    public void driveStop(View view){
        int stop = 0;
        mMqttClient.publish("smartcar/stop", Integer.toString(stop), 1, null);
    }

    /*
    Only one arrow button can show as pressed/color at a time.
    The other buttons will get unpressed when one button is pressed.
    */
    public void colorArrowButtons(String direction){
        ImageButton forward = findViewById(R.id.arrowUp);
        ImageButton backward = findViewById(R.id.arrowDown);
        ImageButton left = findViewById(R.id.arrowLeft);
        ImageButton right = findViewById(R.id.arrowRight);
        switch (direction) {
            case "forward":
                unColorImageButton(forward, backward, left, right);
                colorImageButton(forward);
                break;
            case "backward":
                unColorImageButton(forward, backward, left, right);
                colorImageButton(backward);
                break;
            case "left":
                unColorImageButton(forward, backward, left, right);
                colorImageButton(left);
                break;
            case "right":
                unColorImageButton(forward, backward, left, right);
                colorImageButton(right);
                break;
        }
    }

    public void colorImageButton(ImageButton button){
        button.setColorFilter(Color.parseColor("#ED7D9F88"));
    }

    public void unColorImageButton(ImageButton button1, ImageButton button2, ImageButton button3, ImageButton button4){
        button1.setColorFilter(Color.TRANSPARENT);
        button2.setColorFilter(Color.TRANSPARENT);
        button3.setColorFilter(Color.TRANSPARENT);
        button4.setColorFilter(Color.TRANSPARENT);
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
            //TODO: if no mode is activated, what should happen?
        }
    }

    /*
    Only one number button can show as pressed/colored at a time.
    The other buttons will get unpressed when one button is pressed.
     */
    public void colorNumberButtons(String number){
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        Button four = findViewById(R.id.button4);
        switch (number) {
            case "one":
                unColorNumberButton(one, two, three, four);
                colorButton(one);
                break;
            case "two":
                unColorNumberButton(one, two, three, four);
                colorButton(two);
                break;
            case "three":
                unColorNumberButton(one, two, three, four);
                colorButton(three);
                break;
            case "four":
                unColorNumberButton(one, two, three, four);
                colorButton(four);
                break;
        }
    }

    public void colorButton(Button button){
        button.setBackgroundColor(Color.parseColor("#ED2E3C34"));
    }

    public void unColorNumberButton(Button button1, Button button2, Button button3, Button button4){
        button1.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button2.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button3.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button4.setBackgroundColor(Color.parseColor("#ED7D9F88"));
    }

    /*
    A number is shown underneath the three different mode buttons
    to show in which number that mode is currently in.
    */
    public void updateAndShowModeNumbers(){
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
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        Button four = findViewById(R.id.button4);
        int number = getCurrentModeNumber();

        if (number == 1) {
            unColorNumberButton(one, two, three, four);
            colorButton(one);
        } else if (number == 2) {
            unColorNumberButton(one, two, three, four);
            colorButton(two);
        } else if (number == 3) {
            unColorNumberButton(one, two, three, four);
            colorButton(three);
        } else if (number == 4) {
            unColorNumberButton(one, two, three, four);
            colorButton(four);
        } else if (number == 0) {
            unColorNumberButton(one, two, three, four);
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
        Button speed = findViewById(R.id.speed);
        Button angle = findViewById(R.id.angle);
        Button brake = findViewById(R.id.brake);

        if (speedMode.isActivated()) {
            unColorModeButton(angle, brake, speed);
            colorButton(speed);
        } else if (angleMode.isActivated()) {
            unColorModeButton(angle, brake, speed);
            colorButton(angle);
        } else if (brakeMode.isActivated()) {
            unColorModeButton(angle, brake, speed);
            colorButton(brake);
        } else {
            unColorModeButton(angle, brake, speed);
        }
    }

    public void unColorModeButton(Button button1, Button button2, Button button3){
        button1.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button2.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        button3.setBackgroundColor(Color.parseColor("#ED7D9F88"));
    }

    public void showSpeed(String message){
        Button currentSpeed = findViewById(R.id.currentSpeed);
        currentSpeed.setText(message);
    }

    public void resetSettings(){
        // TODO: global buttons?
        ImageButton forward = findViewById(R.id.arrowUp);
        ImageButton backward = findViewById(R.id.arrowDown);
        ImageButton left = findViewById(R.id.arrowLeft);
        ImageButton right = findViewById(R.id.arrowRight);
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        Button four = findViewById(R.id.button4);
        Button speed = findViewById(R.id.speed);
        Button angle = findViewById(R.id.angle);
        Button brake = findViewById(R.id.brake);
        speedMode.setActivated(false);
        angleMode.setActivated(false);
        brakeMode.setActivated(false);
        speedMode.setNumber(0);
        angleMode.setNumber(0);
        brakeMode.setNumber(0);
        updateAndShowModeNumbers();
        numberAlreadyActivated();

        unColorImageButton(forward, backward, left, right);
        unColorModeButton(speed, angle, brake);
        unColorNumberButton(one, two, three, four);

    }
    /*
    When getSpeed() 0.0000 then uncolor(allModes, allNumbers, allArrows)
     */
}