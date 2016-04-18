package ca.georgiancollege.time_ticker_app;

/**
 * Created by Robbie on 2016-04-15.
 *old method of adding an alarm, currently not in use
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MyActivity", "In the receiver");

        String get_your_string = intent.getExtras().getString("extra");

        Log.d("MyActivity", "String Key: " + get_your_string);

        Intent service_intent = new Intent(context, AlarmService.class);

        service_intent.putExtra("extra", get_your_string);

        context.startService(service_intent);
    }
}
