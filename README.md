# CUCM force logouter #
* The application scans log file folder, for each file starts a new thread - file listener and for each shared line message constructs and sends through CUCM API logout message for each isSharedLine = true phone and also sends login (for specified in properties tech end user) message to to kick out previous user.
* Each logout message can be send for each phone not earlier than specified amount of time.

## Requirements ##
* JRE 1.8

## Compilation ##
* Download source files
* Open folder with project in cmd / bash
* mvn clean install
* Find compiled files in you local Maven repository

## Usage ##
* Download or compile the latest release of the application
* Extract zip archive in needed place
* Configure settings in etc/config.properties. See detailed comments in the file. Speficy at least CUCM URL, login, password and log path of Verint RIS log directory
* "javaw.exe -Dapp.home=%pathApplicationDirectory% -jar %pathApplicationDirectory% \cucmforcelogouter-1.1-SNAPSHOT.jar" where %pathApplicationDirectory% is your path to the application directory

## Release notes ##
### 1.1 ###
* Added login for tech end user behavior
* Added time restriction for sending logouts - each logout message can be send for each phone not earlier than specified amount of time.
#### 1.0 ####
* Release.