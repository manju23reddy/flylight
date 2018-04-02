package com.manju.alex.flylight.ui;

import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manju.alex.flylight.BuildConfig;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.holder.DeviceHolder;
import com.manju.alex.flylight.holder.NewDeviceInfoHolder;
import com.manju.alex.flylight.holder.RoomHolder;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by mreddy3 on 3/23/2018.
 */

public class AddNewDeviceStep3Fragment extends Fragment implements View.OnClickListener{

    private final String TAG = AddNewDeviceStep3Fragment.class.getSimpleName();

    private IAddDeviceStepTraverseCaller mCallBacks = null;

    EditText mDeviceName = null;
    private Button mBtnConfigureDevice;
    private DeviceHolder mNewDeviceInfo;
    private DatabaseReference mDataBase;

    public AddNewDeviceStep3Fragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.add_device_step3,
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

        mBtnConfigureDevice = view.findViewById(R.id.btn_configure_device);

        mDeviceName = view.findViewById(R.id.edtxt_device_name);

        mBtnConfigureDevice.setOnClickListener(this);

        mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_configure_device:
                //Todo 1. Get the device details and push into Firebase

                //Todo 2. after push is complete send the firebase project url, auth id and device node details to new device.
                final String deviceName = mDeviceName.getText().toString();

                if (TextUtils.isEmpty(deviceName)){
                    Toast.makeText(getContext(), "Device Name Cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    mNewDeviceInfo = new DeviceHolder();
                    mNewDeviceInfo.setDeviceName(deviceName);
                    mNewDeviceInfo.setDeviceIntensity(0);
                    mNewDeviceInfo.setDeviceState(0);

                    /*DatabaseReference ref = mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("devices");

                    ref.push().child(mNewDeviceInfo.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

                    mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            child("devices").child(mNewDeviceInfo.getDeviceName()).
                            setValue(mNewDeviceInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                                new SenderUDPServer(BuildConfig.FB_PROJECT_URL,
                                        BuildConfig.FB_PROJECT_DB_KEY, mNewDeviceInfo,
                                        FirebaseAuth.getInstance().getCurrentUser().
                                                getUid()).start();


                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                }

                break;

            case R.id.btn_cancel_step:
                mCallBacks.cancelStepScreen();
                break;
        }
    }

    public class SenderUDPServer extends Thread{
        private DatagramSocket mSoc;
        private byte[] mData;

        public SenderUDPServer(String projectUrl, String authToken, DeviceHolder deviceHolder, String userUID){
            try {
                mSoc = new DatagramSocket(18277);

                JSONObject sendData = new JSONObject();
                sendData.put("FB_HOST_URL", projectUrl);
                sendData.put("FB_AUTH_KEY", authToken);
                sendData.put("USER_UID", userUID);
                sendData.put("LISTEN_URL", "/"+userUID+"/devices/"+deviceHolder.getDeviceName());
                sendData.put("LISTEN_NODE", "deviceState");
                sendData.put("LED_NUM", 2);
                mData = new byte[sendData.length()];
                mData = sendData.toString().getBytes();
                Log.d(TAG, "sending to device "+mData);
            }
            catch (Exception ee){
                Log.e(TAG, ee.getMessage());
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                DatagramPacket packet = new DatagramPacket(mData, mData.length, NewDeviceInfoHolder.getInstance().getNewDeviceInetAddress(), 4210);
                mSoc.send(packet);
            }
            catch (Exception ee){
                Log.e(TAG, ee.getMessage());
            }
            finally {
                if (null != mSoc) {
                    mSoc.close();
                }
            }




            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), mNewDeviceInfo.getDeviceName()+" added successfully", Toast.LENGTH_LONG).show();
                    mCallBacks.cancelStepScreen();
                }
            });
        }
    }
}
