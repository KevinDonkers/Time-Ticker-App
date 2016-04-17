package ca.georgiancollege.time_ticker_app;

/**
 * Created by Robbie on 2016-04-15.
 */
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service {

    MediaPlayer media_song;
    Boolean isRunning = false;
    int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        this.startId = startId;
        Log.d("MyActivity", "In the Service - startID: " + startId + "Intent: " + intent);

        String state = intent.getExtras().getString("extra");

        Log.d("MyActivity", "Ringtone state: extra is " + state);

        //notifications
        NotificationManager notify_manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

        //pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

        //make the notification parameters
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("An alarm is going off")
                .setSmallIcon(R.drawable.company_logo)
                .setContentText("Click me!")
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();



        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1){
            Log.d("MyActivity", "music turn on");
            media_song = MediaPlayer.create(this, R.raw.hahaha_sound);
            media_song.start();

            //start command
            notify_manager.notify(0, notification_popup);

            this.isRunning = true;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 0){
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }
        else if(!this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 1){
            this.isRunning = false;
            this.startId = 0;
        }
        else {

        }


        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy(){
        Log.d("MyActivity", "on destroyed called");
        super.onDestroy();
        this.isRunning = false;
    }

}
