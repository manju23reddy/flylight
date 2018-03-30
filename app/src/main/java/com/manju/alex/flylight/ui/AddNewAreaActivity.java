package com.manju.alex.flylight.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.holder.RoomHolder;
import com.manju.alex.flylight.util.FlyLightUtilConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewAreaActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = AddNewAreaActivity.class.getSimpleName();

    @BindView(R.id.edtxt_area_name)
    EditText mAreaNameEdt;

    @BindView(R.id.edtxt_area_desc)
    EditText mAreaDescEdt;

    @BindView(R.id.txt_add_device_intro)
    TextView mAddAreaDescTxtv;

    @BindView(R.id.rcv_devices_list)
    RecyclerView mDevicesListRcv;

    @BindView(R.id.btn_create_new_room)
    Button mCreateRoomBtn;

    @BindView(R.id.btn_add_device_step)
    Button mAddDeviceBtn;
    @BindView(R.id.btn_link_devices)
    Button mLinkDeviceBtn;


    @BindView(R.id.lyt_add_area_details)
    LinearLayout mLytAreaDetails;

    private DatabaseReference mDataBase;

    private String mAreaName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_room);
        getSupportActionBar().setTitle(getString(R.string.new_area_title));

        ButterKnife.bind(this);

        mDataBase = FirebaseDatabase.getInstance().getReference();

        initUI();
    }

    private void initUI(){
        mCreateRoomBtn.setOnClickListener(this);
        mAddDeviceBtn.setOnClickListener(this);
        mLinkDeviceBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_create_new_room:

                String roomName = mAreaNameEdt.getText().toString();
                String roomDesc = mAreaDescEdt.getText().toString();
                mAreaName = roomName;
                if(TextUtils.isEmpty(roomName)){
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_area_nam_err),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(roomDesc)){
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_area_desc_err),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    saveNewRoomToFireBase(roomName, roomDesc);
                }

                break;

            case R.id.btn_add_device_step:
                Intent device_add_steps = new Intent(getApplicationContext(), AddNewDeviceActivity.class);
                Bundle data = new Bundle();
                data.putString(FlyLightUtilConstants.KEY_AREA_NAME, mAreaName);
                device_add_steps.putExtras(data);
                startActivity(device_add_steps);
                finish();
                break;
            case R.id.btn_link_devices:
                break;

        }
    }

    private void saveNewRoomToFireBase(String roomName, String roomDesc){
        try{
            RoomHolder room = new RoomHolder(roomName, roomDesc, new ArrayList<String>());
            mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    child(getString(R.string.tab_areas).toLowerCase()).child(roomName).setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mAddAreaDescTxtv.setText(getString(R.string.link_device_to_room_msg));
                    mLytAreaDetails.setVisibility(View.GONE);
                    mCreateRoomBtn.setVisibility(View.GONE);
                    mAddDeviceBtn.setVisibility(View.VISIBLE);
                    mLinkDeviceBtn.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.failed_to_create_room_err),
                            Toast.LENGTH_LONG).show();
                    return;
                }
            });
        }
        catch (Exception ee){
            Log.e(TAG, ee.getMessage());
            Toast.makeText(getApplicationContext(), getString(R.string.failed_to_create_room_err),
                    Toast.LENGTH_LONG).show();;
        }
    }
}
