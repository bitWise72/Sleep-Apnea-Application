# Serene Sleep
## Introduction:
*Serene Sleep* is an all-in-one sleep apnea application aimed at alleviating the distress of people suffering from sleep related anomalies, by the dint of the Google Health Connect+ Application, Google Analytics and Drowsiness and Snoring related data analysis to provide apt sleep and lifestyle related fitness advice and recommendations.  

Our comprehensive sleep apnea assistance platform utilizes the Health Connect API to incorporate vital sleep metrics, including heart rates, REM sleep, and blood pressure variations from preferred third-party apps. Leveraging lifestyle data, such as smoking habits, passive smoke exposure, and exercise routines, we assess the likelihood of sleep apnea. The platform features an in-built drowsiness detection tool for safety in tasks requiring alertness, a snoring detection tool using machine learning, and personalized lifestyle recommendations, enforced by regular alarms and updates.
## Steps to compile and run the application:
Prerequirements :
* Install the [Android Studio](https://developer.android.com/studio/index.html)
* Android 6.0 (API level 23) or higher is required.
* Google Services Plugin (version 4.4.1) is required.
* Install [Health Connect](https://developer.android.com/codelabs/health-connect#1) and create your profile with necessary details.

Compile and run the project:
* Clone this repository with command: 
`git clone https://github.com/Zaid-0504/Sleep-Apnea-Application.git`
* Import the project into the Android Studio
* Run the project from Android Studio


## How it works?
Our app analyses whether a person is suffering from Sleep Apnea disease or not. It takes necessary information regarding the users health and analyses and detects the necessary factors and then provides valuable lifestyle recommendations which should be followed to cope up with the disease.

To use the app, one needs to follow the steps as mentioned -
* Sign in with your email and password or directly using a google account.
* Allow the app to access your Health-Connect account (if not present, create an account first). Our app will collect your data regarding necessary factors of the Sleep Apnea disease and analyze those.
* Take the Lifestyle Evaluation Test and answer the questions accordingly. This data is also necessary to check your health condition.
* Also it's suggested to use the Snoring Detection model while sleeping.
* Our app will check all these factors and give you some charts describing your health conditions and whether you are suffering from Sleep Apnea or not.
* Go to the Diet and Lifestyle Recommendation Page to check what steps are to be taken to cope up with the disease.
* Also for people facing day-time drowsiness, use the Drowsiness Detector module in our app which helps in alarming a person when he is drowsy and operating some heavy machines.


## About our Machine Learning Models
We have made two machine learning models- one for Snoring Detection and one for Drowsiness Detection. 
* The Snoring Detection tracks the frequency of snoring and the time duration of snoring which helps in evaluating whether the person is facing extreme problem while breathing at night or not.
* The Drowsiness Detector uses real-time face detection to check users eye movements.In this model we have used YOLO v5 model. The user can integrate it in their camera very easily and use it while driving or doing some other heavy work. It generates an alarm when someone is feeling extreme drowsy to alert the person and prevent accident. 
