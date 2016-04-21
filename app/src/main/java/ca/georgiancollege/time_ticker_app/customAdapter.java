package ca.georgiancollege.time_ticker_app;

/**
 * Created by Robbie on 2016-04-08.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;


public class customAdapter extends BaseAdapter implements ListAdapter {

    //declare arraylists and context
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<Boolean> isAlarmEnabled = new ArrayList<>();
    private Context context;

    //constructor
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

    //returns the minute of the item at the position
    public int getItemMinute(int pos) {
        if(time.get(pos).length() == 5){
            return Integer.parseInt(time.get(pos).substring(3, 5));
        }
        else{
            return Integer.parseInt(time.get(pos).substring(2, 4));
        }
    }

    //returns the minute of the item at the position
    public int getItemHour(int pos) {
        if(time.get(pos).length() == 5){
            return Integer.parseInt(time.get(pos).substring(0, 2));
        }
        else{
            return Integer.parseInt(time.get(pos).substring(0, 1));
        }
    }

    @Override
    public long getItemId(int pos) { return 0; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, null);
        }

        //set on on click listener for the list items
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when the list item is clicked create the intent to go to teh add alarm page
                Intent openAlarmIntent = new Intent(context, AddAlarmActivity.class);

                //add alarm name arraylist and the alarm time arrylist to the intent
                openAlarmIntent.putStringArrayListExtra("ALARM_NAME_ARRAYLIST", list);
                openAlarmIntent.putStringArrayListExtra("ALARM_TIME_ARRAYLIST", time);

                //pass the position of the item in the array as an extra
                openAlarmIntent.putExtra("POSITION", position);

                //add the selected alarm name and time as extras
                openAlarmIntent.putExtra("SELECTED_ALARM_NAME", list.get(position));
                openAlarmIntent.putExtra("SELECTED_ALARM_TIME", time.get(position));

                //start the activity
                context.startActivity(openAlarmIntent);
            }
        });

        //populate the list items
        TextView listItemText = (TextView)view.findViewById(R.id.alarmNameTextView);
        listItemText.setText(list.get(position));
        TextView timeItemText = (TextView)view.findViewById(R.id.alarmTimeTextView);
        timeItemText.setText(time.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);

        //set delete button on click listener
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove the name and time from the arraylists
                list.remove(position);
                time.remove(position);

                //update the listview
                notifyDataSetChanged();
            }
        });


        Switch alarmToggle = (Switch) view.findViewById(R.id.alarmActiveSwitch);

        //add the switch status to the switch arraylist
        isAlarmEnabled.add(position, alarmToggle.isChecked());

        //attach a listener to check for changes in state
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //update the switch status in the arraylist when it is clicked
                isAlarmEnabled.set(position, isChecked);
            }
        });


        return view;
    }
}
