package com.manju.alex.flylight.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.ui.mainui.IMainScreenContract;
import com.manju.alex.flylight.ui.mainui.MainFragmentViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlyLightMainActivity extends AppCompatActivity {

    final String TAG = FlyLightMainActivity.class.getSimpleName();

    final int[] mTab_Icons = new int[]{R.drawable.areas_tabs, R.drawable.ic_action_name};

    @BindView(R.id.vpager_pages)
    ViewPager mMainScreenViewPager;

    @BindView(R.id.tab_pages)
    TabLayout mMainScreenTabLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly_light_main);

        ButterKnife.bind(this);

        initViews();

    }

    public void initViews(){
        MainFragmentViewPagerAdapter adapter = new MainFragmentViewPagerAdapter(this,
                getSupportFragmentManager());

        mMainScreenViewPager.setAdapter(adapter);

        mMainScreenTabLyt.setupWithViewPager(mMainScreenViewPager);
        mMainScreenTabLyt.getTabAt(IMainScreenContract.ROOMS_FRAGMENT).
                setIcon(mTab_Icons[IMainScreenContract.ROOMS_FRAGMENT]);
        mMainScreenTabLyt.getTabAt(IMainScreenContract.DEVICES_FRAGMENT).
                setIcon(mTab_Icons[IMainScreenContract.DEVICES_FRAGMENT]);

    }
}
