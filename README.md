# Group-12

![alt text](https://media.discordapp.net/attachments/831502886215221248/836180478083727360/Logo2.png)

## What?

The DanceCar is a fun application that was developed in order to let users remote control as well as dance with their SmartCar. DanceCar has two different use modes to alternate between Dance and Drive. In Drive mode the user can use our application to control the car and drive with four different speed, angle and braking alternatives so that they can choose the appropriate mode based on the surrounding environment. In Dance mode the user will be able to have the car perform dance moves accompanied by music which is provided via another application (Spotify). Alternatively, the user can also create new moves as the DanceCar records a driving sequence and stores it for later use. During any kind of dancing performance the user has the option to connect to spotify and play a song along with the dance.

## Why?

Because it is guaranteed to be fun and furthermore the DanceCar is a small version of a safe way to dance and interact with people during a pendemic. The aim of this project is to make dancing safe again by letting your car dance for you so that the users can maintain a safe distance. With the DanceCar we are not loosing the dancing culture.

## How?

The DanceCar is an application developed over the [SmartCar](https://platisd.github.io/smartcar_shield/)Library. A smartphone application developed in Android Studio is used as the GUI, enabling users to interact with the DanceCar. The connect between the smartphone application and the SmartCar is done via a local MQTT broker. The SmartCar is developed in Arduino using the smartcar shield library, Spotify connection is developed using the Spotify API.


In the smartphone application users will be able to remote control all functions of the DanceCar, mimmicing physical car driving. When the smartphone application is opend the DanceCar will connect to a local MQTT broker that is responsible of sending correct data between the smartphone application and the DanceCar. When connecting the DanceCar to Spotify in the smartphone application the user needs to have spotify installed on the same smartphone and be logged in to a Spotify account. The user can select any song from a given playlist. If a user wants a song that is not present in this playlist the user can add songs to this playlist in Spotify. DanceCar is using Spotify APIs in order to connect to Spotify and retrieve music. 

Make a catalog of dance moves for the car to move and blink in sync with the tempo of a song and make the timing of the execution of the moves be dependent on the tempo of any given song as an input variable. The program will extract the tempos of any given song and have the car dance all night long, ready to be sent off to a COVID-free music festival!

## Set-up

In order to set-up the DanceCar you first need to download [Android Studio](https://developer.android.com/studio), [Arduino IDE](https://www.arduino.cc/), [SMCE](https://github.com/ItJustWorksTM/smce-gd/releases) and a [MQTT mosquitto broker](https://mosquitto.org/) that is compatible with your OS. All downloaded programs need to run when using the DanceCar. To get our application you need to clone and run the repository in Android Studio. Where you initially need to update _Gradle_. Further. you need to download an AVD or connect an Android device that has Google Play store installed and API level 29 as minimum, where you can test and install this application. In the device, you will also need to have installed Spotify application and log in using you own account.

In order to run the emulation in the SMCE, you need to select the smartcar.ino file, located in DanceCar repository and compile as a sketch. When compiling is complete press "_Start_". After this you can use yor device in order to control all functions of the DanceCar and view the show on the emulator. 

## User manual

When Set-up is complete you device will show the Drive mode as default. In this mode you can use your device as a remote driving control. At the top you can switch between Drive mode and Dance mode. Below the switch you can press "SPEED", "ANGLE" or "BRAKE" that will indicate what alternative you want to modify (speed of car, angle when turning, braking distance). In the middle you have the control of direction of the car that will mirror the settings above. At the button you select the setting of your selected mode ("SPEED", "ANGLE" or "BRAKE") where "1" indicates low speed, soft turn and long braking distance and "5" indicates high speed, hard turn and short braking distance.

When starting DanceCar the first time the Dance mode will show you the four predefined moves ("MoonWalk", "SideKick", "ShowOff" and "ChaChaCha") in a list. You can select one or many moves and the moves are stored in the order you select them which you can visually view in the bottom. After this you have the possibility to connect the selected moves to a song by pressing the Spotify logo. When you press the Spotify logo you will be presented with the current song that you are playing, the playback time as well as four new buttons (play, pause, previous and next) that is used to control the music. In this case, when you have connected the DanceCar to Spotify, the DanceCar will play music and dance at the same time when pressing "Dance".

The button "RECORD MOVE" takes you to a new screen where you can remotly drive the car and store that driving sequence as a new Move for future dance. In order to start the recording of the driving sequence press "START". A timer will appear that indicates the maximum length of you recorded driving sequence (15 seconds). When the time is up or if you press "STOP" before the 15 seconds you need to press "SAVE" in order to store this dance sequence. Make sure to give you new move a good name and click "Save" again. Now you can replay this recorded move any time. 

The button "CREATE DANCE" lets you select minimum 2 moves that will be created as one dance in the order you picked the moves. Once you have selected moves in your prefered order you press "CREATE DANCE" and give this dance a good name. Now you have a stored sequence och moves so that you do not need to select multiple moves every time. 

## Resources

**Software**

- [SMCE](https://github.com/ItJustWorksTM/smce-gd/releases)
- [SmartCar Library](https://platisd.github.io/smartcar_shield/)
- [Android Studio](https://developer.android.com/studio)
- [Arduino IDE](https://www.arduino.cc/)
- [MQTT mosquitto](https://mosquitto.org/)
- [Spotify Web API](https://developer.spotify.com/documentation/web-api/)
- [Spotify Android SDK](https://developer.spotify.com/documentation/android/quick-start/)
- [Postman, API testing](https://www.postman.com/)

## Developers

- Karl Stahre (gusstahrka@student.gu.se)
- Amanda De Souza Turquis (gusdesam@student.gu.se)
- Maja Linder (guskalmas@student.gu.se)
- Meis Salim (gussalime@student.gu.se)
- Navya Pulikandla Satyanarayanachetty (guspulna@student.gu.se)
- Malte Götharsson (gusgothama@student.gu.se)
- Bardha Ahmeti (gusahmeba@student.gu.se)
