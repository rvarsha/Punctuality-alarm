Punctuality-alarm
=================
PSU CS461/561

Welcome to the Punctuality Alarm Repository!!



Name
=================
Varsha Radhakrishnan

Project Name
=================
Punctuality Alarm

Contributors:
=================
Varsha Radhakrishnan

Contact
=================
rvarsha@pdx.edu

Description
=================
An app that periodically monitors the traffic and tells us the right time to start from home to reach the destination on time.

Legal
=================
All of the source code in this archive is licensed under the terms of the GNU General Public License as published by the Free Software Foundation version 3 except as noted under the COPYING page:
https://github.com/rvarsha/Punctuality-alarm/blob/master/COPYING

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.


Source Code
=================
All the source files can be found here
https://github.com/rvarsha/Punctuality-alarm

Pre-requisites
=================
A smart phone which uses Android v4.0 or higher.

A computer where you can build the application from the source files

A mapquest api key (see instructions below)



How to Install and use this application:
=================



4. Install Android SDK http://developer.android.com/sdk/index.html
5. Install Android Debug bridge http://developer.android.com/tools/help/adb.html
6. Begin a new Android project. Copy the src files for Punctuality Alarm from the github repository. You will also need to copy the res->values->strings.xml, AndroidManifest.xml, res->layouts->activity_traffic.xml files.
Make sure that the package name in the src files and the manifest file is "com.example.punctualityalarm"
7. The program requires a key or token to the mapquest directions api.
It is very easy to obtain this by registering on the mapquest developers network at the following link:
http://developer.mapquest.com/web/info/account/app-keys
The "Community Edition (Licensed)" provides upto 5000 api calls per day.

5. Once you have a key (say "aabbbcc1234"), change the strings.xml file like this:
From:
INSERT_YOUR_KEY_HERE
To:
aabbbcc1234

6. Build the application.

7. Find the Punctuality Alarm.apk file in the bin directory

8. Connect your phone to the laptop through a USB device
Copy the .apk file from the bin file and put it into your phone memory.

9. Log into your phone and look for Apps -> My Files.
Inside the directory, you should see your .apk file.

10. Double click the file to install the application.

11. Once you have installed, double click the application to bring up the GUI.

12. You can enter your start point and destination in the textboxes, as seen in the attached screenshot.
![alt tag](https://raw.githubusercontent.com/rvarsha/Punctuality-alarm/master/screen_shots/1.PNG)

13. Click on "Create Route"

14. If it cannot find the location, an error will pop up and you will have to find a nearby location that mapquest recognizes. Usually, it is very good at getting the location right.

15. Once the routing is done, a map will be shown on the lower half of the screen as shown in the screenshot 2.
![alt tag](https://raw.githubusercontent.com/rvarsha/Punctuality-alarm/master/screen_shots/2.PNG)

16. You can also click on "Show Itinerary" to get step by step directions and total travel time.

17. Now click on "Set date". It will bring up a datepicker widget where you can enter the date of your appointment.
![alt tag](https://raw.githubusercontent.com/rvarsha/Punctuality-alarm/master/screen_shots/3.PNG)

18. Click on "Set Time". It will bring up a timepicker widget where you can enter the time of your appointment.
![alt tag](https://raw.githubusercontent.com/rvarsha/Punctuality-alarm/master/screen_shots/4.PNG)

19. Click on "Set Alarm" to set the alarm.
20. Click on "Cancel Alarm" if you want to cancel.
21. The app will calculate your start time and send you a notification 15 minutes before your ideal start time.

Bug Reporting
=================
If you find a bug not listed under the "Bug Tracker" list below, please send me a mail at rvarsha@pdx.edu

Bug Tracker
=================
No open bugs as of now

References
=================
http://developer.android.com/guide/index.html

http://www.mapquestapi.com/

http://stackoverflow.com/


Acknowledgements
=================
* Some of the method calls used here are adopted from the samples given on the mapquestapi developers website
http://developer.mapquest.com/web/products/featured/android-maps-api/documentation
* Thanks are also due to various "stackoverflow.com" posters for their code examples.
* The android developers page for information about various api calls.
* OSS class of Summer 2014 for discussions and support
