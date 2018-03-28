package com.manju.alex.flylight.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manju.alex.flylight.R;
import com.manju.alex.flylight.esptouch.EsptouchTask;
import com.manju.alex.flylight.esptouch.IEsptouchListener;
import com.manju.alex.flylight.esptouch.IEsptouchResult;
import com.manju.alex.flylight.esptouch.IEsptouchTask;
import com.manju.alex.flylight.network.EspWifiAdminSimple;

import java.util.List;

/**
 * Created by mreddy3 on 3/23/2018.
 */

public class AddNewDeviceStep2Fragment extends Fragment implements View.OnClickListener{

    final String TAG = AddNewDeviceStep2Fragment.class.getSimpleName();

    private Button mBtnCancel;
    private Button mBtnConfigureWifi;
    private EditText mWifiSSID;
    private EditText mWifiPassword;
    private IAddDeviceStepTraverseCaller mCallBacks = null;
    WifiInfo mWifiInfo;
    EspWifiAdminSimple mWifiAdmin = null;
    ProgressBar mProgressbar = null;
    LinearLayout mWifiSettingsLyt = null;
    TextView mConfiguringText = null;


    private Context mContext = null;


    public AddNewDeviceStep2Fragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_device_step2,
                container, false);



        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        if (context instanceof IAddDeviceStepTraverseCaller){
            mCallBacks = (IAddDeviceStepTraverseCaller)context;
        }
        else{
            throw new ClassCastException(context.toString()+" is not valid instance of "+IAddDeviceStepTraverseCaller.class.getName());
        }

        mWifiAdmin = new EspWifiAdminSimple(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnConfigureWifi = view.findViewById(R.id.btn_configure_wifi);
        mBtnCancel = view.findViewById(R.id.btn_cancel_step);


        mWifiSSID = view.findViewById(R.id.edtxt_wifi_ssid);
        mWifiPassword = view.findViewById(R.id.edtxt_wifi_password);
        mBtnConfigureWifi.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mProgressbar = view.findViewById(R.id.progressBar);
        mWifiSettingsLyt = view.findViewById(R.id.lyt_wifi_details);
        mConfiguringText = view.findViewById(R.id.edtx_wifi_configure_progress_text);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_configure_wifi:
                String apSsid = mWifiSSID.getText().toString();
                apSsid = apSsid.replace("\"", "");
                String apPassword = mWifiPassword.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();

                Log.d(TAG, apSsid);

                if (TextUtils.isEmpty(apSsid) || TextUtils.isEmpty(apPassword)){
                    Toast.makeText(mContext, "Wifi Name and Wifi Password cannot be empty", Toast.LENGTH_LONG).show();
                }
                else {

                    new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword, "" + 3);
                }

                break;
            case R.id.btn_cancel_step:
                mCallBacks.cancelStepScreen();
                break;

        }
    }

    public String getWIFISSIDName(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);


        mWifiInfo = wifiManager.getConnectionInfo();
        if (null != mWifiInfo && mWifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return mWifiInfo.getSSID().toString();
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mWifiSSID){
            String SSIDName = getWIFISSIDName();
            SSIDName.replace("\"",
                    "");
            mWifiSSID.setText(getWIFISSIDName());
        }
    }



    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {



        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            mConfiguringText.setVisibility(View.VISIBLE);
            mWifiSettingsLyt.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                // !!!NOTICE
                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
                String apBssid = params[1];
                String apPassword = params[2];
                String taskResultCountStr = params[3];
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, mContext);
                mEsptouchTask.setEsptouchListener(mEspResultTouchListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = "
                                + resultInList.getBssid()
                                + ",InetAddress = "
                                + resultInList.getInetAddress()
                                .getHostAddress() + "\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                    mCallBacks.showNextScreen(IAddDeviceStepTraverseCaller.STEP_3);

                } else {
                    Toast.makeText(mContext, "Failed to Configure your new FlyLight Device.", Toast.LENGTH_LONG).show();
                    mConfiguringText.setVisibility(View.GONE);
                    mWifiSettingsLyt.setVisibility(View.VISIBLE);
                    mProgressbar.setVisibility(View.GONE);
                    mEsptouchTask.interrupt();
                    mEsptouchTask = null;
                }
            }
        }
    }

    IEsptouchListener mEspResultTouchListener = new IEsptouchListener() {
        @Override
        public void onEsptouchResultAdded(IEsptouchResult result) {
            Log.d(TAG, result.getInetAddress().toString());
        }
    };


}