#include <Smartcar.h>
/*
The skeleton for this code is derived from [https://platisd.github.io/smartcar_shield/manual_control_8ino-example.html]
*/
int fSpeed = 0;
int bSpeed = 0;
int lDegrees = -75; // degrees to turn left
int rDegrees = 75;  // degrees to turn right
String direct;

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
    Serial.setTimeout(200);
}

void loop()
{
    handleInput();
    if (handleObstacle1()){
        car.setSpeed(0);
        car.setSpeed(-90);
        delay(300);
        car.setSpeed(0);
        direct = "f";
        }
}

boolean handleObstacle1(){
  int distance = front.getDistance();
  if (distance != 0 && distance < 100){
      return true;
      }
  else{
      return false;    
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
        case 'l': // rotate counter-clockwise going forward
            car.setAngle(lDegrees);
            break;
        case 'r': // turn clock-wise
            car.setAngle(rDegrees);
            break;
        case 'f': // go ahead   
            car.setSpeed(fSpeed);
            car.setAngle(0);
            direct = "f";
            break;
        case 'b': // go back 
            car.setSpeed(bSpeed);
            car.setAngle(0);
            direct = "b";
            break;
        case 'g':
          braking(0.05);
            break;
        case 'h': // break
          braking(0.15);
            break;
        case 'j': // break
           braking(0.3);
            break; 
        case 'k': // break
           braking(0.5);
            break;       
        default:  
             setCarSpeed(input, direct);
            }
        } 
    }

/*
The method setCarSpeed takes the direction and the speed mode as arguments.
For an invalid speed mode the car will stop.
*/

char readInput(){
  if (Serial.available()){
  char input = Serial.read();
  switch(input){
    case 'l':
    car.setAngle(lDegrees);
    break;
    case 'r':
    car.setAngle(rDegrees);
    break;
    case 'f':
    car.setSpeed(fSpeed);
    return 'f';
    break;
    case 'b':
    car.setSpeed(fSpeed * -1);
    return 'b';
    break;
    default: return 'x';
  }
  }
  return 'x';
  
}


void braking(double brakeMode){
  while(fSpeed > 0){
    fSpeed = fSpeed - fSpeed * brakeMode;
    car.setSpeed(fSpeed);
    delay(150);
    char inputChar = readInput();
    if (inputChar == 'f'){
      break;
    }
    if (inputChar == 'b'){
      break;
    }   
  }
}

void setCarSpeed(char input, String direct){
  switch(input){
          case '1':
          if (direct == "f"){
            fSpeed = 10;
            bSpeed = -10;
            car.setSpeed(fSpeed);
          }
          else if(direct == "b"){
            bSpeed = -10;
            fSpeed = 10;
            car.setSpeed(bSpeed);
          }
          else{
            car.setSpeed(0);
          }
          break;
        case '2':
        if (direct == "f"){
            fSpeed = 40;
            bSpeed = -40;
            car.setSpeed(fSpeed);
          }
          else if(direct == "b"){
            bSpeed = -40;
            fSpeed = 40;
            car.setSpeed(bSpeed);
          }
          else{
            car.setSpeed(0);
          }
          break;
        case '3':
        if (direct == "f"){
            fSpeed = 60;
            bSpeed = -60;
            car.setSpeed(fSpeed);
          }
          else if(direct == "b"){
            bSpeed = -60;
            fSpeed = 60;
            car.setSpeed(bSpeed);
          }
          else{
            car.setSpeed(0);
          }
          break;
        case '4':
        if (direct == "f"){
            fSpeed = 90;
            bSpeed = -90;
            car.setSpeed(fSpeed);
          }
          else if(direct == "b"){
            bSpeed = -90;
            fSpeed = 90;
            car.setSpeed(bSpeed);
          }
          else{
            car.setSpeed(0);
          }
          break;
          default: // if you receive something that you don't know, just stop
          car.setSpeed(0);
          car.setAngle(0);
       }
  }
