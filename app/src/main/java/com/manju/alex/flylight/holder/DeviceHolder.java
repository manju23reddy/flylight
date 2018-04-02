package com.manju.alex.flylight.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mreddy3 on 3/27/2018.
 */

public class DeviceHolder implements Parcelable{

    private String devicename;
    private ArrayList<String> linkedrooms;
    private int deviceintensity;
    private int devicestate;


    public DeviceHolder(){
        linkedrooms = new ArrayList<>();
    }

    public DeviceHolder(Parcel in){
        devicename = in.readString();
        in.readStringList(linkedrooms);
        devicestate = in.readInt();
        deviceintensity = in.readInt();
    }

    public DeviceHolder(String aDName, ArrayList<String> aRooms, int aDState,
                        int aDIntensity){

        this.devicename = aDName;
        this.linkedrooms = aRooms;
        this.devicestate = aDState;
        this.deviceintensity = aDIntensity;
    }


    public void setDeviceName(String aDName){
        this.devicename = aDName;
    }

    public void addAreaRoomToDevice(String aAreaRoom){
        this.linkedrooms.add(aAreaRoom);
    }

    public void setDeviceState(int aDDState){
        this.devicestate = aDDState;
    }

    public void setDeviceIntensity(int aDintensity){
        this.deviceintensity = aDintensity;
    }

    public void setLinkedRoomsList(ArrayList<String> mRoomsList){
        this.linkedrooms = mRoomsList;
    }

    public String getDeviceName(){
        return this.devicename;
    }

    public ArrayList<String> getLinkedRooms(){
        return this.linkedrooms;
    }

    public int getDeviceState(){
        return this.devicestate;
    }

    public int getDeviceIntensity(){
        return this.deviceintensity;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.devicename);
        parcel.writeStringList(this.linkedrooms);
        parcel.writeInt(this.devicestate);
        parcel.writeInt(this.deviceintensity);
    }

    public static final Parcelable.Creator<DeviceHolder> CREATOR = new ClassLoaderCreator<DeviceHolder>() {
        @Override
        public DeviceHolder createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new DeviceHolder(parcel);
        }

        @Override
        public DeviceHolder createFromParcel(Parcel parcel) {
            return new DeviceHolder(parcel);
        }

        @Override
        public DeviceHolder[] newArray(int i) {
            return new DeviceHolder[0];
        }
    };
}
