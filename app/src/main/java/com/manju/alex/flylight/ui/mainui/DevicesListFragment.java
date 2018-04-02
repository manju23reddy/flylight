package com.manju.alex.flylight.ui.mainui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.adapters.DeviceListAdapter;
import com.manju.alex.flylight.holder.DeviceHolder;
import com.manju.alex.flylight.ui.AddNewDeviceActivity;
import com.manju.alex.flylight.ui.DeviceDetailsActivity;
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

    @BindView(R.id.rcv_devices_list)
    RecyclerView mDeviceListRcv;

    Unbinder mUnBindView = null;

    DeviceListAdapter mDeviceAdapter = null;

    Context mContext;


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

        mDeviceAdapter = new DeviceListAdapter(getContext(), mDeviceSelectionHandler);
        GridLayoutManager grd_lyt_manager = new GridLayoutManager(mContext, 2);
        mDeviceListRcv.setLayoutManager(grd_lyt_manager);
        mDeviceListRcv.setAdapter(mDeviceAdapter);
        mDeviceListRcv.setHasFixedSize(true);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    private DeviceListAdapter.DeviceSelectionCallbacks mDeviceSelectionHandler = new
            DeviceListAdapter.DeviceSelectionCallbacks() {
        @Override
        public void onDeviceSelected(int pos) {
            DeviceHolder holder = mDeviceAdapter.getDeviceAtPos(pos);
            Intent device_details = new Intent(mContext, DeviceDetailsActivity.class);
            Bundle data = new Bundle();
            data.putParcelable(FlyLightUtilConstants.KEY_DEVICE_NAME, holder);
            device_details.putExtras(data);
            startActivity(device_details);
        }

        @Override
        public void onDeviceTurnOnOffSelected(int pos, int state) {

            DeviceHolder holder = mDeviceAdapter.getDeviceAtPos(pos);

            //int ON_OFF = true == state ? FlyLightUtilConstants.TURN_ON :
            //        FlyLightUtilConstants.TURN_OFF;

            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).child("devices").child(holder.getDeviceName()).
                    child("deviceState").setValue(state);

        }

        @Override
        public void onDevicesAvailable(boolean state) {
            if (state){
                mNoDevicesMessageTxt.setVisibility(View.GONE);
            }
            else{
                mNoDevicesMessageTxt.setVisibility(View.VISIBLE);
            }
        }

                @Override
                public void onDeviceTurnOnOffFromRemote(int pos, int state) {
                    DeviceListAdapter.DeviceViewHolder view = (DeviceListAdapter.DeviceViewHolder)
                            mDeviceListRcv.findViewHolderForLayoutPosition(pos);
                    if (null != view){
                        view.setSwitchONOFF(state);
                    }

                }
            };
}
