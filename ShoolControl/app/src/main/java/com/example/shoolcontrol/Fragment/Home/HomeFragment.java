package com.example.shoolcontrol.Fragment.Home;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoolcontrol.Device.Class;
import com.example.shoolcontrol.Device.Garage;
import com.example.shoolcontrol.Device.Gate;
import com.example.shoolcontrol.Device.Yard;
import com.example.shoolcontrol.MyApplication;
import com.example.shoolcontrol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    TextView hour,minutes,time ,date,temp,humi;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseDatabase datamic = FirebaseDatabase.getInstance();
    private DatabaseReference fireClassRef;
    private DatabaseReference fireWCRef;
    TextView txthello;
    ImageView mic;

    private ActivityResultLauncher<Intent> speechRecognitionLauncher;



    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root;
        root=inflater.inflate(R.layout.fragment_home, container, false);


        hour=root.findViewById(R.id.hour);
        minutes=root.findViewById(R.id.minutes);
        time=root.findViewById(R.id.time);
        date=root.findViewById(R.id.date);
        temp=root.findViewById(R.id.temp);
        humi=root.findViewById(R.id.humi);
        txthello= root.findViewById(R.id.text_Hello);
        mic= root.findViewById(R.id.btnmic);

//        // Khởi tạo tham chiếu đến Realtime Database của Firebase
//        fireClassRef = FirebaseDatabase.getInstance().getReference().child("FireClass");
//        fireWCRef = FirebaseDatabase.getInstance().getReference().child("FireWC");
        Calendar calendar= Calendar.getInstance();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString("first", "");
        String lastName = sharedPreferences.getString("Last", "");
        txthello.setText("Xin chào "+firstName + " " + lastName+", tôi có thể giúp gì cho bạn ?? ");

        int gio=calendar.get(Calendar.HOUR_OF_DAY);
        int phut=calendar.get(Calendar.MINUTE);
        int buoi=calendar.get(Calendar.AM_PM);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Lấy thứ trong tuần (1: Chủ Nhật, 2: Thứ Hai, ..., 7: Thứ Bảy)
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // Lấy tháng ( tháng bắt đầu từ 0 cho tháng 1)
        int month = calendar.get(Calendar.MONTH);
        // Lấy năm
        int year = calendar.get(Calendar.YEAR);
        String buoiText;
        if (buoi == Calendar.AM) {
            buoiText = "AM";
        } else {
            buoiText = "PM";
        }
        String formattedMinutes = String.format("%02d", phut);

        hour.setText(String.valueOf(gio));
        minutes.setText(String.valueOf(formattedMinutes));
        time.setText(buoiText);
        date.setText("Thứ " + dayOfWeek + ", " + dayOfMonth + "/" + (month + 1) + "/" + year);

        speechRecognitionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        handleSpeechRecognitionResult(result.getData());
                    }
                });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechRecognition();
            }
        });



        DatabaseReference myRef = database.getReference().child("dht11").child("temp");

         myRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                String floatValue = snapshot.getValue(String.class);
                 temp.setText(String.valueOf(floatValue));


             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

        DatabaseReference humidity= database.getReference().child("dht11").child("humi");
        humidity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String floatHUMI = snapshot.getValue(String.class);
               humi.setText(String.valueOf(floatHUMI));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        // Lắng nghe sự thay đổi trong giá trị của FireClass
//        fireClassRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                // Kiểm tra giá trị của FireClass khi có sự thay đổi
//                String fireClassValue = dataSnapshot.getValue(String.class);
//                if (fireClassValue != null && fireClassValue.equals("1")) {
//                    // Gửi thông báo cháy khi FireClass có giá trị là 1
//                    sendnotification("Cảnh báo cháy", "Phát hiện có nguy cơ cháy trong lớp học ",1);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
//            }
//        });
//
//        // Lắng nghe sự thay đổi trong giá trị của FireWC
//        fireWCRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Kiểm tra giá trị của FireWC khi có sự thay đổi
//                String fireWCValue = dataSnapshot.getValue(String.class);
//                if (fireWCValue != null && fireWCValue.equals("1")) {
//                    // Gửi thông báo cháy khi FireWC có giá trị là 1
//                    sendnotification("Cảnh báo cháy", "Phát hiện có nguy cơ cháy trong khu vực WC ",2);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
//            }
//        });


        return root;
    }
//    private void sendnotification(String title, String message,int notificationId) {
//        Context context = getContext();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//        android.app.Notification notification =new NotificationCompat.Builder(getContext(), MyApplication.CHANNEL_ID)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setLargeIcon(bitmap)
//                .build();
//
//        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(notificationManager!=null){
//            notificationManager.notify(notificationId,notification);
//        }
//    }


    private void startSpeechRecognition() {
        if (isSpeechRecognitionAvailable()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy thử nói gì đó...");

            try {
                speechRecognitionLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Không thể mở bộ nhận dạng giọng nói", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Thiết bị của bạn không hỗ trợ nhận dạng giọng nói", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSpeechRecognitionAvailable() {
        return getActivity().getPackageManager().resolveActivity(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0) != null;
    }



    private void handleSpeechRecognitionResult(Intent data) {
        DatabaseReference daa = datamic.getReference();
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (result != null && !result.isEmpty()) {
            String spokenText = result.get(0);
            if (spokenText.contains("bật đèn lớp học")) {
                daa.child("LedClass").setValue("1");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("tắt đèn lớp học")) {
                daa.child("LedClass").setValue("0");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
               }
                else if (spokenText.contains("bật quạt lớp học")) {
                daa.child("fan").setValue("1");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
                } else if (spokenText.contains("tắt quạt lớp học")) {
                daa.child("fan").setValue("0");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("bật đèn sân trường")) {
                daa.child("LedYard").setValue("1");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("tắt đèn sân trường")) {
                daa.child("LedYard").setValue("0");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("mở cổng")) {
                daa.child("gate").setValue("1");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("đóng cổng")) {
                daa.child("gate").setValue("0");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("bật đèn nhà xe")) {
                daa.child("LedGara").setValue("1");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            } else if (spokenText.contains("tắt đèn nhà xe")) {
                daa.child("LedGara").setValue("0");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            }
            else if (spokenText.contains("Mở nhà xe")) {
                daa.child("servo").setValue("1");
                Toast.makeText(requireContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireContext(), "Lệnh không được nhận dạng.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}