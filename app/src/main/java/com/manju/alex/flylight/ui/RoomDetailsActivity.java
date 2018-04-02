package com.manju.alex.flylight.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.manju.alex.flylight.R;
import com.manju.alex.flylight.holder.RoomHolder;
import com.manju.alex.flylight.util.FlyLightUtilConstants;

public class RoomDetailsActivity extends AppCompatActivity {

    RoomHolder mInRoomData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        Bundle data = getIntent().getExtras();
        if (data != null && data.containsKey(FlyLightUtilConstants.KEY_AREA_NAME)){
            mInRoomData = data.getParcelable(FlyLightUtilConstants.KEY_AREA_NAME);

            getSupportActionBar().setTitle(mInRoomData.getareaname());
        }

    }
}
