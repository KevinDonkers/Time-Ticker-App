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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextClock;

public class MainActivity extends AppCompatActivity {

    private Switch clockFormatSwitch;
    private TextClock mainClock;
    private CharSequence default24HourFormat, default12HourFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        clockFormatSwitch = (Switch) findViewById(R.id.clockFormatSwitch);
        mainClock = (TextClock) findViewById(R.id.textClock);

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
