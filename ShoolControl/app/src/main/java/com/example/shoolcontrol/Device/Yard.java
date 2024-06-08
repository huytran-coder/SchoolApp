package com.example.shoolcontrol.Device;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shoolcontrol.GetInformation;
import com.example.shoolcontrol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Yard extends AppCompatActivity {
    SwitchCompat switchbuton;
    ImageView imageViewlight;
    private TimePicker timePicker;
    private Button setButton;
    private Button btnOFF;
    private Button btnauto;
    private TextView txtauto;
    private DatabaseReference mDatabase;
    private DatabaseReference light;
    private DatabaseReference lightstatus;
    private SharedPreferences sharedPreferences;
    private boolean automaticControlEnabled ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yard);
        // Khởi tạo DatabaseReference cho Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        light=FirebaseDatabase.getInstance().getReference();
        lightstatus=FirebaseDatabase.getInstance().getReference();
        switchbuton=findViewById(R.id.ledYard);
        imageViewlight=findViewById(R.id.imagelight);
        timePicker = findViewById(R.id.timePicker);
        setButton = findViewById(R.id.setButton);
        btnOFF=findViewById(R.id.btnOff);
        btnauto= findViewById(R.id.btnauto);
        txtauto= findViewById(R.id.txtauto);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        automaticControlEnabled = sharedPreferences.getBoolean("auto_control_enabled", false);
        if (automaticControlEnabled) {
            txtauto.setText("Automatic Control: ON");}
        else{
            txtauto.setText("Automatic Control: OFF");
        }

        btnOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();
                Toast.makeText(Yard.this, "Thời gian đã chọn: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();

                long timeDiffMillis = calculateTimeDifference(currentHour, currentMinute, selectedHour, selectedMinute);

                // Tạo một hẹn giờ để tắt đèn sau khi đủ thời gian trôi qua
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Tắt đèn sau khi đủ thời gian trôi qua
                        switchbuton.setChecked(false);
                    }
                }, timeDiffMillis);
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();
                Toast.makeText(Yard.this, "Thời gian đã chọn: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();

                long timeDiffMillis = calculateTimeDifference(currentHour, currentMinute, selectedHour, selectedMinute);

                // Tạo một hẹn giờ để bật đèn sau khi đủ thời gian trôi qua
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Bật đèn sau khi đủ thời gian trôi qua
                        switchbuton.setChecked(true);
                    }
                }, timeDiffMillis);
            }
        });
        light.child("LedYard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String led = snapshot.getValue(String.class);
                if (led != null && led.equals("1")) {
                    switchbuton.setChecked(true);
                } else {
                    switchbuton.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                automaticControlEnabled = !automaticControlEnabled;

                // Cập nhật trạng thái của button
                if (automaticControlEnabled) {
                    txtauto.setText("Automatic Control: ON");
                    Toast.makeText(Yard.this, " Bật chế độ tự động " , Toast.LENGTH_SHORT).show();
                } else {
                    txtauto.setText("Automatic Control: OFF");
                    Toast.makeText(Yard.this, " Tắt chế độ tự động " , Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("auto_control_enabled", automaticControlEnabled);
                editor.apply();
            }
        });
        lightstatus.child("Light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (automaticControlEnabled) {

                    String lightValueStr = snapshot.getValue(String.class);
                    if (lightValueStr != null) {
                        try {
                            int lightValue = Integer.parseInt(lightValueStr);

                            if (lightValue <= 100 ) {
                                switchbuton.setChecked(false);
                            } else if (lightValue > 100 ) {

                                switchbuton.setChecked(true);
                            }
                        } catch (NumberFormatException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        switchbuton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imageViewlight.setImageResource(R.drawable.light_on);
                    mDatabase.child("LedYard").setValue("1");
                    Toast.makeText(Yard.this,"Light On",Toast.LENGTH_SHORT).show();
                } else {
                    imageViewlight.setImageResource(R.drawable.light_off);
                    mDatabase.child("LedYard").setValue("0");
                    Toast.makeText(Yard.this,"Light Off",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private long calculateTimeDifference(int currentHour, int currentMinute, int selectedHour, int selectedMinute) {
        // Tính khoảng thời gian giữa hai thời điểm theo milliseconds
        Calendar currentCal = Calendar.getInstance();
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(Calendar.HOUR_OF_DAY, selectedHour);
        selectedCal.set(Calendar.MINUTE, selectedMinute);
        selectedCal.set(Calendar.SECOND, 0);

        long currentTimeMillis = currentCal.getTimeInMillis();
        long selectedTimeMillis = selectedCal.getTimeInMillis();

        long timeDiffMillis = selectedTimeMillis - currentTimeMillis;

        // Đảm bảo rằng thời gian đã chọn chưa qua, nếu đã qua thì cộng thêm 1 ngày
        if (timeDiffMillis <= 0) {
            timeDiffMillis += 24 * 60 * 60 * 1000; // Thêm 1 ngày
        }

        return timeDiffMillis;
    }
}