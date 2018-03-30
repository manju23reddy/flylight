package com.manju.alex.flylight.ui.mainui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manju.alex.flylight.R;
import com.manju.alex.flylight.ui.AddNewDeviceActivity;
import com.manju.alex.flylight.util.FlyLightUtilConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mreddy3 on 3/28/2018.
 */

public class DevicesListFragment  extends Fragment implements View.OnClickListener{

    @BindView(R.id.fab_btn_add_new_device)
    FloatingActionButton mAddDeviceBtn;

    @BindView(R.id.txtv_no_device_message)
    TextView mNoDevicesMessageTxt;

    Unbinder mUnBindView = null;

    public DevicesListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_devices_list, container, false);

        mUnBindView = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddDeviceBtn.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBindView.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_btn_add_new_device:
                Intent device_add_steps = new Intent(getActivity().getApplicationContext(), AddNewDeviceActivity.class);
                startActivity(device_add_steps);
                break;
        }
    }
}
