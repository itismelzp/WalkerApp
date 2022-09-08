// IRemoteCallback.aidl
package com.demo.ipc.aidl;

// Declare any non-default types here with import statements

interface IRemoteCallback {

    void onReceiver(in String func,in int code,in String params);

    String fetch(in String func);
}