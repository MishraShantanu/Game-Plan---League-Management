# GamePlan
#### Personal Sports Team Manager
[Zizzle Studios Website](https://sites.google.com/view/zizzlestudioscanada/home)

# Explanation of App
GamePlan is a simple way to plan and track your leagues and teams. Discover leagues throughout the world, make and join teams in any sport, schedule games with other teams within the app, and record scores ... all presented in a clear, bold interface.

KEY FEATURES
*  Find sports leagues anywhere in the world
*  Make a new league
*  Join teams within a league
*  Make your own team
*  Schedule games with other teams in your league
*  Record scores and track your wins and losses


# How to Run

## 1) Running APK from online emulator (may or may not work):
1. Download app APK from [APK folder](https://git.cs.usask.ca/test_alpha/project_1/tree/develop/APK)
2. Download [Android Online Emulator](https://chrome.google.com/webstore/detail/android-online-emulator/lnhnebkkgjmlgomfkkmkoaefbknopmja) from the chrome store 
2. Click on the extension in the top right of chrome
3. Click 'My APK Manager & APK Upload'
4. Upload app APK
5. Select Run_APK

## 2) Running APK from Android Studio:
*  For these methods, you need Android Studio installed with our source code (pull master branch from the repository)
### Method 1: Using your own Android Phone
1. You will need an Android phone and a USB cable
2. Enable USB debugging on your Android phone
   *  On your phone, go to settings -> about phone
   *  tap "Build Number" 7 times and it will then say "you are now a developer"
   *  Now develop options will be available, go to develop options and enable USB debugging
   *  You may need to install a USB driver for your Android phone (depending on your phone model and device manufacturer).
3. Plug in the phone to your computer and if a notification pops up asking to allow USB debugging, click "Yes:."
4. To run the app, click "Run" from the bar menu at the top of Android Studio. 
5. You will have to select a "deployment target" which will be your phone.

### Method 2: Using Android Studio emulator
1. First, you need to install the Android Emulator. Select "Android Emulator" componeent in the SDK Tools tab of the SDK manager
   *  To open SDK manager, click Tools -> SDK Manager
   *  Select the Android Smulator option install/update it.
2. Create an Android Virtual Device (AVD) that the emulator can use to install the app
   *  In the toolbar, click on the box to the left of the "Run" button and open the "AVD Manager"
   *  Click on "Create Virtual Device"
   *  Select "Pizel 3 XL" and press Next
   *  Select API Level "28" and press Next
   *  Press "Finish" which will download the phone and OS
3. After download has completed, select the "Pixel 3 XL" from the toolbar AVD manager bar to the left of the "Run" button
4. Press "Run" which will load the emulator and start the app (might take a few minutes initially)

## 3) Nox(Android Emulator for PC) (May or may not work)
1. Download [Nox](https://www.bignox.com/en/download/fullPackage) using any browser
2. Install Nox using the downloaded exe file
2. Download latest app APK from [APK folder](https://git.cs.usask.ca/test_alpha/project_1/tree/develop/APK)
3. Open Nox application 
4. Drag the Downloaded APK over Nox emulator screen(It will automatically install the APK)
5. You will get a "GamePlan" application logo on the Nox main and applications screen, Click on that to run the app
