package com.example.shoolcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOTP extends AppCompatActivity {
    //private EditText txtOTP;
    private Button btn_OTP;
    private TextView nototp;
    private String mphonenumber;
    private String mphonenumberagain;
    private  String mverifycationID;
    private EditText code1,code2,code3,code4,code5,code6;
    public static final String TAG=EnterOTP.class.getName();
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mForcwResendingToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        anhxa();
        setTitle();
        getdataIntent();
        setupInput();
        mAuth= FirebaseAuth.getInstance();

        TextView textmobilenumber=findViewById(R.id.textmobile);
        textmobilenumber.setText(String.format(
                "+84-%s",getIntent().getStringExtra("phone_number")));
        btn_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lấy text OTP
                if (code1.getText().toString().trim().isEmpty() || code2.getText().toString().trim().isEmpty()
                        || code3.getText().toString().trim().isEmpty() || code4.getText().toString().trim().isEmpty()
                        || code5.getText().toString().trim().isEmpty() || code6.getText().toString().trim().isEmpty()) {
                    // Hiển thị thông báo Toast nếu một trong các ô nhập liệu trống
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                }
                else{
                    String Code=code1.getText().toString()+
                            code2.getText().toString()+
                            code3.getText().toString()+
                            code4.getText().toString()+
                            code5.getText().toString()+
                            code6.getText().toString();
                    onclicksendOTP(Code);
                }

            }
        });

        nototp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oclicksenOTPagian();
            }
        });
    }


     private void getdataIntent(){
        mphonenumber=getIntent().getStringExtra(("phone_number"));
        mverifycationID=getIntent().getStringExtra("verifycationID");
     }

    private void setTitle(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Enter OTP");
        }
    }
    private void anhxa(){
        nototp=findViewById(R.id.notOTP);
       // txtOTP=findViewById(R.id.txt_otp);
        btn_OTP=findViewById(R.id.btn_otp);
        code1=findViewById(R.id.code1);
        code2=findViewById(R.id.code2);
        code3=findViewById(R.id.code3);
        code4=findViewById(R.id.code4);
        code5=findViewById(R.id.code5);
        code6=findViewById(R.id.code6);

    }
    private void setupInput(){
        anhxa();
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!s.toString().trim().isEmpty()){
                 code2.requestFocus();
             }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            //FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            //goToMainActivity(user.getPhoneNumber());
                            goToGetInformation();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true); // Đã đăng nhập
                            editor.apply();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(EnterOTP.this,
                                        "The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void goToGetInformation() {//String phoneNumber
        Intent intent=  new Intent(this, GetInformation.class);
       // intent.putExtra("phone_number",phoneNumber);
        startActivity(intent);
    }
    private void onclicksendOTP(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverifycationID,Code);
        signInWithPhoneAuthCredential(credential);
    }
    private void oclicksenOTPagian() {
        mphonenumberagain= "+84"+mphonenumber;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mphonenumberagain)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setForceResendingToken(mForcwResendingToken)
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOTP.this,"VerificationFailed",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verifycationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifycationID, forceResendingToken);
                                mverifycationID=verifycationID;
                                mForcwResendingToken = forceResendingToken;

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}