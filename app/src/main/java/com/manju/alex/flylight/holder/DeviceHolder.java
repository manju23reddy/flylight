package com.manju.alex.flylight.holder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mreddy3 on 3/27/2018.
 */

public class DeviceHolder implements Parcelable{

    private String deviceName;
    private String deviceAreaRoom;
    private int deviceIntensity;
    private int deviceState;


    public DeviceHolder(){

    }

    public DeviceHolder(Parcel in){
        deviceName = in.readString();
        deviceAreaRoom = in.readString();
        deviceState = in.readInt();
        deviceIntensity = in.readInt();
    }

    public DeviceHolder(String aDName, String aDDesc, int aDState, int aDIntensity){
        this.deviceName = aDName;
        this.deviceAreaRoom = aDDesc;
        this.deviceState = aDState;
        this.deviceIntensity = aDIntensity;
    }


    public void setDeviceName(String aDName){
        this.deviceName = aDName;
    }

    public void setDeviceAreaRoom(String aDDesc){
        this.deviceAreaRoom = aDDesc;
    }

    public void setDeviceState(int aDDState){
        this.deviceState = aDDState;
    }

    public void setDeviceIntensity(int aDintensity){
        this.deviceIntensity = aDintensity;
    }

    public String getDeviceName(){
        return this.deviceName;
    }

    public String getDeviceAreaRoom(){
        return this.deviceAreaRoom;
    }

    public int getDeviceState(){
        return this.deviceState;
    }

    public int getDeviceIntensity(){
        return this.deviceIntensity;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.deviceName);
        parcel.writeString(this.deviceAreaRoom);
        parcel.writeInt(this.deviceState);
        parcel.writeInt(this.deviceIntensity);
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
