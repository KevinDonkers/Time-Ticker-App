package ca.georgiancollege.time_ticker_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    private Switch clockFormatSwitch;
    private TextClock mainClock;
    private CharSequence default24HourFormat, default12HourFormat;

    private ListView alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //generate list
        ArrayList<String> alarms = new ArrayList<String>();
        alarms.add("Wake Up");
        alarms.add("Class");
        alarms.add("Meeting");
        alarms.add("Lunch");

        ArrayList<String> times = new ArrayList<String>();
        times.add("6:30");
        times.add("8:00");
        times.add("2:30");
        times.add("12:30");

        clockFormatSwitch = (Switch) findViewById(R.id.clockFormatSwitch);
        mainClock = (TextClock) findViewById(R.id.textClock);

        alarmList = (ListView)findViewById(R.id.alarmListView);


        //instantiate custom adapter
        customAdapter adapter = new customAdapter(alarms, times, this);


        alarmList.setAdapter(adapter);


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



    public void openAlarmScreen(){
        Intent openAlarmIntent = new Intent(MainActivity.this, AddAlarmActivity.class);
        //String UserName = nameEditText.getText().toString();
        //openAlarmIntent.putExtra("NAME_INFO", UserName);
        startActivity(openAlarmIntent);
    }
}
