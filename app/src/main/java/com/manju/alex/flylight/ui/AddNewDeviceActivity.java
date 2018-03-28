package com.manju.alex.flylight.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.anton46.stepsview.StepsView;
import com.manju.alex.flylight.R;

/**
 * Created by mreddy3 on 3/22/2018.
 */

public class AddNewDeviceActivity extends AppCompatActivity implements IAddDeviceStepTraverseCaller{




    StepsView mAddDeviceProcessIndicator = null;
    String[] mStepLabels = null;

    private FrameLayout mStepLayout;

    private int mCurrent_Step = 0;

    FragmentManager mFrgManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);

        initUI();

    }

    public void initUI(){
        mStepLabels = getResources().getStringArray(R.array.add_device_labels);

        mAddDeviceProcessIndicator = findViewById(R.id.stps_view_add_new_device_steps);
        mAddDeviceProcessIndicator.setLabels(mStepLabels)
                .setBarColorIndicator(getColor
                        (android.support.v7.appcompat.R.color.material_blue_grey_800))
                .setProgressColorIndicator(getColor(R.color.steps_indicator_orange)).
                setLabelColorIndicator(getColor(R.color.white))
                .setCompletedPosition(mCurrent_Step).drawView();

        mFrgManager = getSupportFragmentManager();
        Fragment curFrag = getItem(IAddDeviceStepTraverseCaller.STEP_1);
        curFrag.setEnterTransition(new Slide(Gravity.RIGHT));
        mFrgManager.beginTransaction().replace(R.id.frame_lyt_add_device, curFrag).commit();


    }

    public void updateStepToNext(int position){
        mAddDeviceProcessIndicator.setCompletedPosition(position).drawView();
    }

    public Fragment getItem(int position) {
        switch (position){
            case IAddDeviceStepTraverseCaller.STEP_1:
                return new AddNewDeviceStep1Fragment();
            case IAddDeviceStepTraverseCaller.STEP_2:
                return new AddNewDeviceStep2Fragment();
            case IAddDeviceStepTraverseCaller.STEP_3:
                return new AddNewDeviceStep3Fragment();
            default:
                return null;

        }

    }


    @Override
    public void showNextScreen(int pos) {
        Fragment curFrag = getItem(pos);
        curFrag.setEnterTransition(new Slide(Gravity.RIGHT));
        mFrgManager.beginTransaction().replace(R.id.frame_lyt_add_device, curFrag).commit();
        updateStepToNext(pos);
    }

    @Override
    public void cancelStepScreen() {
        finish();
    }



}
