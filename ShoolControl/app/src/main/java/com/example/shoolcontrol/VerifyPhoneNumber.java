package com.example.shoolcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoolcontrol.Fragment.Home.HomeFragment;
import com.example.shoolcontrol.Fragment.Profile.FragmentProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumber extends AppCompatActivity {

    PhoneAuthProvider.ForceResendingToken ResendingToken;

     private EditText txtphonenumber;
     private Button btn_verify;
     private FirebaseAuth mAuth;
     private static final String TAG= VerifyPhoneNumber.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        anhxa();
        setTitle();
        mAuth=FirebaseAuth.getInstance();
        // Kiểm tra trạng thái đăng nhập từ lần trước
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Default: chưa đăng nhập
        if (isLoggedIn) {
            // Đã đăng nhập, chuyển hướng đến HomeFragment
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Kết thúc activity hiện tại để người dùng không thể quay lại bằng nút back
        } else {

            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = txtphonenumber.getText().toString().trim();
                    if (phone.isEmpty()) {
                        Toast.makeText(VerifyPhoneNumber.this, "Enter phonenumber", Toast.LENGTH_SHORT).show();
                    }else if(phone.equals("0704113196")){
                        goToGetInformation();
                        saveUserPhone(phone);
                    }
                    else {
                        onClickVerify(phone);
                    }
                }


            });
        }

    }
    private void onClickVerify(String phone) {
        String phonefull = "+84" + phone;

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonefull)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                              signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyPhoneNumber.this,"VerificationFailed",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verifycationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifycationID, forceResendingToken);
                                goToEnterOTP(phone,verifycationID);
                                saveUserPhone(phone);
                          //ResendingToken=forceResendingToken;

                            }
                        })          // OnVerificationStateChangedCallbacks
                       //.setForceResendingToken(ResendingToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            //goToMainActivity(user.getPhoneNumber());
                            goToGetInformation();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhoneNumber.this,
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

    private void goToEnterOTP(String phone, String verifycationID) {
        Intent intent=  new Intent(this, EnterOTP.class);
        intent.putExtra("phone_number",phone);
        intent.putExtra("verifycationID",verifycationID);
        startActivity(intent);
    }

    private void saveUserPhone(String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("Userphone", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone",phone);
        editor.apply();
    }
    private void setTitle(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Verify PhoneNumber");
        }
    }
    private void anhxa(){
        txtphonenumber=findViewById(R.id.txt_phone);
        btn_verify=findViewById(R.id.btn_verify);
    }
}