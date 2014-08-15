Punctuality-alarm
=================
PSU CS461/561

Welcome to the Punctuality Alarm Repository!!

All of the source code in this archive is licensed under the terms of the GNU General Public License as published by the Free Software Foundation version 3 except as noted under LICENSE INFORMATION.txt





How to Install and use this application:


1. Install Android SDK
http://developer.android.com/sdk/index.html
2. Install Android Debug bridge
http://developer.android.com/tools/help/adb.html
3. Copy the src files for Punctuality Alarm from the github repository
You will need:

strings.xml
android_manifest.xml
activity_traffic.xml
3. The program requires a key or token to the mapquest directions api.
It is very easy to obtain this by registering on the mapquest developers network at the following link:
http://developer.mapquest.com/web/info/account/app-keys
The "Community Edition (Licensed)" provides upto 5000 api calls per day.
4. Once you have a key (say "aabbbcc1234"), change the strings.xml file like this:
From:
<my_key>"INSERT_YOUR_KEY_HERE"</my_key>
To:
<my_key>"aabbbcc1234"</my_key>
5. Build the application.
6. Find the Punctuality Alarm.apk file in the bin directory
7. Connect your phone to the laptop through a USB device
Copy the .apk file from the bin file and put it into your phone memory.
8. Log into your phone and look for Apps -> My Files.
Inside the directory, you should see your .apk file.
9. Double click the file to install the application.
10. Once you have installed, double click the application to bring up the GUI.
11. You can enter your start point and destination in the textboxes, as seen in the attached screenshot.
12. Click on "Create Route"
13. If it cannot find the location, an error will pop up and you will have to find a nearby location that mapquest recognizes. Usually, it is very good at getting the location right.
14. Once the routing is done, a map will be shown on the lower half of the screen as shown in the screenshot 2.
15. You can also click on "Show Itinerary" to get step by step directions and total travel time.
16. Now click on "Set date". It will bring up a datepicker widget where you can enter the date of your appointment.
17. Click on "Set Time". It will bring up a timepicker widget where you can enter the time of your appointment.
18. Click on "Set Alarm" to set the alarm.
19. Click on "Cancel Alarm" if you want to cancel.
20. The app will calculate your start time and send you a notification 15 minutes before your ideal start time.
