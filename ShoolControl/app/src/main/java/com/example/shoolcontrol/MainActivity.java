package com.example.shoolcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.example.shoolcontrol.Fragment.Home.HomeFragment;
import com.example.shoolcontrol.Fragment.Device.Device_fragment;
import com.example.shoolcontrol.Fragment.Profile.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView menu;
    HomeFragment homeFragment;
    FragmentProfile fragmentProfile;                                                                                  
    Device_fragment deviceFragment;
    private DatabaseReference fireClassRef;
    private DatabaseReference fireWCRef;
    private static final int JOB_ID = 1001;
    private static final long NOTIFICATION_JOB_INTERVAL = 15* 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, NotificationFore.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        scheduleNotificationJob();

        menu = findViewById(R.id.menu);

        homeFragment = new HomeFragment();
        fragmentProfile = new FragmentProfile();
        deviceFragment= new Device_fragment();
//        fireClassRef = FirebaseDatabase.getInstance().getReference().child("FireClass");
//       fireWCRef = FirebaseDatabase.getInstance().getReference().child("FireWC");

        menu.setOnItemSelectedListener(v->{
            if(v.getItemId() == R.id.home){
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, homeFragment).commit();
            }

            if(v.getItemId() == R.id.profile){
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, fragmentProfile).commit();
            }

            if(v.getItemId() == R.id.device){
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, deviceFragment).commit();
            }
            return true;
        });

        menu.setSelectedItemId(R.id.home);


    }
    private void scheduleNotificationJob() {
        ComponentName componentName = new ComponentName(this,NotificationJod.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
       // builder.setPeriodic(NOTIFICATION_JOB_INTERVAL); // Đặt thời gian lặp lại công việc
        builder.setMinimumLatency(1000); // Đặt thời gian tối thiểu trước khi kích hoạt công việc là 1 giây (1000 miligiây)
        builder.setOverrideDeadline(5000); // Đặt thời hạn cuối cùng trước khi hủy bỏ công việc là 5 giây (5000 miligiây)
        builder.setBackoffCriteria(0, JobInfo.BACKOFF_POLICY_LINEAR); // Đặt chiến lược backoff
        builder.setPersisted(true); // Lưu trữ công việc sau khi khởi động lại điện thoại
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // Yêu cầu có kết nối mạng
        builder.setRequiresCharging(false); // Không yêu cầu điện thoại đang sạc
        builder.setRequiresDeviceIdle(false); // Không yêu cầu thiết bị đang không hoạt động

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        }
    }


}