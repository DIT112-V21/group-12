package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.sql.SQLOutput;
import java.util.ArrayList;

public class DancingActivity extends AppCompatActivity {

    //TODO: ASK AT THE STAND UP
    //the list should be in the on create. Why is not private?
    private ArrayList<DanceMove> danceMoves;
    private ArrayList<CreatedDanceMove> createdDanceMoves;
    private ArrayList<Choreography> chorMoves;
    private ArrayList<Choreography> selectedChorMoves;
    private ArrayList<String> selectedChorMovesText;
    private ArrayList<DanceMove> selectedMoves;
    private ArrayList<String> selectedMoveText;
    private LinearLayout lLayout;
    private LinearLayout rLayout;
    private CheckBox checkBox;

    private DBHelper dbHelper;
    private Choreography choreography;
    private DanceMove danceMove;

    private  Button createChor;
    private String myText;
    private boolean isConnected = false;
    private MqttClient mMqttClient;
    private static final String TAG = "SmartcarMqttController";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";

    public ArrayList<CreatedDanceMove> getCreatedDanceMoves() {
        return createdDanceMoves;
    }

    public ArrayList<DanceMove> getDanceMoves() {
        return danceMoves;
    }

    public void setDanceMoves(DanceMove danceMove) {
        danceMoves.add(danceMove);
    }

    public void setCreatedDanceMoves(CreatedDanceMove createdDanceMove) {
        createdDanceMoves.add(createdDanceMove);
    }

    public DancingActivity(){
        danceMoves = new ArrayList<>();
        createdDanceMoves = new ArrayList<>();
        chorMoves = new ArrayList<>();
        selectedChorMoves = new ArrayList<>();
        selectedChorMovesText = new ArrayList<>();
        selectedMoves = new ArrayList<>();
        selectedMoveText = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DBHelper myDB = new DBHelper(this);
        setContentView(R.layout.activity_dancing);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        connectToMqttBroker();

        DanceMove dance1  = new DanceMove("MoonWalk");
        danceMoves.add(dance1);
        DanceMove dance2  = new DanceMove("SideKick");
        danceMoves.add(dance2);
        DanceMove dance3  = new DanceMove("ShowOff");
        danceMoves.add(dance3);
        DanceMove dance4  = new DanceMove("ChaChaCha");
        danceMoves.add(dance4);
        //danceMoves.add(dbHelper);
        Log.d(TAG, "onCreate: DanceMoves holds: " + danceMoves.toString());
        lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
        rLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
        createChor = findViewById(R.id.createChoreography);
        createChor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(DancingActivity.this);
                myDialog.setTitle("Name");
                final EditText name = new EditText(DancingActivity.this);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                myDialog.setView(name);
                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myText = name.getText().toString();

                                createChoreography(myText);

                                myDB.insertChorMove(selectedMove, myText);
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

        createDanceMovesCheckboxes();

        createChoreographyCheckboxes();
    }

    View.OnClickListener checkBoxMove(final CheckBox checkBox, final DanceMove dance){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    selectedMoves.add(dance);
                    Log.d(TAG, "onClick: id is: " + dance.getId());
                    TextView current = findViewById(R.id.currentDances);
                    for(int i = 0; i < selectedMoves.size(); i++){
                        String name = selectedMoves.get(i).getDanceName();
                        if(!selectedMoveText.contains(name)){
                            selectedMoveText.add(name);
                        }
                        current.setText(selectedMoveText.toString());
                    }
                }else{
                    String removeName = dance.getDanceName();
                    selectedMoves.remove(dance);
                    TextView current = findViewById(R.id.currentDances);
                    selectedMoveText.remove(removeName);
                    current.setText(selectedMoveText.toString());
                    }
                Log.d("onClick: ", "CheckBox ID: " + checkBox.getId() + " Text: " + checkBox.getText().toString());
            }
        };
    }

    public void createDanceMovesCheckboxes() {
        for (int i = 0; i < danceMoves.size(); i++) {
            DanceMove dance = danceMoves.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(dance.getId());
            checkBox.setText(dance.getDanceName());
            checkBox.setOnClickListener(checkBoxMove(checkBox, dance));
            lLayout.addView(checkBox);
        }
    }

    public void createChoreographyCheckboxes() {
        for (int i = 0; i < chorMoves.size(); i++) {
            Choreography chor = chorMoves.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(chor.getChorMoveID());
            checkBox.setText(chor.getChorName());
            checkBox.setOnClickListener(checkBoxDance(checkBox, chor));
            rLayout.addView(checkBox);
        }
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, DrivingActivity.class);
        startActivity(intent);
    }

    public void createChoreography(String name){
        if(!selectedMoves.isEmpty() && selectedMoves.size() > 1 && selectedMoves.size() < 101) {
            ArrayList<DanceMove> fullChor = new ArrayList<DanceMove>();
            for(int i = 0; i < selectedMoves.size(); i++){
                fullChor.add(selectedMoves.get(i));
            }
            Choreography newChor = new Choreography(fullChor,  name);
            chorMoves.add(newChor);
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
            int id = 1;  ///TODO add new id!!!!!!
            checkBox = new CheckBox(this);
            checkBox.setId(id);
            checkBox.setText(name);
            checkBox.setOnClickListener(checkBoxDance(checkBox, newChor));
            lLayout.addView(checkBox);
            Toast.makeText(DancingActivity.this, myText + " was created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please select at least 2 moves but you can choose 100 :) ", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBackToDanceMenu(View view){
        Intent intent = new Intent(this, DanceModeActivity.class);
        startActivity(intent);
    }

    public void makeCarDance(View view){
        if(selectedMoves.size() > 0) {
            for (int i = 0; i < selectedMoves.size(); i++) {
                DanceMove dance = selectedMoves.get(i);
                String name = dance.getDanceName();

                if (dance.isCreated()) {
                    for (CreatedDanceMove createdDance : createdDanceMoves) {
                        if (dance.getDanceName().equals(createdDance.getName())) {
                            // TODO: add connection to mqtt
                            for (IndividualMove individualMove : createdDance.getIndividualMoves()) {
                                String carInstruction = individualMove.getCarInstruction();
                                long duration = individualMove.getDuration() / 1000000;
                                mMqttClient.publish("smartcar/duration", Long.toString(duration), 1, null);
                                mMqttClient.publish("smartcar/direction", carInstruction, 1, null);
                            }
                            mMqttClient.publish("smartcar/stopDance", "0", 1, null);
                        } else {
                            mMqttClient.publish("smartcar/makeCarDance/" + name, "1", 1, null);
                        }
                    }
                }
            }
        } else if(selectedChorMoves.size() > 0){
            for(int i = 0; i <selectedChorMoves.size(); i++){
                Choreography chor = selectedChorMoves.get(i);
                ArrayList<DanceMove> fullDance = chor.getSelectedDances();
                Log.d(TAG, "makeCarDance: fulldance is " + fullDance.toString());
                for(int j = 0; j <fullDance.size(); j++){
                    DanceMove dance = fullDance.get(j);
                    String name = dance.getDanceName();
                    mMqttClient.publish("smartcar/makeCarDance/" + name ,"1",  1, null);
                }
            }
        }else{
            Toast.makeText(this, "You need to select a dance", Toast.LENGTH_SHORT).show();
        }
    }

    public void recordNewMove(View view){
        Intent intent = new Intent(DancingActivity.this, RecordDanceMoveActivity.class);
        startActivity(intent);
    }

    View.OnClickListener checkBoxDance(final CheckBox checkBox, final Choreography chor){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    selectedChorMoves.add(chor);
                    Log.d(TAG, "onClick: id is: " + chor.getChorMoveID());
                    TextView current = findViewById(R.id.currentDances);
                    for(int i = 0; i < selectedChorMoves.size(); i++){
                        String name = selectedChorMoves.get(i).getChorName();
                        if(!selectedChorMovesText.contains(name)){
                            selectedChorMovesText.add(name);
                        }
                        current.setText(selectedChorMovesText.toString());
                    }
                }else{
                    String removeName = chor.getChorName();
                    selectedChorMoves.remove(chor);
                    TextView current = findViewById(R.id.currentDances);
                    selectedChorMovesText.remove(removeName);
                    current.setText(selectedChorMovesText.toString());
                    Log.d(TAG, "onClick: removed to the selectedChorMoves");
                }
                Log.d("onClick: ", "CheckBox ID: " + checkBox.getId() + " Text: " + checkBox.getText().toString());
            }
        };
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

