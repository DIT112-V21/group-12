package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Timer;
import java.util.TimerTask;


public class NewMoves extends AppCompatActivity {
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
    ImageButton forward;
    ImageButton backward;
    ImageButton left;
    ImageButton right;
    Button speed;
    Button angle;
    Button brake;
    Button one;
    Button two;
    Button three;
    Button four;
    Button currentSpeedMode;
    Button currentAngleMode;
    Button currentBrakeMode;
    Button speedometer;
    EditText move_name, instructions, duration;
    Button save;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        connectToMqttBroker();
        initialiseButtons();

        setContentView(R.layout.activity_new_moves);

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
    }

    public void startDance(View view){
        new startTimer(5);
    }

    public void saveDance(View view){
    }

    public class startTimer {
        Timer timer;

        public startTimer(int seconds) {
            timer = new Timer();
            timer.schedule(new StartMoves(), seconds * 1000);
        }

        class StartMoves extends TimerTask {
            public void run() {
                System.out.println("Time's up!");
                timer.cancel(); //Terminate the timer thread
            }
        }
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
        forward  = findViewById(R.id.arrowUp);
        backward  = findViewById(R.id.arrowDown);
        left  = findViewById(R.id.arrowLeft);
        right  = findViewById(R.id.arrowRight);
        speed = findViewById(R.id.speed);
        angle = findViewById(R.id.angle);
        brake = findViewById(R.id.brake);
        one = findViewById(R.id.button1);
        two  = findViewById(R.id.button2);
        three = findViewById(R.id.button3);
        four = findViewById(R.id.button4);
        currentSpeedMode  = findViewById(R.id.currentSpeedMode);
        currentAngleMode  = findViewById(R.id.currentAngleMode);
        currentBrakeMode  = findViewById(R.id.currentBrakeMode);
        speedometer  = findViewById(R.id.currentSpeed);
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
        button.setColorFilter(Color.parseColor("#ED7D9F88"));
    }

    public void colorButton(Button button){
        button.setBackgroundColor(Color.parseColor("#ED2E3C34"));
    }

    public void uncolorButtons(String type) {
        if (type.equals("mode")) {
            speed.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            angle.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            brake.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        } else if (type.equals("number")) {
            one.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            two.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            three.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            four.setBackgroundColor(Color.parseColor("#ED7D9F88"));
        } else if (type.equals("arrow")) {
            forward.setColorFilter(Color.TRANSPARENT);
            backward.setColorFilter(Color.TRANSPARENT);
            left.setColorFilter(Color.TRANSPARENT);
            right.setColorFilter(Color.TRANSPARENT);
        } else {
            speed.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            angle.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            brake.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            one.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            two.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            three.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            four.setBackgroundColor(Color.parseColor("#ED7D9F88"));
            forward.setColorFilter(Color.TRANSPARENT);
            backward.setColorFilter(Color.TRANSPARENT);
            left.setColorFilter(Color.TRANSPARENT);
            right.setColorFilter(Color.TRANSPARENT);
        }
    }
}