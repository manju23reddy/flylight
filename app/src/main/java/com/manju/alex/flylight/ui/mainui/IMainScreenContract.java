package com.manju.alex.flylight.ui.mainui;

/**
 * Created by mreddy3 on 3/28/2018.
 */

public interface IMainScreenContract {
    int ROOMS_FRAGMENT = 0;
    int DEVICES_FRAGMENT = ROOMS_FRAGMENT+1;
    int[] MAIN_SCREENS = {ROOMS_FRAGMENT, DEVICES_FRAGMENT};

}
