package com.manju.alex.flylight.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.data.DataHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.holder.DeviceHolder;
import com.manju.alex.flylight.util.FlyLightUtilConstants;

import java.util.ArrayList;

/**
 * Created by mreddy3 on 3/30/2018.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    public interface DeviceSelectionCallbacks{
        void onDeviceSelected(int pos);
        void onDeviceTurnOnOffSelected(int pos, int state);
        void onDevicesAvailable(boolean state);
        void onDeviceTurnOnOffFromRemote(int pos, int state);
    }

    ArrayList<DeviceHolder> mDeviceList = null;
    DeviceSelectionCallbacks mDeviceSelectionCallBacks = null;

    Context mContext;

    DatabaseReference mDevicesRef = null;

    boolean misDeviceAvailableNotified;

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mDeviceNameTxtView;
        ImageView mTurnOnOffBtn;
        TextView mNumOfLinkedAreas;

        public DeviceViewHolder(View view){
            super(view);

            mDeviceNameTxtView = view.findViewById(R.id.txtv_device_name);
            mTurnOnOffBtn = view.findViewById(R.id.btn_power_on_off);
            mNumOfLinkedAreas = view.findViewById(R.id.txtv_linked_area_numbers);

            mTurnOnOffBtn.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_power_on_off:
                    int state = (int)mTurnOnOffBtn.getTag();
                    if(FlyLightUtilConstants.TURN_ON == state){
                        mTurnOnOffBtn.setImageDrawable(mContext.getDrawable(R.drawable.off_icon));
                        state = FlyLightUtilConstants.TURN_OFF;
                    }
                    else{
                        mTurnOnOffBtn.setImageDrawable(mContext.getDrawable(R.drawable.off_icon));
                        state = FlyLightUtilConstants.TURN_ON;
                    }
                    mTurnOnOffBtn.setTag(state);
                    mDeviceSelectionCallBacks.onDeviceTurnOnOffSelected(getAdapterPosition(),
                            state);

                    break;
                default:
                    mDeviceSelectionCallBacks.onDeviceSelected(getAdapterPosition());
                    break;

            }
        }

        public void setSwitchONOFF(int state){
            if(FlyLightUtilConstants.TURN_OFF == state){
                mTurnOnOffBtn.setImageDrawable(mContext.getDrawable(R.drawable.off_icon));
                mTurnOnOffBtn.setTag(FlyLightUtilConstants.TURN_OFF);

            }
            else{
                mTurnOnOffBtn.setImageDrawable(mContext.getDrawable(R.drawable.on_icon));
                mTurnOnOffBtn.setTag(FlyLightUtilConstants.TURN_ON);

            }
        }


    }



    public DeviceListAdapter(Context context, DeviceSelectionCallbacks callbacks){
        mContext = context;
        mDeviceSelectionCallBacks = callbacks;

        mDeviceList = new ArrayList<>();

        mDevicesRef = FirebaseDatabase.getInstance().getReference();


        mDevicesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("devices").
                addChildEventListener(childEventListener);

        misDeviceAvailableNotified = false;
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public DeviceHolder getDeviceAtPos(int pos){
        return mDeviceList.get(pos);
    }

    public void insertDevice(DeviceHolder device){
        mDeviceList.add(device);
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        DeviceHolder device = mDeviceList.get(position);
        holder.mDeviceNameTxtView.setText(device.getDeviceName());
        holder.mNumOfLinkedAreas.setText(device.getLinkedRooms().size()+"");
        holder.setSwitchONOFF(device.getDeviceState());
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int lyt_id = R.layout.item_device;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(lyt_id, parent, false);
        return new DeviceViewHolder(view);
    }
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            DeviceHolder device = dataSnapshot.getValue(DeviceHolder.class);
            insertDevice(device);
            if (!misDeviceAvailableNotified){
                misDeviceAvailableNotified = true;
                mDeviceSelectionCallBacks.onDevicesAvailable(mDeviceList.size() > 0);
            }
            Log.d("Device", dataSnapshot.toString());


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            DeviceHolder device = dataSnapshot.getValue(DeviceHolder.class);
            int i = 0;
            for (DeviceHolder curDev : mDeviceList){
                if (curDev.getDeviceName().equalsIgnoreCase(device.getDeviceName())) {
                    curDev.setLinkedRoomsList(device.getLinkedRooms());
                    curDev.setDeviceState(device.getDeviceState());
                    mDeviceSelectionCallBacks.onDeviceTurnOnOffFromRemote(i, device.getDeviceState());
                    break;
                }
                i++;
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            DeviceHolder device = dataSnapshot.getValue(DeviceHolder.class);
            for(DeviceHolder curDevice : mDeviceList){
                if (curDevice.getDeviceName().equalsIgnoreCase(curDevice.getDeviceName())){
                    mDeviceList.remove(curDevice);
                    notifyDataSetChanged();
                    break;
                }
            }

            if (mDeviceList.size() <= 0){
                mDeviceSelectionCallBacks.onDevicesAvailable(mDeviceList.size() > 0);
                misDeviceAvailableNotified = false;
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
