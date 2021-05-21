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


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.util.ArrayList;

public class DanceMoves extends AppCompatActivity {
    ArrayList<DaneMoveObject> danceMoves = new ArrayList<DaneMoveObject>();
    ArrayList<ChorMoves> chorMoves = new ArrayList<ChorMoves>();
    ArrayList<ChorMoves> selectedChorMoves = new ArrayList<ChorMoves>();
    ArrayList selectedChorMovesText = new ArrayList();
    ArrayList<DaneMoveObject> selectedMove = new ArrayList<DaneMoveObject>();
    ArrayList selectedMoveText = new ArrayList();
    LinearLayout lLayout;
    LinearLayout rLayout;
    CheckBox checkBox;
    private static final String CLIENT_ID = "764ef5ad07284dd499fcb8bb5604bc26";
    private static final String REDIRECT_URI = "dancing-car-app://callback";
    private SpotifyAppRemote mSpotifyAppRemote;


    private  Button createChor;
    private String myText;
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
        DaneMoveObject dance1  = new DaneMoveObject("MoonWalk");
        danceMoves.add(dance1);
        DaneMoveObject dance2  = new DaneMoveObject("SideKick");
        danceMoves.add(dance2);
        DaneMoveObject dance3  = new DaneMoveObject("ShowOff");
        danceMoves.add(dance3);
        DaneMoveObject dance4  = new DaneMoveObject("ChaChaCha");
        danceMoves.add(dance4);
        Log.d(TAG, "onCreate: DanceMoves holds: " + danceMoves.toString());
        lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
        rLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
        createChor = findViewById(R.id.createChoreography);
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
            DaneMoveObject dance = danceMoves.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(dance.getId());
            checkBox.setText(dance.getDanceName());
            checkBox.setOnClickListener(checkBoxMove(checkBox, dance));
            lLayout.addView(checkBox);

        }

        for (int i = 0; i < chorMoves.size(); i++) {
            ChorMoves chor = chorMoves.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(chor.getChorMoveID());
            checkBox.setText(chor.getChorName());
            checkBox.setOnClickListener(checkBoxDance(checkBox, chor));
            rLayout.addView(checkBox);
        }
    }

    View.OnClickListener checkBoxMove(final CheckBox checkBox, final DaneMoveObject dance){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    selectedMove.add(dance);
                    Log.d(TAG, "onClick: id is: " + dance.getId());
                    TextView current = findViewById(R.id.currentDances);
                    for(int i = 0; i < selectedMove.size(); i++){
                        String name = selectedMove.get(i).getDanceName();
                        if(!selectedMoveText.contains(name)){
                            selectedMoveText.add(name);
                        }
                        current.setText(selectedMoveText.toString());
                    }
                }else{
                    String removeName = dance.getDanceName();
                    selectedMove.remove(dance);
                    TextView current = findViewById(R.id.currentDances);
                    selectedMoveText.remove(removeName);
                    current.setText(selectedMoveText.toString());
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
        if(!selectedMove.isEmpty() && selectedMove.size() > 1 && selectedMove.size() < 101) {
            ArrayList<DaneMoveObject> fullChor = new ArrayList<DaneMoveObject>();
            for(int i = 0; i < selectedMove.size(); i++){
                fullChor.add(selectedMove.get(i));
            }
            ChorMoves newChor = new ChorMoves(fullChor,  name);
            chorMoves.add(newChor);
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
            int id = 1;  ///TODO add new id!!!!!!
            checkBox = new CheckBox(this);
            checkBox.setId(id);
            checkBox.setText(name);
            checkBox.setOnClickListener(checkBoxDance(checkBox, newChor));
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
        if(selectedMove.size() > 0){
            for(int i = 0; i < selectedMove.size(); i++){
                DaneMoveObject dance = selectedMove.get(i);
                String name = dance.getDanceName();

                mMqttClient.publish("smartcar/makeCarDance/" + name ,"1", 1, null);
            }
        }
         else if(selectedChorMoves.size() > 0){
            for(int i = 0; i <selectedChorMoves.size(); i++){
                ChorMoves chor = selectedChorMoves.get(i);
                ArrayList<DaneMoveObject> fullDance = chor.getSelectedDances();
                Log.d(TAG, "makeCarDance: fulldance is " + fullDance.toString());
                for(int j = 0; j <fullDance.size(); j++){
                    DaneMoveObject dance = fullDance.get(j);
                    String name = dance.getDanceName();
                    mMqttClient.publish("smartcar/makeCarDance/" + name ,"1",  1, null);
                }
            }
        }else{
            Toast.makeText(this, "You need to select a dance", Toast.LENGTH_SHORT).show();
        }
    }

    public void recordNewMove(View view){
        Intent intent = new Intent(DanceMoves.this, NewMoves.class);
        startActivity(intent);
    }

    public void goToSpotify(View view){
        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID).setRedirectUri(REDIRECT_URI).showAuthView(true).build();

        Log.d(TAG, "onStart: blabla : " + connectionParams);
        SpotifyAppRemote.connect(getApplicationContext(), connectionParams, new Connector.ConnectionListener() {

            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! Yay!");
                // Now you can start interacting with App Remote
                connected();

            }

            public void onFailure(Throwable throwable) {
                Log.e("MyActivity", throwable.getMessage(), throwable);

                // Something went wrong when attempting to connect! Handle errors here
            }
        });
    }

    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }


    View.OnClickListener checkBoxDance(final CheckBox checkBox, final ChorMoves chor){
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

