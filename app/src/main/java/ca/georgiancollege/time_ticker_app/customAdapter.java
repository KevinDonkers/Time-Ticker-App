package ca.georgiancollege.time_ticker_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Robbie on 2016-04-08.
 */
public class customAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private Context context;



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

    @Override
    public long getItemId(int pos) {
        return 69;
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

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.alarmNameTextView);
        listItemText.setText(list.get(position));
        TextView timeItemText = (TextView)view.findViewById(R.id.alarmTimeTextView);
        timeItemText.setText(time.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                time.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });


        return view;
    }
}
