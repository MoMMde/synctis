# Synctis
Syncronize your Untis Calendar into your Google Calendar.
Dockerization is subject to future.  

## Configuration
Each Instance can only provide synchronization for one Calendar.  
Configuration is done via. Environment Variables.  
Take into Account that every time references to the Time Zone of the Calendar.  

**RUN_SCHEDULE**: When an Synchronization takes place. Written in an Crontab Way. See https://crontab.guru to build your Own. Defaults to `"0 5 1-31 * *"`, which means that it runs every day at 5 in the morning. (Optional)  

**DAYS_IN_FUTURE**: How many days in the future the calendar synchroniyes. So when `RUN_SCHEDULE` invokes, all events from today until today + `DAYS_IN_FUTURE` will be copied over. Defaults to `14`. (Optional)     

**USER_AGENT**: How the Http Client will present itself. (Optional)  

**DEBUG**: Defaults to false, will just print extra messages. (Optional)  

**PORT**: On which port the Google Authorization Flow will take place. Defaults to `8080`. (Optional)  

**HOST**: On which Host the Google Authorization Flow will listen. Defaults to `0.0.0.0`. (Optional)  

**GOOGLE_AUTH_FILE**: Which file stores the Google Credentials. This will be written first on Google Authorization and auto. updates when the Token Expires. For mor Info see [Googles OAuth2 Flow](https://developers.google.com/identity/openid-connect/openid-connect). Defaults to `.google-auth` (Optional)

**GOOGLE_CALENDAR_ID**: On which Calendar the events will be written. This ID can be obtained via [GCalendar](calendar.google.com). (Required)  

**GOOGLE_DEFAULT_EVENT_TYPE**: How you will be presented in Google. Defaults to `FOCUSTIME`. See [GoogleCalendarEventType](https://github.com/MoMMde/synctis/blob/development/src/main/kotlin/xyz/mommde/synctis/google/implementation/objects/GoogleCalendarEventType.kt). (Optional)  

**UNTIS_SERVER**: The Server your School uses. (Required)  

**UNTIS_USERNAME**: Your Username. (Required)  

**UNTIS_PASSWORD**: Your Password. (Required)  

**UNTIS_SCHOOL**: Your School ID. (Required)  

**UNTIS_SCHOOL_LOCATION**: Can be used to give more details in the `Location` Field provided by the Google API. (Optional)  

## Google Calendar
To be Documented
