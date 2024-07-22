# Tutorial

## Setup backend

* Run server cloned from ```https://github.com/hau564/MyAPI```
* In gradle.properties, change ```100.100.1.1``` in ```API_BASE_URL```  to your ```Ipv4 address```
* Clean and rebuild project, make sure it was built correctly by checking ```API_BASE_URL``` in ```app/build/generated/debug/.../BuildConfig.kt```.
* Run app

Notes: 
* See example usages in ```MainActivity.kt```.
* CHECK THE ```API_BASE_URL``` MULTIPLE TIMES (print it), errors mostly occur since the app was built incorrectly (somehow).
* If the request was sent correctly, the signal should be logged by the server. Open the server to check (where you run ```npm run dev```).
* See changes (if the response is ok) on mongodb.com (Database -> Collections).
* To get your Ipv4, in cmd: ```ipconfig```