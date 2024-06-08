package com.example.shoolcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoolcontrol.Fragment.Profile.FragmentProfile;

public class GetInformation extends AppCompatActivity {
 private Button btnnext;
 private EditText first;
 private EditText Last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_information);
        anhxa();

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname=first.getText().toString().trim();
                String lastname=Last.getText().toString().trim();
                if(firstname.isEmpty()){
                    Toast.makeText(GetInformation.this,"Enter FirstName",Toast.LENGTH_SHORT).show();
                } else if (lastname.isEmpty()) {

                    Toast.makeText(GetInformation.this,"Enter LastName",Toast.LENGTH_SHORT).show();
                }
                else{

                    goToMainActivity();
                   saveUserData(firstname,lastname);

                }
            }
        });

    }

    private void anhxa(){
        btnnext=findViewById(R.id.btn_NextInformation);
        first=findViewById(R.id.txt_FirstName);
        Last= findViewById(R.id.txt_LastName);
    }
    private void saveUserData(String firstname, String lastname) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first", firstname);
        editor.putString("Last", lastname);
        editor.apply();
    }
    private void goToMainActivity(){
        Intent intent=  new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}