package com.example.shoolcontrol;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationJod extends JobService {

    private DatabaseReference fireClassRef;
    private DatabaseReference fireWCRef;

    @Override
    public boolean onStartJob(JobParameters params) {

        // Khởi tạo tham chiếu đến Realtime Database của Firebase
        fireClassRef = FirebaseDatabase.getInstance().getReference().child("FireClass");
        fireWCRef = FirebaseDatabase.getInstance().getReference().child("FireWC");

        // Lắng nghe sự thay đổi trong giá trị của FireClass
        fireClassRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Kiểm tra giá trị của FireClass khi có sự thay đổi
                String fireClassValue = dataSnapshot.getValue(String.class);
                if (fireClassValue != null && fireClassValue.equals("1")) {
                    // Gửi thông báo cháy khi FireClass có giá trị là 1
                    sendnotification("Cảnh báo cháy", "Phát hiện có nguy cơ cháy trong lớp học ",1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });

        // Lắng nghe sự thay đổi trong giá trị của FireWC
        fireWCRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra giá trị của FireWC khi có sự thay đổi
                String fireWCValue = dataSnapshot.getValue(String.class);
                if (fireWCValue != null && fireWCValue.equals("1")) {
                    // Gửi thông báo cháy khi FireWC có giá trị là 1
                    sendnotification("Cảnh báo cháy", "Phát hiện có nguy cơ cháy trong khu vực WC ",2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });


        return true;
    }
    private void sendnotification(String title, String message,int notificationId) {


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        android.app.Notification notification =new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // Set PendingIntent
                .build();

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager!=null){
            notificationManager.notify(notificationId,notification);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
