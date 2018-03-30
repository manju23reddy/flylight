package com.manju.alex.flylight.ui.mainui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.manju.alex.flylight.R;

/**
 * Created by mreddy3 on 3/28/2018.
 */

public class MainFragmentViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MainFragmentViewPagerAdapter(Context context, FragmentManager fmgr){
        super(fmgr);

        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case IMainScreenContract.ROOMS_FRAGMENT:
                return new RoomsListFragment();
            case IMainScreenContract.DEVICES_FRAGMENT:
                return new DevicesListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return IMainScreenContract.MAIN_SCREENS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case IMainScreenContract.ROOMS_FRAGMENT:
                return mContext.getString(R.string.tab_areas);
            case IMainScreenContract.DEVICES_FRAGMENT:
                return mContext.getString(R.string.tab_devices);
            default:
                return null;
        }
    }
}
