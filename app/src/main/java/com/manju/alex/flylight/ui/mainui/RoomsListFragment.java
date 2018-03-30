package com.manju.alex.flylight.ui.mainui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manju.alex.flylight.R;
import com.manju.alex.flylight.ui.AddNewAreaActivity;

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


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddNewRoomBtn.setOnClickListener(this);
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
}
