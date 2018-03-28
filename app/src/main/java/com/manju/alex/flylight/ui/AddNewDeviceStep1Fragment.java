package com.manju.alex.flylight.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.manju.alex.flylight.R;

/**
 * Created by mreddy3 on 3/23/2018.
 */

public class AddNewDeviceStep1Fragment extends Fragment implements View.OnClickListener{

    private Button mBtnNext;
    private Button mBtnCancel;

    private IAddDeviceStepTraverseCaller mCallBacks = null;

    public AddNewDeviceStep1Fragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.add_device_step1,
                container, false);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof IAddDeviceStepTraverseCaller){
            mCallBacks = (IAddDeviceStepTraverseCaller)context;
        }
        else{
            throw new ClassCastException(context.toString()+" is not valid instance of "+IAddDeviceStepTraverseCaller.class.getName());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnNext = view.findViewById(R.id.btn_next_step);
        mBtnCancel = view.findViewById(R.id.btn_cancel_step);

        mBtnNext.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next_step:
                mCallBacks.showNextScreen(IAddDeviceStepTraverseCaller.STEP_1+1);
                break;
            case R.id.btn_cancel_step:
                mCallBacks.cancelStepScreen();
                break;
        }
    }
}
