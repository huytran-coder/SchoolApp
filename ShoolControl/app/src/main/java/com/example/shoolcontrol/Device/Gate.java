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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoolcontrol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Gate extends AppCompatActivity {
     SwitchCompat ongate;
     TextView gatestatus;
    private DatabaseReference datagate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);
        ongate=findViewById(R.id.gate);
        gatestatus=findViewById(R.id.gatestatus);
        datagate=FirebaseDatabase.getInstance().getReference();


        ongate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    datagate.child("gate").setValue("1");
                    Toast.makeText(Gate.this,"Gate On",Toast.LENGTH_SHORT).show();
                } else {

                    datagate.child("gate").setValue("0");
                    Toast.makeText(Gate.this,"Gate Off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        datagate.child("gate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//

                String gate = snapshot.getValue(String.class);
                    if (gate != null && gate.equals("1")) {
                        ongate.setChecked(true);
                        gatestatus.setText(" Cổng đang mở");
                    } else {
                        ongate.setChecked(false);
                        gatestatus.setText(" Cổng đang đóng");
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}