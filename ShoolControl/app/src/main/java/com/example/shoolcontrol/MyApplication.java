package com.example.shoolcontrol;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

public class MyApplication extends Application {

    public static final String CHANNEL_ID ="push_notificationID" ;

    @Override
    public void onCreate() {
        super.onCreate();
        creatChanelNotification();

    }

    private void creatChanelNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,"Pushnotification"
                    ,NotificationManager.IMPORTANCE_DEFAULT);
            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.coibao);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri,audioAttributes);

            //channel.getDescription();
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }



}
