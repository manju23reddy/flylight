package com.manju.alex.flylight.ui;

/**
 * Created by mreddy3 on 3/23/2018.
 */

public interface IAddDeviceStepTraverseCaller {



        final int STEP_1 = 0;
        final int STEP_2 = STEP_1+1;
        final int STEP_3 = STEP_2+1;
        final int STEP_4 = STEP_3+1;

        public void showNextScreen(int pos);
        public void cancelStepScreen();

}
