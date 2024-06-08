package com.example.shoolcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class control_tinnhan extends AppCompatActivity {

    private DatabaseReference UID;
   ImageView delete;
    TextView textID;
    String lastFirebaseValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_tinnhan);
        UID = FirebaseDatabase.getInstance().getReference();
        delete= findViewById(R.id.btnDelete);
        textID= findViewById(R.id.textID);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Đọc dữ liệu từ SharedPreferences
        String savedText = sharedPreferences.getString("textViewContent", "");

        textID.setText(savedText);



        UID.child("UID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Value = snapshot.getValue(String.class);


                if (!Value.equals(lastFirebaseValue)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = dateFormat.format(System.currentTimeMillis());

                    // Kết hợp giá trị và thời gian
                    String displayText = "\n" + Value + "          " + currentTime;

                    // Cập nhật TextView
                    textID.append(displayText);

                    // Cập nhật biến trạng thái cuối cùng từ Firebase
                    lastFirebaseValue = Value;

                    // Lưu trạng thái hiện tại vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("textViewContent", lastFirebaseValue);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textID.setText("");

                // Xóa lịch sử đã lưu trong SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("lastFirebaseValue");
                editor.apply();
            }
        });
    }
}