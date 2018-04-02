package com.manju.alex.flylight.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manju.alex.flylight.R;
import com.manju.alex.flylight.holder.RoomHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mreddy3 on 3/30/2018.
 */

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.AreaViewHolder> {

    public interface AreaItemSelectionListener{
        void onAreaSelected(int pos);
        void onAreaSwitchSelected(int pos, boolean state);
        void notifyRoomsAvailable(boolean state);
    }

    Context mContext;

    AreaItemSelectionListener mAreaItemSelectionCallBacks;

    ArrayList<RoomHolder> mListofRooms;

    DatabaseReference mDBReference = null;

    private boolean isRoomsAvailableNotified = false;


    public class AreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView mNumOfLinkedDevices;

        TextView mAreaName;

        ImageView mOnOffImgView;

        public AreaViewHolder(View view){
            super(view);

            ButterKnife.bind(view);

            view.setOnClickListener(this);
            mOnOffImgView = view.findViewById(R.id.btn_power_on_off);
            mNumOfLinkedDevices = view.findViewById(R.id.txtv_linked_devices_numbers);
            mAreaName = view.findViewById(R.id.txtv_area_name);
            mOnOffImgView.setOnClickListener(this);
            mOnOffImgView.setTag(false);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.btn_power_on_off:
                    boolean state = (Boolean)view.getTag();
                    state = !state;
                    view.setTag(state);
                    if(true == state){
                        mOnOffImgView.setImageDrawable(mContext.getDrawable(R.drawable.on_icon));
                    }
                    else{
                        mOnOffImgView.setImageDrawable(mContext.getDrawable(R.drawable.off_icon));
                    }
                    mAreaItemSelectionCallBacks.onAreaSwitchSelected(getAdapterPosition(), state);
                    break;
                default:
                    mAreaItemSelectionCallBacks.onAreaSelected(getAdapterPosition());
                    break;
            }
        }
    }

    public AreaListAdapter(Context context, AreaItemSelectionListener callback){
        mContext = context;
        mAreaItemSelectionCallBacks = callback;

        mListofRooms = new ArrayList<>();

        mDBReference = FirebaseDatabase.getInstance().getReference();
        mDBReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("areas").
                addChildEventListener(areasAddEventListener);

        isRoomsAvailableNotified = false;
    }


    @Override
    public int getItemCount() {
        return mListofRooms.size();
    }

    public RoomHolder getRoomAt(int pos){
        return mListofRooms.get(pos);
    }

    public void insertRoom(RoomHolder room){
        mListofRooms.add(room);
        notifyDataSetChanged();
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int lyt_id = R.layout.item_room;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(lyt_id, parent, false);

        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        RoomHolder room = mListofRooms.get(position);
        holder.mAreaName.setText(room.getareaname());
        holder.mNumOfLinkedDevices.setText(""+room.getlinkeddevices().size());
        if (room.getAllOn()){
            holder.mOnOffImgView.setImageDrawable(mContext.getDrawable(R.drawable.on_icon));
            holder.mOnOffImgView.setTag(true);
        }
        else{
            holder.mOnOffImgView.setImageDrawable(mContext.getDrawable(R.drawable.off_icon));
            holder.mOnOffImgView.setTag(false);
        }
    }

    ChildEventListener areasAddEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            RoomHolder holder = dataSnapshot.getValue(RoomHolder.class);
            insertRoom(holder);
            if (!isRoomsAvailableNotified) {
                mAreaItemSelectionCallBacks.notifyRoomsAvailable(mListofRooms.size() > 0);
                isRoomsAvailableNotified= true;
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            RoomHolder room = dataSnapshot.getValue(RoomHolder.class);
            for (RoomHolder curRoom : mListofRooms){
                if (curRoom.getareaname().equalsIgnoreCase(room.getareaname())) {
                    curRoom.setLinkeddevices(room.getlinkeddevices());
                    curRoom.setaresdesc(room.getareadesc());
                    break;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            RoomHolder room = dataSnapshot.getValue(RoomHolder.class);
            for (RoomHolder curRoom : mListofRooms){
                if (curRoom.getareaname().equalsIgnoreCase(room.getareaname())){
                    mListofRooms.remove(curRoom);
                    notifyDataSetChanged();
                    break;
                }
            }
            if ( mListofRooms.size() <= 0 ) {
                mAreaItemSelectionCallBacks.notifyRoomsAvailable(mListofRooms.size() > 0);
                isRoomsAvailableNotified = false;
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
