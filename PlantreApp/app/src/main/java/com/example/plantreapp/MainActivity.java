

package com.example.plantreapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plantreapp.db.AppDatabase;
import com.example.plantreapp.login.LoginActivity;
import com.example.plantreapp.onBoarding.OnBoardingActivity;

/*Splash Screen*/

public class MainActivity extends AppCompatActivity {
    private int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //AppDatabase.Companion.invoke(getApplicationContext());

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(!isTimerServiceRunning()) {
                Intent timerIntent = new Intent(this, TimerService.class);
                startForegroundService(timerIntent);
            }
            CharSequence name = "waterPlantChannel";
            String description = "Channel for watering the plants";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("waterPlantChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }*/

        // hide actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // run the splash screen for 'SPLASH_TIME' milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }


    /*public Boolean isTimerServiceRunning(){
        @SuppressLint("ServiceCast") ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(TimerService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }*/
    //this is test
}
