package ca.georgiancollege.time_ticker_app;

//Edited by Kevin Donkers on April 21, 2016

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextClock;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //declare private local variables
    private Switch clockFormatSwitch;
    private TextClock mainClock;
    private CharSequence default24HourFormat, default12HourFormat;
    private ArrayList<String> alarms = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private static BroadcastReceiver tickReceiver;
    private MediaPlayer media_song;

    private ListView alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //instantiate the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //instantiate the Notification manager to create a notification
        final NotificationManager notify_manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //make the notification parameters
        final Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("An alarm is going off")
                .setSmallIcon(R.drawable.company_logo)
                .setContentText("Click me!")
                .setAutoCancel(true)
                .build();

        //generate list
        alarms.add("Meeting");
        alarms.add("Lunch");
        //check if there is arraylist extras
        if(getIntent().hasExtra("ALARM_NAME_ARRAYLIST")) {
            //if there is that means they were coming from the add alarm page so set the listview to the passed arraylists
            alarms = getIntent().getStringArrayListExtra("ALARM_NAME_ARRAYLIST");
        }

        times.add("14:30");
        times.add("12:30");
        if(getIntent().hasExtra("ALARM_TIME_ARRAYLIST")) {
            times = getIntent().getStringArrayListExtra("ALARM_TIME_ARRAYLIST");
        }

        //get the clock format switch and main clock components
        clockFormatSwitch = (Switch) findViewById(R.id.clockFormatSwitch);
        mainClock = (TextClock) findViewById(R.id.textClock);

        //get the listview by id
        alarmList = (ListView)findViewById(R.id.alarmListView);

        //instantiate custom adapter
        final customAdapter adapter = new customAdapter(alarms, times, this);

        //set the custom adapter to the listview
        alarmList.setAdapter(adapter);

        //Create a broadcast receiver to handle change in time
        tickReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0)
                {
                    //this event gets fired by the android system every time the minute changes on the system clock
                    //check for any enabled alarms that have the current minute
                    for(int i = 0; i < adapter.getCount();i++){
                        if (adapter.getItemIsEnabled(i)){
                            if (adapter.getItemHour(i) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY) && adapter.getItemMinute(i) == Calendar.getInstance().get(Calendar.MINUTE)){

                                //instantiate the media player and start playing a looping sound
                                media_song = MediaPlayer.create(MainActivity.this, R.raw.hahaha_sound);
                                media_song.setLooping(true);
                                media_song.start();

                                //create a notification in the top bar of the phone
                                notify_manager.notify(0, notification_popup);

                                //create an alert modal that tells the user which alarm is ringing and and lets them end the alarm by clicking a button on the modal
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                                dlgAlert.setMessage("Alarm is Ringing");
                                dlgAlert.setTitle(adapter.getItemName(i));

                                //set the click handler for the end alarm
                                dlgAlert.setPositiveButton("End Alarm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //stop playing the sound and remove the notification
                                        media_song.stop();
                                        notify_manager.cancel(0);
                                    }
                                });

                                //show the alert modal
                                dlgAlert.create().show();

                            }//end of if

                        }//end of if

                    }//end of for

                }
            }
        };

        //Register the broadcast receiver to receive TIME_TICK
        registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        //get the default time formats for 12 and 24 hour clocks
        default12HourFormat = mainClock.getFormat12Hour();
        default24HourFormat = mainClock.getFormat24Hour();

        //attach a listener to check for changes in state
        clockFormatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                //if the switch is activated set the clock to 24 hour mode
                if(isChecked){
                    mainClock.setFormat24Hour(default24HourFormat);
                    mainClock.setFormat12Hour(null);
                }else{
                    mainClock.setFormat12Hour(default12HourFormat);
                    mainClock.setFormat24Hour(null);
                }

            }
        });

        //set a click event to open the add alarm screen when the fab is clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlarmScreen();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onStop() { super.onStop(); }

    public void openAlarmScreen(){
        //crete the intent to open the add alarm activity
        Intent openAlarmIntent = new Intent(MainActivity.this, AddAlarmActivity.class);

        //send the alarms and times arraylist as extras to the add alarm page
        openAlarmIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", alarms);
        openAlarmIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", times);

        //set the position extra to negative to show that we are adding a new alarm not editing one
        openAlarmIntent.putExtra("POSITION", -1);

        //start the add alarm activity
        startActivity(openAlarmIntent);
    }
}
