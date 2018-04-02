package com.manju.alex.flylight.ui.mainui;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.adapters.AreaListAdapter;
import com.manju.alex.flylight.holder.RoomHolder;
import com.manju.alex.flylight.ui.AddNewAreaActivity;
import com.manju.alex.flylight.ui.RoomDetailsActivity;
import com.manju.alex.flylight.util.FlyLightUtilConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsListFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    @BindView(R.id.fab_btn_add_new_room)
    FloatingActionButton mAddNewRoomBtn;
    Unbinder mUnBinder = null;



    AreaListAdapter mAreaAdapter;
    @BindView(R.id.rcv_rooms_list)
    RecyclerView mRoomsRecyclerView;

    @BindView(R.id.txtv_no_rooms_message)
    TextView mNoRoomsMsg;

    Context mContext;


    public RoomsListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms_list, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddNewRoomBtn.setOnClickListener(this);

        GridLayoutManager gridLytMgr = new GridLayoutManager(mContext, 2);
        mRoomsRecyclerView.setHasFixedSize(true);
        mRoomsRecyclerView.setLayoutManager(gridLytMgr);
        mAreaAdapter = new AreaListAdapter(mContext, mCallBack);
        mRoomsRecyclerView.setAdapter(mAreaAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_btn_add_new_room:
                Intent newRoomIntent = new Intent(getActivity().getApplicationContext(), AddNewAreaActivity.class);
                startActivity(newRoomIntent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();

    }

    private AreaListAdapter.AreaItemSelectionListener mCallBack = new AreaListAdapter.AreaItemSelectionListener() {
        @Override
        public void onAreaSelected(int pos) {
            RoomHolder holder = mAreaAdapter.getRoomAt(pos);
            Intent roomDetailsActivity = new Intent(mContext, RoomDetailsActivity.class);
            Bundle data = new Bundle();
            data.putParcelable(FlyLightUtilConstants.KEY_AREA_NAME, holder);
            roomDetailsActivity.putExtras(data);
            startActivity(roomDetailsActivity);
        }

        @Override
        public void onAreaSwitchSelected(int pos, boolean state) {

        }

        @Override
        public void notifyRoomsAvailable(boolean state) {
            if(state){
                mNoRoomsMsg.setVisibility(View.GONE);
                mRoomsRecyclerView.setVisibility(View.VISIBLE);
            }
            else{
                mNoRoomsMsg.setVisibility(View.VISIBLE);
                mRoomsRecyclerView.setVisibility(View.GONE);
            }
        }
    };



}
