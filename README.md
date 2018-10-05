#Music-Service-Client-Application

The first app, named Music Service stores a number of audio clips,
such as songs or other recordings. The clips are numbered 1 through n, where n is the total number of clips.
The app contains a service intended to be bound (as opposed to started), which exposes an API for clients
to use. The API supports such functionality as playing one of the audio clips, pausing the clip, resuming
the clip and stopping the playing of the clip altogether. The application should include at least 3 audio clips
of variable duration. However, the duration of Clip #1 should be at least 30 seconds but no more than 3
minutes.
The second app, Music Client consists of an activity that exposes functionality for using the Music Service
and binds to the service for playing desired audio clips. Your interface should mininally include appropriate
View elements for the following functionality: (1) Playing a given clip (by number), (2) Pausing the
playback, (3) Resuming the playback, and (4) Stopping the player. When the client activity is stopped,
the service should continue playing; however, the service should be unbound and stopped if the activity is
destroyed.
In addition the Music Client app keeps track of all requests that an interactive user made (e.g., play
Clip 1, pause Clip 1, stop player, etc.). The requests are displayed in a scrollable list view maintained by the
app.

Notes:
For the Music Service to run in Android Studio please go to Run -> Launch Options -> Nothing
The Music service does not have an activity to open its purpose is to run in the background
