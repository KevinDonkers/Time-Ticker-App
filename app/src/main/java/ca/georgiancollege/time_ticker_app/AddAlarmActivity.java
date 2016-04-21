package ca.georgiancollege.time_ticker_app;

/**
 * Created by Kevin on 2016-04-08.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;

public class AddAlarmActivity extends AppCompatActivity {

    //declare arraylists
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

        //set on click for add button to open main activity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlarmListScreen();
            }
        });

        alarmName = (EditText) findViewById(R.id.addAlarmNameEditText);
        alarmTime = (TimePicker) findViewById(R.id.timePicker);

        //check if this is an edit of a current alarm or a new one
        if (getIntent().hasExtra("SELECTED_ALARM_NAME")) {
            //if is an edit set the alarm name edit text and the timpicker time
            alarmName.setText(getIntent().getStringExtra("SELECTED_ALARM_NAME"));
        }
        if (getIntent().hasExtra("SELECTED_ALARM_TIME")) {
            int hour, minute;
            String fullTime = getIntent().getStringExtra("SELECTED_ALARM_TIME");

            //parse the full time strinf to get the hour and minutes
            if(fullTime.length() == 5){
                hour = Integer.parseInt(fullTime.substring(0, 2));
                minute = Integer.parseInt(fullTime.substring(3, 5));
            }
            else{
                hour = Integer.parseInt(fullTime.substring(0, 1));
                minute = Integer.parseInt(fullTime.substring(2, 4));
            }

            //st the timepicker times
            //note using setCurrentHour and minute for API 21 support
            alarmTime.setCurrentHour(hour);
            alarmTime.setCurrentMinute(minute);
        }
    }

    public void openAlarmListScreen(){
        //check of the name text box is empty
        if (alarmName.getText().toString().trim().isEmpty()){
            //if the trimmed name is empty then display a dialog box telling the user to enter a title
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Please enter an Alarm Title");
            dlgAlert.setTitle("Error...");
            dlgAlert.create().show();

            //and clear any spaces if there is any
            alarmName.getText().clear();
        } else {
            //if there is a name in the text field then create an intent to retur to the main activity
            Intent openAlarmListIntent = new Intent(AddAlarmActivity.this, MainActivity.class);

            //get the new alarm name and time from the textbox and timepicker
            String newAlarmName = alarmName.getText().toString();
            String newAlarmTime;

            //add a 0 if the minutes are less than 10
            if (alarmTime.getCurrentMinute() < 10) {
                newAlarmTime = alarmTime.getCurrentHour() + ":0" + alarmTime.getCurrentMinute();
            } else {
                newAlarmTime = alarmTime.getCurrentHour() + ":" + alarmTime.getCurrentMinute();
            }

            //set the arraylists to match the extra ones on the intent
            alarms = getIntent().getStringArrayListExtra("ALARM_NAME_ARRAYLIST");
            times = getIntent().getStringArrayListExtra("ALARM_TIME_ARRAYLIST");

            //check if this is an edit
            if (getIntent().getIntExtra("POSITION", -1) >= 0) {
                //if it is an edit than change the values for that position
                alarms.set(getIntent().getIntExtra("POSITION", -1), newAlarmName);
                times.set(getIntent().getIntExtra("POSITION", -1), newAlarmTime);
            } else {
                //else add the new alarm to the end of the arraylist
                alarms.add(newAlarmName);
                times.add(newAlarmTime);
            }

            //add the arraylists as extras
            openAlarmListIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", alarms);
            openAlarmListIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", times);

            //re open the main activity
            startActivity(openAlarmListIntent);
        }
    }

}
