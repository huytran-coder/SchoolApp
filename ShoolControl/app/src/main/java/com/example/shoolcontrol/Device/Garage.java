package com.example.shoolcontrol.Device;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Garage extends AppCompatActivity {
    ImageView Servo_on,Servo_off,light;
    SwitchCompat lighton;
    EditText textID;
    private DatabaseReference servostatus;
    private DatabaseReference lighttatus;
    private DatabaseReference UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Servo_off=findViewById(R.id.servo_off);
        Servo_on=findViewById(R.id.servo_on);
        lighton=findViewById(R.id.onoff);
        textID=findViewById(R.id.textID);
        light=findViewById(R.id.lightgara);
        servostatus = FirebaseDatabase.getInstance().getReference();
       lighttatus = FirebaseDatabase.getInstance().getReference();
        UID = FirebaseDatabase.getInstance().getReference();


        Servo_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servostatus.child("servo").setValue(0);
                Toast.makeText(Garage.this, "Đóng thanh chắn " , Toast.LENGTH_SHORT).show();

            }
        });
        Servo_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servostatus.child("servo").setValue("1");
                Toast.makeText(Garage.this, "Mở thanh chắn " , Toast.LENGTH_SHORT).show();

            }
        });

        lighton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    light.setImageResource(R.drawable.light_on);
                    lighttatus.child("LedGara").setValue("1");
                    Toast.makeText(Garage.this,"Light On",Toast.LENGTH_SHORT).show();
                }
                else{
                    light.setImageResource(R.drawable.light_off);
                    lighttatus.child("LedGara").setValue("0");
                    Toast.makeText(Garage.this,"Light Off",Toast.LENGTH_SHORT).show();
                }
            }
        });


        lighttatus.child("LedGara").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String led = snapshot.getValue(String.class);
                if (led != null && led.equals("1")) {
                    lighton.setChecked(true);
                } else {
                    lighton.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UID.child("UID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Value = snapshot.getValue(String.class);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String currentTime = dateFormat.format(System.currentTimeMillis());

                // Kết hợp giá trị và thời gian
                String displayText = "\n"+Value + " - " + currentTime;

                textID.setText(displayText);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}