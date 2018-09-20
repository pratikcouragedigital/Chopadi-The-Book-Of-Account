package com.mobitechs.chopadi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("LOB","onReceive");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            wasScreenOn = false;
           // Toast.makeText(context, "Screen Off", Toast.LENGTH_SHORT).show();
            Log.e("LOB","wasScreenOn"+wasScreenOn);
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;
            //Toast.makeText(context, "Screen On", Toast.LENGTH_SHORT).show();

        }
        else if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            // and do whatever you need to do here
            wasScreenOn = true;
            Toast.makeText(context, "Sys Close", Toast.LENGTH_SHORT).show();

        }

        else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.e("LOB","userpresent");
            Log.e("LOB","wasScreenOn"+wasScreenOn);
           // String url = "http://www.stackoverflow.com";
            //Toast.makeText(context, "User Present", Toast.LENGTH_SHORT).show();

            //redirecting to page
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setData(Uri.parse(url));
//            context.startActivity(i);
        }
    }
}
