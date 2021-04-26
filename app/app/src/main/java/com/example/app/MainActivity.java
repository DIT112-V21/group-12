package com.example.app;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MqttClient mMqttClient;
    private boolean isConnected = false;

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

                final String successfulConnection = "Connected to MQTT broker";
                Log.i(TAG, successfulConnection);
                Toast.makeText(getApplicationContext(), successfulConnection, Toast.LENGTH_SHORT).show();

                mMqttClient.subscribe("/smartcar/ultrasound/front", QOS, null);
                mMqttClient.subscribe("/smartcar/camera", QOS, null);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                final String failedConnection = "Failed to connect to MQTT broker";
                Log.e(TAG, failedConnection);
                Toast.makeText(getApplicationContext(), failedConnection, Toast.LENGTH_SHORT).show();
            }
        }, new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                isConnected = false;

                final String connectionLost = "Connection to MQTT broker lost";
                Log.w(TAG, connectionLost);
                Toast.makeText(getApplicationContext(), connectionLost, Toast.LENGTH_SHORT).show();
            }
/*@Override
/public void messageArrived(String topic, MqttMessage message) throws Exception {
    if (topic.equals("/smartcar/somehthing)) {
*/
