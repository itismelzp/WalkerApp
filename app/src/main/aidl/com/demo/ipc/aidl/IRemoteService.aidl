// IRemoteService.aidl
package com.demo.ipc.aidl;

import com.demo.ipc.aidl.IRemoteCallback;

// Declare any non-default types here with import statements

interface IRemoteService {

    void register(in String pkgName,in IRemoteCallback callback);

    void unRegister(in String pkgName,in IRemoteCallback callback);

    void send(in String packageName,in String func,in String params);

    String fetch(in String packageName,in String func);

}