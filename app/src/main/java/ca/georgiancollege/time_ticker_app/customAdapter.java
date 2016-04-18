package ca.georgiancollege.time_ticker_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Robbie on 2016-04-08.
 */
public class customAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<Boolean> isAlarmEnabled = new ArrayList<>();
    private Context context;

    private AlarmManager alarmManager;
    private Calendar calendar;
    private Intent my_intent;
    private PendingIntent pending_intent;

    public customAdapter(ArrayList<String> list, ArrayList<String> time, Context context) {
        this.list = list;
        this.time = time;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    public String getItemName(int pos) { return list.get(pos); }

    public String getItemTime(int pos) { return time.get(pos); }

    public Boolean getItemIsEnabled(int pos) {
        return isAlarmEnabled.get(pos);
    }

    public int getItemMinute(int pos) {
        if(time.get(pos).length() == 5){
            return Integer.parseInt(time.get(pos).substring(3, 5));
        }
        else{
            return Integer.parseInt(time.get(pos).substring(2, 4));
        }
    }

    public int getItemHour(int pos) {
        if(time.get(pos).length() == 5){
            return Integer.parseInt(time.get(pos).substring(0, 2));
        }
        else{
            return Integer.parseInt(time.get(pos).substring(0, 1));
        }
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, null);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlarmIntent = new Intent(context, AddAlarmActivity.class);
                openAlarmIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", list);
                openAlarmIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", time);

                openAlarmIntent.putExtra("POSITION", position);
                openAlarmIntent.putExtra("SELECTED_ALARM_NAME", list.get(position));
                openAlarmIntent.putExtra("SELECTED_ALARM_TIME", time.get(position));
                context.startActivity(openAlarmIntent);
            }
        });

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.alarmNameTextView);
        listItemText.setText(list.get(position));
        TextView timeItemText = (TextView)view.findViewById(R.id.alarmTimeTextView);
        timeItemText.setText(time.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                time.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });


        Switch alarmToggle = (Switch) view.findViewById(R.id.alarmActiveSwitch);

        isAlarmEnabled.add(position, alarmToggle.isChecked());

        //attach a listener to check for changes in state
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isAlarmEnabled.set(position, isChecked);

                if (isChecked) {
                    Log.d("MyActivity", "Switch is on");

                    /*alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                    String string_time = time.get(position);
                    int hour, minute;
                    if(string_time.length() == 5){
                        hour = Integer.parseInt(string_time.substring(0, 2));
                        minute = Integer.parseInt(string_time.substring(3, 5));
                    }
                    else{
                        hour = Integer.parseInt(string_time.substring(0, 1));
                        minute = Integer.parseInt(string_time.substring(2, 4));
                    }

                    calendar = Calendar.getInstance();
                    calendar.set(calendar.HOUR_OF_DAY, hour);
                    calendar.set(calendar.MINUTE, minute);
                    calendar.set(calendar.SECOND, 00);

                    my_intent = new Intent(context, AlarmReceiver.class);
                    my_intent.putExtra("extra", "alarm on");

                    pending_intent = PendingIntent.getBroadcast(context, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                    Log.d("MyActivity", "Hour: " + hour + " - Minute: " + minute + " calendar: " + calendar.getTimeInMillis());*/

                } else {
                    Log.d("MyActivity", "Switch is off");
                    /*alarmManager.cancel(pending_intent);

                    my_intent.putExtra("extra", "alarm off");

                    context.sendBroadcast(my_intent);*/
                }

            }
        });


        return view;
    }
}
