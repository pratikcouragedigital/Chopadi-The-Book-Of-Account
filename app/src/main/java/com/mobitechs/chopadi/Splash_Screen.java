package com.mobitechs.chopadi;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.os.Handler;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;
import java.io.File;

public class Splash_Screen extends AppCompatActivity {

    TextView txtZoomIn,txt2_zoom_in;
    Animation animZoomIn;
    private static int SPLASH_TIME_OUT = 2500;
    private DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        File exportDir = new File(Environment.getExternalStorageDirectory()+"/Chopadi/backup");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        mydb = new DataBaseHelper(this);
        //mydb.Bill_Clear();

        txtZoomIn=(TextView)findViewById(R.id.txt_zoom_in);
        txt2_zoom_in=(TextView)findViewById(R.id.txt2_zoom_in);

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);

        txtZoomIn.setVisibility(View.VISIBLE);
        txt2_zoom_in.setVisibility(View.VISIBLE);
        txtZoomIn.startAnimation(animZoomIn);
        txt2_zoom_in.startAnimation(animZoomIn);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent gotoHome =  new Intent(Splash_Screen.this,MainActivity.class);
//                Intent gotoHome =  new Intent(Splash_Screen.this,Auth.class);
                startActivity(gotoHome);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
