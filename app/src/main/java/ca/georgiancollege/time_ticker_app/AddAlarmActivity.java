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
    }

    public void openAlarmListScreen(){
        Intent openAlarmListIntent = new Intent(AddAlarmActivity.this, MainActivity.class);
        EditText alarmName = (EditText) findViewById(R.id.addAlarmNameEditText);
        TimePicker alarmTime = (TimePicker) findViewById(R.id.timePicker);

        String newAlarmName = alarmName.getText().toString();
        String newAlarmTime;

        if (alarmTime.getMinute() < 10) {
            newAlarmTime = alarmTime.getHour() + ":0" + alarmTime.getMinute();
        }else {
            newAlarmTime = alarmTime.getHour() + ":" + alarmTime.getMinute();
        }

        alarms = getIntent().getStringArrayListExtra("ALARM_NAME_ARRAYLIST");
        times = getIntent().getStringArrayListExtra("ALARM_TIME_ARRAYLIST");

        alarms.add(newAlarmName);
        times.add(newAlarmTime);

        openAlarmListIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", alarms);
        openAlarmListIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", times);

        startActivity(openAlarmListIntent);
    }

}
