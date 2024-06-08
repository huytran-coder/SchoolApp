package com.example.shoolcontrol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationFore extends Service {
    private static final String CHANNEL_ID = "FireNotificationChannel";
    private static final int NOTIFICATION_ID = 123;


    private DatabaseReference mDatabaseClass;
    private DatabaseReference mDatabaseWC;
    @Override
    public void onCreate() {
        super.onCreate();
        mDatabaseClass = FirebaseDatabase.getInstance().getReference();
        mDatabaseWC = FirebaseDatabase.getInstance().getReference();
        startForeground(NOTIFICATION_ID, createNotification());
        listenForFireEvents();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        // Tạo notification cho Foreground Service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Foreground Service")
                .setContentText("Đang lắng nghe sự kiện từ Firebase")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.coibao);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri,audioAttributes);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        return builder.build();
    }


    private void listenForFireEvents() {
        mDatabaseClass.child("FireClass").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fireClassValue = dataSnapshot.getValue(String.class);
                if (fireClassValue != null && fireClassValue.equals("1")) {
//                    // Gọi createNotification() với thông tin về khu vực cháy
                   sendNotification("Cảnh báo cháy", "Phát hiện có nguy cơ cháy trong lớp học1",1);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });

        mDatabaseWC.child("FireWC").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fireWCValue = dataSnapshot.getValue(String.class);
                if (fireWCValue != null && fireWCValue.equals("1")) {
//                    // Gọi createNotification() với thông tin về khu vực cháy
                  sendNotification("Cảnh báo cháy", "Phát hiện có nguy cơ cháy trong WC1",2);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });
    }
    private void sendNotification(String title, String message,int notificationId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.smoke);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(icon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
