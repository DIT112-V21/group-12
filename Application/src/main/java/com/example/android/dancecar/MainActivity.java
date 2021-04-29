package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.darktheme.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SmartcarMqttController";
    private static final String EXTERNAL_MQTT_BROKER = "aerostun.dev";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883"; //Coonnect local
    private static final String THROTTLE_CONTROL = "/smartcar/control/throttle";
    private static final String STEERING_CONTROL = "/smartcar/control/steering";
    private static final int MOVEMENT_SPEED = 70;
    private static final int IDLE_SPEED = 0;
    private static final int STRAIGHT_ANGLE = 0;
    private static final int STEERING_ANGLE = 50;
    private static final int QOS = 1;
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;

    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private ImageView mCameraView;

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
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //Add code for data to app.
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    void drive(int throttleSpeed, int steeringAngle, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(TAG, notConnected);
            Toast.makeText(getApplicationContext(), notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, actionDescription);
        mMqttClient.publish(THROTTLE_CONTROL, Integer.toString(throttleSpeed), QOS, null);
        mMqttClient.publish(STEERING_CONTROL, Integer.toString(steeringAngle), QOS, null);
    }

    public void moveForward(View view) {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving forward");
    }

    public void moveForwardLeft(View view) {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Moving forward left");
    }

    public void stop(View view) {
        drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stopping");
    }

    public void moveForwardRight(View view) {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Moving forward left");
    }

    public void moveBackward(View view) {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving backward");
    }


    public void driveForward(View view){
        int speed = 80;
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

}


/*public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstpage);

        ImageButton nextPageButton = findViewById(R.id.driveButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        });
    }

    //TODO: CODE TO CHANGE ACTIVITY -- MEIS
    private void changeActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}*/

