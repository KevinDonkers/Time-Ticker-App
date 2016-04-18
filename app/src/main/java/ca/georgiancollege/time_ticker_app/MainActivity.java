package ca.georgiancollege.time_ticker_app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Switch clockFormatSwitch;
    private TextClock mainClock;
    private CharSequence default24HourFormat, default12HourFormat;
    private ArrayList<String> alarms = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private static BroadcastReceiver tickReceiver;

    private ListView alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //generate list
        alarms.add("Meeting");
        alarms.add("Lunch");
        if(getIntent().hasExtra("ALARM_NAME_ARRAYLIST")) {
            alarms = getIntent().getStringArrayListExtra("ALARM_NAME_ARRAYLIST");
        }

        times.add("14:30");
        times.add("12:30");
        if(getIntent().hasExtra("ALARM_TIME_ARRAYLIST")) {
            times = getIntent().getStringArrayListExtra("ALARM_TIME_ARRAYLIST");
        }

        clockFormatSwitch = (Switch) findViewById(R.id.clockFormatSwitch);
        mainClock = (TextClock) findViewById(R.id.textClock);

        alarmList = (ListView)findViewById(R.id.alarmListView);
        //instantiate custom adapter
        final customAdapter adapter = new customAdapter(alarms, times, this);

        alarmList.setAdapter(adapter);

        //Create a broadcast receiver to handle change in time
        tickReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0)
                {

                    //check for any enabled alarms that have the current minute
                    for(int i = 0; i < adapter.getCount();i++){
                        if (adapter.getItemIsEnabled(i)){
                            Log.d("Broadcast Recieved", adapter.getItemName(i) + " is enabled");

                            if (adapter.getItemHour(i) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                                Log.d("Broadcast Recieved", adapter.getItemName(i) + " has the same Hour as system");

                                if (adapter.getItemMinute(i) == Calendar.getInstance().get(Calendar.MINUTE)){
                                    Log.d("Broadcast Recieved", adapter.getItemName(i) + " has the same Minute as system");

                                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                                    dlgAlert.setMessage("Alarm is Ringing");
                                    dlgAlert.setTitle(adapter.getItemName(i));

                                    dlgAlert.setPositiveButton("End Alarm", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                                    dlgAlert.create().show();

                                }//end of if

                            }//end of if

                        }//end of if

                    }//end of for

                }
            }
        };

        //Register the broadcast receiver to receive TIME_TICK
        registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

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
    public void onStop()
    {
        super.onStop();
        //unregister broadcast receiver.
        //if(tickReceiver!=null)
            //unregisterReceiver(tickReceiver);
    }

    public void openAlarmScreen(){
        Intent openAlarmIntent = new Intent(MainActivity.this, AddAlarmActivity.class);
        openAlarmIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", alarms);
        openAlarmIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", times);
        openAlarmIntent.putExtra("POSITION", -1);
        startActivity(openAlarmIntent);
    }
}
