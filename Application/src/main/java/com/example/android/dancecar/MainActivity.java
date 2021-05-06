package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        updateButtons(direction);
        mMqttClient.publish("smartcar/forward", Integer.toString(speed), 1, null);
    }

    public void driveBackward(View view){
        int backSpeed = -80;
        direction = "backward";
        updateButtons(direction);
        mMqttClient.publish("smartcar/backward", Integer.toString(backSpeed),1, null );
    }

    public void driveLeft(View view){
        int leftAngle = -60;
        direction = "left";
        updateButtons(direction);
        mMqttClient.publish("smartcar/left", Integer.toString(leftAngle), 1, null);
    }

    public void driveRight(View view){
        int rightAngle = 60;
        direction = "right";
        updateButtons(direction);
        mMqttClient.publish("smartcar/right", Integer.toString(rightAngle), 1, null);
    }

    public void driveStop(View view){
        int stop = 0;
        mMqttClient.publish("smartcar/stop", Integer.toString(stop), 1, null);
    }

    public void updateButtons(String direction){
        ImageButton forward = findViewById(R.id.arrowUp);
        ImageButton backward = findViewById(R.id.arrowDown);
        ImageButton left = findViewById(R.id.arrowLeft);
        ImageButton right = findViewById(R.id.arrowRight);
        switch (direction) {
            case "forward":
                pressButton(forward);
                unPressButton(backward, left, right);
                break;
            case "backward":
                pressButton(backward);
                unPressButton(forward, left, right);
                break;
            case "left":
                pressButton(left);
                unPressButton(forward, backward, right);
                break;
            case "right":
                pressButton(right);
                unPressButton(forward, backward, left);
                break;
        }
    }

    public void pressButton(ImageButton button){
        button.setColorFilter(Color.parseColor("#ED7D9F88"));
    }

    public void unPressButton(ImageButton button1, ImageButton button2, ImageButton button3){
        button1.setColorFilter(Color.TRANSPARENT);
        button2.setColorFilter(Color.TRANSPARENT);
        button3.setColorFilter(Color.TRANSPARENT);
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