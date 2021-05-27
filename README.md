# Group-12

![alt text](https://media.discordapp.net/attachments/831502886215221248/836180478083727360/Logo2.png)

## What?

The DanceCar is a fun application that was developed in order to let users manually control as well as dance with their SmartCar. The DanceCar application has two different modes that is Dance and Drive that the user can alternate between. In Drive mode the user can control the car with four different speed, angle and braking alternatives so that the user can choose the appropriate mode based on the surrounding environment. In Dance mode the user will be presented with four built in moves where the user can select one or many moves. With the selected moves the user can make the car perform the dance or choose to create a new dance out of the moves selected. The user can also create new moves where the DanceCar records a driving sequance and stores it for later use. During any kind of dancing performance the user has the option to connect to spotify and play a song along with the dance.

## Why?

Because it is guaranteed to be fun and furthermore the DanceCar is a small version of a safe way to dance and interact with people during a pendemic. The aim of this project is to make dancing safe again by letting your car dance for you so that the users can maintain a safe distance. With the DanceCar we are not loosing the dancing culture.

## How?

The DanceCar GUI is developed in Android Studio

A smartphone application developed in Android Studio is used as the GUI in order to let users interact with the DanceCar. The connect between the smartphone application and the SmartCar is done via a local MQTT broker. The SmartCar is developed in Arduion using the smartcar shield library, Spotify connection is developed using the Spotify API.


In the smartphone application users will be able to remot control all functions of the DanceCar. When the smartphone application is opend the DanceCar will connect to a local MQTT broker that is responsible of sending correct data between the smartphone application and the DanceCar. When connecting the DanceCar to Spotify in the smartphone application the user needs to have spotify installed on the same smartphone and be logged in to a Spotify account. The user can select any song from a given playlist. If a user wants a song that is not present in this playlist the user can add songs to this playlist in Spotify. DanceCar is using Spotify API in order to connect to Spotify. 

Make a catalog of dance moves for the car to move and blink in sync with the tempo of a song and make the timing of the execution of the moves be dependent on the tempo of any given song as an input variable. The program will extract the tempos of any given song and have the car dance all night long, ready to be sent off to a COVID-free music festival!

## Set-up

In order to set-up the DanceCar you first need to download (Android studio), (Arduino), (SMCE) and a (MQTT mosquitto broker) that is compatible with your OS. All downloaded programs need to run when using the DanceCar. To get our application you need to clone and run the repository in Android Studio. In Android Studio you need to update Gradle. In Android Studio you need to download an AVD or connect an Android device that has Google Play store installed and API level 29 as minimum. In the device, install Spotify application and log in using you own account.

In order to run SMCE you need to select the smartcar.ino file located in DanceCar repository and compile the sketch. When compiling is complete press "start". After this you can use yor device in order to control all functions of the DanceCar.

## User manual

When Set-up is complete you device will show the Drive mode as default. In this mode you can use your device as a remote driving control. At the top you can switch between Drive mode and Dance mode. Below the switch you can press "SPEED", "ANGLE" or "BRAKE" that will indicate what alternative you want to modify (speed of car, angle when turning, braking distance). In the middle you have the control of direction of the car that will mirror the settings above. At the button you select the setting of your selected mode ("SPEED", "ANGLE" or "BRAKE") where "1" indicates low speed, soft turn and long braking distance and "5" indicates high speed, hard turn and short braking distance.

When starting DanceCar the first time the Dance mode will show you the four predefined moves ("MoonWalk", "SideKick", "ShowOff" and "ChaChaCha") in a list. You can select one or many moves and the moves are stored in the order you select them which you can visually view in the bottom. After this you have the possibility to connect the selected moves to a song by pressing the Spotify logo. When you press the Spotify logo you will be presented with the current song that you are playing, the playback time as well as four new buttons (play, pause, previous and next) that is used to control the music. In this case, when you have connected the DanceCar to Spotify, the DanceCar will play music and dance at the same time when pressing "Dance".

The button "RECORD MOVE" takes you to a new screen where you can remotly drive the car and store that driving sequence as a new Move for future dance. In order to start the recording of the driving sequence press "START". A timer will appear that indicates the maximum length of you recorded driving sequence (15 seconds). When the time is up or if you press "STOP" before the 15 seconds you need to press "SAVE" in order to store this dance sequence. Make sure to give you new move a good name and click "Save" again. Now you can replay this recorded move any time. 

The button "CREATE DANCE" lets you select minimum 2 moves that will be created as one dance in the order you picked the moves. Once you have selected moves in your prefered order you press "CREATE DANCE" and give this dance a good name. Now you have a stored sequence och moves so that you do not need to select multiple moves every time. 

## Resources

**Software**

- SMCE
- Android Studio
- Arduino IDE
- MQTT mosquitto
- SmartCar Library
- Spotify Web API
- Spotify Android SDK
- Postman

## Developers

- Karl Stahre (gusstahrka@student.gu.se)
- Amanda De Souza Turquis (gusdesam@student.gu.se)
- Maja Linder (guskalmas@student.gu.se)
- Meis Salim (gussalime@student.gu.se)
- Navya Pulikandla Satyanarayanachetty (guspulna@student.gu.se)
- Malte Götharsson (gusgothama@student.gu.se)
- Bardha Ahmeti (gusahmeba@student.gu.se)
