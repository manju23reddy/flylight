package com.manju.alex.flylight.holder;

import java.net.InetAddress;

/**
 * Created by mreddy3 on 3/27/2018.
 */

public final class NewDeviceInfoHolder {

    private InetAddress newDeviceInetAddress = null;

    private final static class NewDeviceInstanceHolder{
        final static NewDeviceInfoHolder INSTANCE = new NewDeviceInfoHolder();
    }

    private NewDeviceInfoHolder(){

    }

    public static NewDeviceInfoHolder getInstance(){
        return NewDeviceInstanceHolder.INSTANCE;
    }

    public void setNewDeviceInetAddress(InetAddress aAddr){
        newDeviceInetAddress = aAddr;
    }

    public InetAddress getNewDeviceInetAddress(){
        return newDeviceInetAddress;
    }
}
