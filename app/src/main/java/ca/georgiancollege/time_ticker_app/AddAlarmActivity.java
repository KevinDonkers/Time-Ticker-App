package ca.georgiancollege.time_ticker_app;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {

    private ArrayList<String> alarms = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private EditText alarmName;
    private TimePicker alarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addButton = (Button) findViewById(R.id.addAlarmButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlarmListScreen();
            }
        });

        alarmName = (EditText) findViewById(R.id.addAlarmNameEditText);
        alarmTime = (TimePicker) findViewById(R.id.timePicker);

        if (getIntent().hasExtra("SELECTED_ALARM_NAME")) {
            alarmName.setText(getIntent().getStringExtra("SELECTED_ALARM_NAME"));
        }
        if (getIntent().hasExtra("SELECTED_ALARM_TIME")) {
            int hour, minute;
            String fullTime = getIntent().getStringExtra("SELECTED_ALARM_TIME");

            if(fullTime.length() == 5){
                hour = Integer.parseInt(fullTime.substring(0, 2));
                minute = Integer.parseInt(fullTime.substring(3, 5));
            }
            else{
                hour = Integer.parseInt(fullTime.substring(0, 1));
                minute = Integer.parseInt(fullTime.substring(2, 4));
            }
            alarmTime.setCurrentHour(hour);
            alarmTime.setCurrentMinute(minute);
        }
    }

    public void openAlarmListScreen(){
        Intent openAlarmListIntent = new Intent(AddAlarmActivity.this, MainActivity.class);

        String newAlarmName = alarmName.getText().toString();
        String newAlarmTime;

        if (alarmTime.getCurrentMinute() < 10) {
            newAlarmTime = alarmTime.getCurrentHour() + ":0" + alarmTime.getCurrentMinute();
        }else {
            newAlarmTime = alarmTime.getCurrentHour() + ":" + alarmTime.getCurrentMinute();
        }

        alarms = getIntent().getStringArrayListExtra("ALARM_NAME_ARRAYLIST");
        times = getIntent().getStringArrayListExtra("ALARM_TIME_ARRAYLIST");

        if (getIntent().getIntExtra("POSITION", -1) >= 0){
            alarms.set(getIntent().getIntExtra("POSITION", -1), newAlarmName);
            times.set(getIntent().getIntExtra("POSITION", -1), newAlarmTime);
        } else {
            alarms.add(newAlarmName);
            times.add(newAlarmTime);
        }


        openAlarmListIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", alarms);
        openAlarmListIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", times);

        startActivity(openAlarmListIntent);
    }

}
