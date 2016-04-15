package ca.georgiancollege.time_ticker_app;

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

public class AddAlarmActivity extends AppCompatActivity {

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
        String newAlarmTime = alarmTime.getHour() + ":" + alarmTime.getMinute();

        openAlarmListIntent.putExtra("ALARM_TITLE", newAlarmName);
        openAlarmListIntent.putExtra("ALARM_TIME", newAlarmTime);

        startActivity(openAlarmListIntent);
    }

}
