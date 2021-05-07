package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    EditText move_name, direction;
    Button insert, update, delete, view;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        connectToMqttBroker();

        move_name = findViewById(R.id.move_name);
        direction = findViewById(R.id.direction);

        insert = findViewById(R.id.btnInsert);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        view = findViewById(R.id.btnView);
        DB = new DBHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String move_nameTXT = move_name.getText().toString();
                String directionTXT = direction.getText().toString();

                Boolean checkInsertData = DB.insertMoveData(move_nameTXT, directionTXT);
                if(checkInsertData==true)
                    Toast.makeText(MainActivity.this, "New move inserted!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "New move not inserted!", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String move_nameTXT = move_name.getText().toString();
                String directionTXT = direction.getText().toString();

                Boolean checkUpdateData = DB.updateMoveData(move_nameTXT, directionTXT);
                if(checkUpdateData==true)
                    Toast.makeText(MainActivity.this, "Move updated!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Move not updated!", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String move_nameTXT = move_name.getText().toString();

                Boolean checkDeleteData = DB.deleteData(move_nameTXT);
                if(checkDeleteData==true)
                    Toast.makeText(MainActivity.this, "Move deleted!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Move not deleted!", Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DB.getData();
                if(res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No move found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Move name :"+res.getString(0)+"\n");
                    buffer.append("Direction :"+res.getString(1)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("New move!");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
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