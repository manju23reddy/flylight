package com.manju.alex.flylight.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mreddy3 on 3/27/2018.
 */

public class RoomHolder implements Parcelable{

    private String areaName;
    private String areaDesc;
    ArrayList<String> linkedDevices;


    public RoomHolder(){
        linkedDevices = new ArrayList<>();
    }

    public RoomHolder(Parcel in){
        areaName = in.readString();
        areaDesc = in.readString();
        linkedDevices = new ArrayList<>();
        in.readStringList(linkedDevices);

    }

    public RoomHolder(String aAreaName, String aADesc, ArrayList<String> linkedDevices){
        this.areaName = aAreaName;
        this.areaDesc = aADesc;
        this.linkedDevices = linkedDevices;
    }


    public void setAreaName(String aName){
        this.areaName = aName;
    }

    public void setAreaDesc(String aDesc){
        this.areaDesc = aDesc;
    }

    public void addLinkedDevicesToRoom(String aLinkedDevices){
        this.linkedDevices.add(aLinkedDevices);
    }


    public String getAreaName(){
        return this.areaName;
    }

    public String getAreaDesc(){
        return this.areaDesc;
    }

    public ArrayList<String> getLinkedDevices(){
        return this.linkedDevices;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.areaName);
        parcel.writeString(this.areaDesc);
        parcel.writeStringList(this.linkedDevices);
    }

    public static final Creator<RoomHolder> CREATOR = new ClassLoaderCreator<RoomHolder>() {
        @Override
        public RoomHolder createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new RoomHolder(parcel);
        }

        @Override
        public RoomHolder createFromParcel(Parcel parcel) {
            return new RoomHolder(parcel);
        }

        @Override
        public RoomHolder[] newArray(int i) {
            return new RoomHolder[0];
        }
    };
}
