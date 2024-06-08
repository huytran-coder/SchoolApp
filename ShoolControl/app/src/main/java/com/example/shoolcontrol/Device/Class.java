package com.example.shoolcontrol.Device;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoolcontrol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Class extends AppCompatActivity {

    SwitchCompat onlight,onfan;
    ImageView imagelight;
    EditText gettemp,geth,getm,gethouroff,getminuoff;
    Button setlight,setfan,setlightoff;
    Timer timer;

    private DatabaseReference lighstatus,fanstatus,tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        onfan=findViewById(R.id.onfan);
        onlight=findViewById(R.id.lightclasson);
        imagelight=findViewById(R.id.lightclass);
        gettemp=findViewById(R.id.fan_temp);
        geth=findViewById(R.id.hourEditText);
        getm=findViewById(R.id.minuteEditText);
        setlight=findViewById(R.id.setLightButton);
        setfan=findViewById(R.id.setFantButton);
        setlightoff=findViewById(R.id.setLightButtonOff);
        gethouroff=findViewById(R.id.houroffEditText);
        getminuoff=findViewById(R.id.minuteEditTextOFF);

        fanstatus = FirebaseDatabase.getInstance().getReference();
        lighstatus = FirebaseDatabase.getInstance().getReference();
        tmp = FirebaseDatabase.getInstance().getReference();

        lighstatus.child("LedClass").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String led = snapshot.getValue(String.class);
                if (led != null && led.equals("1") ) {
                    onlight.setChecked(true);
                } else {
                    onlight.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fanstatus.child("fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
                String fan = snapshot.getValue(String.class); // Chuyển đổi thành kiểu Long
                if (fan != null && fan.equals("1") ) {
                    onfan.setChecked(true);
                } else {
                    onfan.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        onlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imagelight.setImageResource(R.drawable.light_on);
                    lighstatus.child("LedClass").setValue("1");
                    Toast.makeText(Class.this,"Light On",Toast.LENGTH_SHORT).show();
                }
                else{
                    imagelight.setImageResource(R.drawable.light_off);
                    lighstatus.child("LedClass").setValue("0");
                    Toast.makeText(Class.this,"Light Off",Toast.LENGTH_SHORT).show();
                }
            }
        });
        onfan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    fanstatus.child("fan").setValue("1");
                    Toast.makeText(Class.this,"Fan On",Toast.LENGTH_SHORT).show();
                }
                else{
                    fanstatus.child("fan").setValue("0");
                    Toast.makeText(Class.this,"Fan Off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setlightoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hourStroff = gethouroff.getText().toString();
                String minuteStroff = getminuoff.getText().toString();

                // Kiểm tra nếu chuỗi rỗng
                if(hourStroff.isEmpty() || minuteStroff.isEmpty()) {
                    // Thông báo lỗi nếu có
                    Toast.makeText(Class.this, "Vui lòng nhập giờ và phút!", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Chuyển đổi chuỗi sang số nguyên
                int houroff = Integer.parseInt(hourStroff);
                int minuteoff = Integer.parseInt(minuteStroff);
                Toast.makeText(Class.this, "Thời gian đã chọn: " + houroff + ":" +minuteoff, Toast.LENGTH_SHORT).show();
                // Kiểm tra giờ và phút
                if (houroff < 0 || houroff > 23 || minuteoff < 0 || minuteoff > 59) {
                    // Thông báo lỗi nếu giờ hoặc phút không hợp lệ
                    Toast.makeText(Class.this, "Giờ và phút không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo một timer để kiểm tra và bật switch khi đến giờ đã đặt
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // Lấy giờ hiện tại
                        Calendar calendar = Calendar.getInstance();
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);


                        // So sánh với giờ và phút đã đặt
                        if (currentHour == houroff && currentMinute == minuteoff) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Bật switch khi đến thời gian đặt
                                    onlight.setChecked(false);
                                }
                            });
                            // Hủy bỏ timer sau khi switch được bật
                            timer.cancel();
                        }
                    }
                }, 0, 1000); // Kiểm tra mỗi giây
            }

        });
        setlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hourStr = geth.getText().toString();
                String minuteStr = getm.getText().toString();

                // Kiểm tra nếu chuỗi rỗng
                if(hourStr.isEmpty() || minuteStr.isEmpty()) {
                    // Thông báo lỗi nếu có
                    Toast.makeText(Class.this, "Vui lòng nhập giờ và phút!", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Chuyển đổi chuỗi sang số nguyên
                int hour = Integer.parseInt(hourStr);
                int minute = Integer.parseInt(minuteStr);
                Toast.makeText(Class.this, "Thời gian đã chọn: " + hour + ":" +minute, Toast.LENGTH_SHORT).show();
                // Kiểm tra giờ và phút
                if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                    // Thông báo lỗi nếu giờ hoặc phút không hợp lệ
                    Toast.makeText(Class.this, "Giờ và phút không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo một timer để kiểm tra và bật switch khi đến giờ đã đặt
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // Lấy giờ hiện tại
                        Calendar calendar = Calendar.getInstance();
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);


                        // So sánh với giờ và phút đã đặt
                        if (currentHour == hour && currentMinute == minute) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Bật switch khi đến thời gian đặt
                                   onlight.setChecked(true);
                                }
                            });
                            // Hủy bỏ timer sau khi switch được bật
                            timer.cancel();
                        }
                    }
                }, 0, 1000); // Kiểm tra mỗi giây
            }
        });



        setfan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temperatureStr = gettemp.getText().toString();

                // Kiểm tra nếu chuỗi rỗng
                if(temperatureStr.isEmpty()) {
                    // Thông báo lỗi nếu có
                    Toast.makeText(Class.this, "Vui lòng nhập nhiệt độ!", Toast.LENGTH_SHORT).show();
                    return;
                }



                float nhietdo= Float.parseFloat(temperatureStr);

                if (nhietdo < 0) {
                    // Thông báo lỗi nếu nhiệt độ âm
                    Toast.makeText(Class.this, "Nhiệt độ không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(Class.this, "Nhiệt độ đã chọn: " + nhietdo + "°C ", Toast.LENGTH_SHORT).show();
                }



                tmp.child("dht11").child("temp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Value = snapshot.getValue(String.class);
                        float currentTemperature = Float.parseFloat(Value);
                        // So sánh nhiệt độ và bật switch
                        if (currentTemperature >= nhietdo) {
                            onfan.setChecked(true);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });






    }
}