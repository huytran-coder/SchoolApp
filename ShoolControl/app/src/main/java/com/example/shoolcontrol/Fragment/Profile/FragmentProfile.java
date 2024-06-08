package com.example.shoolcontrol.Fragment.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoolcontrol.Fragment.Home.HomeFragment;
import com.example.shoolcontrol.MainActivity;
import com.example.shoolcontrol.R;
import com.example.shoolcontrol.VerifyPhoneNumber;
import com.example.shoolcontrol.control_tinnhan;

public class FragmentProfile extends Fragment {

    private FragmentProfileViewModel mViewModel;
    private TextView nameTextView;
    private  TextView sdt;


    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root;
        root=inflater.inflate(R.layout.fragmnet_profile, container, false);
        nameTextView = root.findViewById(R.id.textname);
        sdt= root.findViewById(R.id.textsdt);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString("first", "");
        String lastName = sharedPreferences.getString("Last", "");
        nameTextView.setText(firstName + " " + lastName);
        SharedPreferences sharedPreferences1 =getActivity().getSharedPreferences("Userphone", Context.MODE_PRIVATE);
        String phone = sharedPreferences1.getString("phone", "");
        sdt.setText("+84-"+ phone);

       Button switchButton = root.findViewById(R.id.btnHomepage);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang trang home (Activity)
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }

        });
        Button question= root.findViewById(R.id.btnquestion);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = "https://www.facebook.com/HuyTran271202";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                startActivity(intent);
            }
        });
        Button noti= root.findViewById(R.id.btnThongbao);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), control_tinnhan.class);
                startActivity(intent);
            }
        });
        Button logout= root.findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false); // Chưa đăng nhập
                editor.apply();
                Intent intent = new Intent(getActivity(), VerifyPhoneNumber.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FragmentProfileViewModel.class);
        // TODO: Use the ViewModel
    }


}