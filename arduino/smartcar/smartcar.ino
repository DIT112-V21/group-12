#include <Smartcar.h>
#include <MQTT.h>
#include <WiFi.h>

#ifndef __SMCE__
WiFiClient net;
#endif
MQTTClient mqtt;

/*
The skeleton for this code is derived from [https://platisd.github.io/smartcar_shield/manual_control_8ino-example.html]
*/
int fSpeed = 40;
int bSpeed = -40;
int lDegrees = -20; // degrees to turn left
int rDegrees = 20;  // degrees to turn right
boolean backward = false;
boolean forward = false;
boolean left = false;
boolean right = false;
boolean braking = false;
boolean brakePress = false;
boolean anglePress = false;
boolean speedPress = false;


const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

void setup()
{
    Serial.begin(9600);
   #ifndef __SMCE__
    mqtt.begin(net);
   #else
   mqtt.begin(WiFi);
   #endif
    if (mqtt.connect("arduino", "public", "public"));{
      mqtt.subscribe("smartcar/#", 1);
      mqtt.onMessage([](String topic, String message){
        if (topic == "smartcar/forward"){
          car.setSpeed(fSpeed);
          car.setAngle(0);
          right = false;
          left = false;
          forward = true;
          backward = false;
        } else if (topic == "smartcar/backward"){
          car.setSpeed(bSpeed);
          car.setAngle(0);
          left = false;
          right = false;
          backward = true;
          forward = false;
        } else if (topic == "smartcar/left"){
          car.setAngle(lDegrees);
          left = true;
          right = false;
        }else if (topic == "smartcar/right"){
          car.setAngle(rDegrees);
          left = false;
          right = true;
        }else if (topic == "smartcar/stop"){
          braking = true;
          forward = false;
          backward = false;
          carBraking(0.5);
        }else if (topic == "smartcar/speedOne"){
          if(brakePress == true){
            carBraking(0.05);
            speedPress = false;
            anglePress = false;
          }else if(speedPress == true){
            carSpeed(10);
            brakePress = false;
            anglePress = false;
          }else if(anglePress == true){
            steeringAngle(5);
            brakePress = false;
            speedPress = false;
            }
         }else if (topic == "smartcar/speedTwo"){
            if(brakePress == true){
            carBraking(0.15);
            speedPress = false;
            anglePress = false;
          }else if(speedPress == true){
            carSpeed(40);
            brakePress = false;
            anglePress = false;
          }else if(anglePress == true){
            steeringAngle(15);
            brakePress = false;
            speedPress = false;
            }
        }else if (topic == "smartcar/speedThree"){
          if(brakePress == true){
            carBraking(0.3);
            speedPress = false;
            anglePress = false;
          }else if(speedPress == true){
            carSpeed(60);
            brakePress = false;
            anglePress = false;
          }else if(anglePress == true){
            steeringAngle(30);
            brakePress = false;
            speedPress = false;
            }
        }else if (topic == "smartcar/speedFour"){
          if(brakePress == true){
            carBraking(0.5);
            speedPress = false;
            anglePress = false;
          }else if(speedPress == true){
            carSpeed(90);
            brakePress = false;
            anglePress = false;
          }else if(anglePress == true){
            steeringAngle(30);
            brakePress = false;
            speedPress = false;
            }
        }else if(topic == "smartcar/speedPress"){
          speedPress = true;
          brakePress = false;
          anglePress = false;
          braking = false;
        }else if(topic == "smartcar/brakePress"){
          speedPress = false;
          brakePress = true;
          anglePress = false;
          braking = true;
          backward = false;
          forward = false;
          left = false;
          right = false;
        }else if(topic == "smartcar/anglePress"){
          speedPress = false;
          brakePress = false;
          anglePress = true;
          braking = false;
        }
        else{
          Serial.println(topic + " " + message);
        }
      });
    }
}


void loop()
{
    if(mqtt.connected()){
      mqtt.loop();
    }
     handleInput();
    if (handleObstacle()){
        car.setSpeed(-90);
        delay(300);
        car.setSpeed(0);
    }
}

/*
When standing still you need to choose a direction and then enter a speed mode 1-4.
Invalid input will still get passed to setCarSpeed which will cause the car to stop.
*/


void handleInput(){
  if (Serial.available()){
    char input = Serial.read();
    String value;
        switch (input)
        {
        case 'l': // Set steering angle to negative number(lDegrees/left).
          car.setAngle(lDegrees);
          left = true;
          right = false;
          break;
        case 'r': // Set steering angle to positive number(rDegrees/right).
          car.setAngle(rDegrees);
          left = false;
          right = true;
          break;
        case 'f': // go ahead
          car.setSpeed(fSpeed);
          car.setAngle(0);
          right = false;
          left = false;
          forward = true;
          backward = false;
          break;
        case 'b': // go back
          car.setSpeed(bSpeed);
          car.setAngle(0);
          left = false;
          right = false;
          backward = true;
          forward = false;
          break;
        case 'g': //break
          braking = true;
          forward = false;
          backward = false;
          carBraking(0.05);
          break;
        case 'h': // break
          braking = true;
          forward = false;
          backward = false;
          carBraking(0.15);
          break;
        case 'j': // break
          braking = true;
          forward = false;
          backward = false;
          carBraking(0.3);
          break;
        case 'k': // break
          braking = true;
          forward = false;
          backward = false;
          carBraking(0.5);
          break;
        case '5':
          steeringAngle(5); //Steering angle mode (User must enter left or right before entering angle).
          break;
        case '6':
          steeringAngle(15);
          break;
        case '7':
          steeringAngle(30);
          break;
        case '8':
          steeringAngle(60);
          break;
        case '1':
          carSpeed(10); //Car speed mode (User must enter forward or backward before entering car speed).
          break;
        case '2':
          carSpeed(40);
          break;
        case '3':
          carSpeed(60);
          break;
        case '4':
          carSpeed(90);
          break;
        default: // if you receive something that you don't know, just stop
          car.setSpeed(0);
          car.setAngle(0);
        }
    }
 }

void carBraking(double brakeMode){
  while(fSpeed >= 0){
    fSpeed = fSpeed - fSpeed * brakeMode;
    car.setSpeed(fSpeed);
    delay(150);
    handleInput();
    if (forward == true || backward == true || fSpeed == 0){
        braking = false;
        break;
    }
    else if(left == true){
        car.setAngle(lDegrees);
    } else if (right == true)
        car.setAngle(rDegrees);
  }
}

void steeringAngle(int angle){
  if(right == true){
     rDegrees = angle;
     car.setAngle(rDegrees);
  }else if (left == true){
     lDegrees = angle * -1;
     car.setAngle(lDegrees);
  }else{
    return;
  }
}

void carSpeed(int carSpeed){
  if (forward == true){
     fSpeed = carSpeed;
     bSpeed = carSpeed * -1;
     car.setSpeed(fSpeed);
  }
  else if(backward == true){
     bSpeed = carSpeed * -1;
     fSpeed = carSpeed;
     car.setSpeed(bSpeed);
  }
  else{
     car.setSpeed(0);
  }
}

boolean handleObstacle(){
  int allowedDistance = 100;
  int distance = front.getDistance();
  if (distance != 0 && distance < allowedDistance){
      return true;
  }
  else{
      return false;
  }
}
