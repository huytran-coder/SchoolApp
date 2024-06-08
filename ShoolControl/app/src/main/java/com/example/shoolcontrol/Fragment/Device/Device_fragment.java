package com.example.shoolcontrol.Fragment.Device;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoolcontrol.Device.Class;
import com.example.shoolcontrol.Device.Garage;
import com.example.shoolcontrol.Device.Gate;
import com.example.shoolcontrol.Device.Yard;
import com.example.shoolcontrol.MainActivity;
import com.example.shoolcontrol.R;

public class Device_fragment extends Fragment {


    private CardView classs,gate,yard,garage;

    private DeviceFragmentViewModel mViewModel;


    public static Device_fragment newInstance() {
        return new Device_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root;
        root=inflater.inflate(R.layout.fragment_device, container, false);
        classs=root.findViewById(R.id.btnclass);
        gate=root.findViewById(R.id.btngate);
        yard=root.findViewById(R.id.btnyard);
        garage=root.findViewById(R.id.btngarage);

        gate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Gate.class);
                startActivity(intent);

            }
        });
        classs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Class.class);
                startActivity(intent);

            }
        });
        yard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Yard.class);
                startActivity(intent);

            }
        });
        garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Garage.class);
                startActivity(intent);

            }
        });


        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeviceFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}