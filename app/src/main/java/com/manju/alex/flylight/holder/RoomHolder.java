package com.manju.alex.flylight.holder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mreddy3 on 3/27/2018.
 */

public class RoomHolder implements Parcelable{

    //private String key;
    private String areaname;
    private String areadesc;
    ArrayList<String> linkeddevices;
    boolean turnonalldevices;


    public RoomHolder(){
        linkeddevices = new ArrayList<>();
    }

    public RoomHolder(Parcel in){
        //key = in.readString();
        areaname = in.readString();
        areadesc = in.readString();
        linkeddevices = new ArrayList<>();
        in.readStringList(linkeddevices);
        turnonalldevices = in.readInt() == 0 ? false : true;

    }

    public RoomHolder(String aAreaName, String aADesc, ArrayList<String> linkedDevices, boolean turnAll){
        //this.key = "";
        this.areaname = aAreaName;
        this.areadesc = aADesc;
        this.linkeddevices = linkedDevices;
        turnonalldevices = turnAll;
    }

    /*
    public RoomHolder(String aKey, String aAreaName, String aADesc, ArrayList<String> linkedDevices){
        this.key = aKey;
        this.areaname = aAreaName;
        this.areadesc = aADesc;
        this.linkeddevices = linkedDevices;
    }*/

    public void setAllOn(boolean allOn){
        this.turnonalldevices = allOn;
    }
   // public void setkey(String aKey){ this.key = aKey; }

    public void setareaname(String aName){
        this.areaname = aName;
    }

    public void setaresdesc(String aDesc){
        this.areadesc = aDesc;
    }

    public void setLinkeddevices(ArrayList<String> aLinkedDevices){
        this.linkeddevices = aLinkedDevices;
    }




    public String getareaname(){
        return this.areaname;
    }

    public String getareadesc(){
        return this.areadesc;
    }

    public ArrayList<String> getlinkeddevices(){
        return this.linkeddevices;
    }

    //public String getkey(){ return this.key; }

    public boolean getAllOn(){
        return this.turnonalldevices;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       // parcel.writeString(this.key);
        parcel.writeString(this.areaname);
        parcel.writeString(this.areadesc);
        parcel.writeStringList(this.linkeddevices);
        int state = this.turnonalldevices == true ? 1 : 0;
        parcel.writeInt( state);
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
