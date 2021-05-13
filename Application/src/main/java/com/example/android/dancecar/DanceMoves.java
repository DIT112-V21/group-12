package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class DanceMoves extends AppCompatActivity {
    ArrayList<DanceMove> danceMoves = new ArrayList<DanceMove>();
    ArrayList<DanceMove> choreography = new ArrayList<DanceMove>();
    ArrayList<DanceMove> selectedDance = new ArrayList<DanceMove>();
    LinearLayout lLayout;
    LinearLayout rLayout;
    CheckBox checkBox;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText name;
    private Button save;
    private Button cancel;
    private  Button createChor;
    private String myText;
    private Button createNewDance;

    private boolean isConnected = false;
    private MqttClient mMqttClient;
    private static final String TAG = "SmartcarMqttController";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_moves);

        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        connectToMqttBroker();

        DanceMove dance1  = new DanceMove("Testing",1);
        danceMoves.add(dance1);
        DanceMove dance2  = new DanceMove("Dance 2",2);
        danceMoves.add(dance2);
        DanceMove dance3  = new DanceMove("Dance 3",3);
        danceMoves.add(dance3);
        DanceMove dance4  = new DanceMove("Dance 4",4);
        danceMoves.add(dance4);
        lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
        rLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
        createChor = findViewById(R.id.createChoreography);
        createNewDance = findViewById(R.id.createNewDanceMove);
        createNewDance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(DanceMoves.this);
                myDialog.setTitle("Name");

                final EditText name = new EditText(DanceMoves.this);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                myDialog.setView(name);

                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myText = name.getText().toString();
                        addNewDance(myText);
                    }
                });

                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        createChor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(DanceMoves.this);
                myDialog.setTitle("Name");

                final EditText name = new EditText(DanceMoves.this);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                myDialog.setView(name);

                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myText = name.getText().toString();
                        createChoreography(myText);
                    }
                });

                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        for (int i = 0; i < danceMoves.size(); i++) {
            DanceMove dance = danceMoves.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(dance.getId());
            checkBox.setText(dance.getDanceName());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, dance));
            lLayout.addView(checkBox);
        }

        for (int i = 0; i < choreography.size(); i++) {
            DanceMove dance = choreography.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(dance.getId());
            checkBox.setText(dance.getDanceName());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, dance));
            rLayout.addView(checkBox);
        }
    }



    View.OnClickListener getOnClickDoSomething(final CheckBox checkBox, final DanceMove dance){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    selectedDance.add(dance);
                    TextView current = findViewById(R.id.currentDances);
                    current.setText(selectedDance.toString());
                }else{
                    selectedDance.remove(dance);
                    TextView current = findViewById(R.id.currentDances);
                    current.setText(selectedDance.toString());
                }
                Log.d("onClick: ", "CheckBox ID: " + checkBox.getId() + " Text: " + checkBox.getText().toString());
            }
        };
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createChoreography(String name){
        if(!selectedDance.isEmpty() && selectedDance.size() > 1 && selectedDance.size() < 101) {
            DanceMove newChor = new DanceMove(name, 11);
            choreography.add(newChor);
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
            int id = 1;
            checkBox = new CheckBox(this);
            checkBox.setId(id);
            checkBox.setText(name);
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, newChor));
            lLayout.addView(checkBox);
            Toast.makeText(DanceMoves.this, myText + " was created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please select at least 2 moves but you can choose 100 :) ", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBackToDanceMenu(View view){
        Intent intent = new Intent(this, DanceMode.class);
        startActivity(intent);
    }

    public void makeCarDance(View view){
        if(selectedDance.size() > 0){
            for(int i = 0; i < selectedDance.size(); i++){
                DanceMove dance = selectedDance.get(i);
                int id = dance.getId();
                String newID = Integer.toString(id);
                mMqttClient.publish("smartcar/makeCarDance", newID , 1, null);
            }
        }else{
            Toast.makeText(this, "You need to select a dance", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNewDance(String name){
        if(!selectedDance.isEmpty() && selectedDance.size() > 1 && selectedDance.size() < 101) {
            DanceMove newDance = new DanceMove(name, 10);
            danceMoves.add(newDance);
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
            checkBox = new CheckBox(this);
            int id = 1;
            checkBox.setId(id);
            checkBox.setText(name);
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, newDance));
            lLayout.addView(checkBox);
            Toast.makeText(DanceMoves.this, myText + " was created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please select a dance move", Toast.LENGTH_SHORT).show();
        }
    }

    public void recordNewMove(View view){
        Intent intent = new Intent(DanceMoves.this, NewMoves.class);
        startActivity(intent);
    }


    private void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    Log.d(TAG, "onSuccess: connected to MQTT");
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
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }
}

