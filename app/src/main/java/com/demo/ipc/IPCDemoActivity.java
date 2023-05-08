package com.demo.ipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.demo.R;
import com.demo.ipc.aidl.MyAIDLServiceConnection;
import com.demo.ipc.ashmem.AshmemServiceConnection;
import com.demo.ipc.messenger.MessengerServer;
import com.demo.ipc.messenger.MessengerServiceConnection;

public class IPCDemoActivity extends AppCompatActivity {

    private static final String TAG = "IPCDemoActivity";

    // AIDL
    private final MyAIDLServiceConnection myAIDLServiceConnection = new MyAIDLServiceConnection();

    // Messenger
    private final Messenger mMessenger = new Messenger(new InnerHandler(Looper.myLooper()));
    private final MessengerServiceConnection mMessengerServiceConnection = new MessengerServiceConnection();

    // Ashmem
    private final AshmemServiceConnection ashmemServiceConnection = new AshmemServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcdemo);

        // AIDL
        initAIDLView();
        bindAIDLService();

        // Ashmem
        initAshmemView();
        bindAshmemService();

        // Messenger
        initMessengerView();
        bindMessengerService();
    }

    private void bindMessengerService() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MESSENGER");
        intent.setPackage("com.demo");
        bindService(intent, mMessengerServiceConnection, BIND_AUTO_CREATE);
    }

    private void initMessengerView() {
        Button getMessengerServiceBtn = findViewById(R.id.get_messenger_service);
        getMessengerServiceBtn.setOnClickListener(view -> {
            Message msgFromClient = Message.obtain();
            msgFromClient.what = MessengerServer.MSG_FROM_CLIENT;
            msgFromClient.replyTo = mMessenger;
            if (mMessengerServiceConnection.isConnect()) {
                try {
                    mMessengerServiceConnection.getService().send(msgFromClient);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bindAshmemService() {
        Intent intent = new Intent();
        intent.setAction("com.demo.ipc.ashmem.MyAshmemService");
        intent.setPackage("com.demo");
        bindService(intent, ashmemServiceConnection, BIND_AUTO_CREATE);
    }

    private void initAshmemView() {
        Button getAshmemServiceBtn = findViewById(R.id.get_ashmem_service);
        getAshmemServiceBtn.setOnClickListener(view -> {
            try {
                Toast.makeText(IPCDemoActivity.this,
                        ProcessUtil.getCurProcessLog() + ": "
                                + ashmemServiceConnection.getIMyAshmemAidlInterface().getString(),
                        Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void initAIDLView() {
        Button getAIDLBtn = findViewById(R.id.get_aidl_service);
        getAIDLBtn.setOnClickListener(view -> {
            Toast.makeText(IPCDemoActivity.this,
                    String.format("Process: %s, accept name: %s",
                            ProcessUtil.getCurrentProcessName(this),
                            myAIDLServiceConnection.getName()),
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, String.format("Process: %s, accept name: %s",
                    ProcessUtil.getCurrentProcessName(this),
                    myAIDLServiceConnection.getName()));
        });
    }

    private void bindAIDLService() {
        Intent intent = new Intent();
        intent.setAction("com.demo.ipc.MyAIDLService");
        intent.setPackage("com.demo");
        bindService(intent, myAIDLServiceConnection, BIND_AUTO_CREATE);
    }

    private class InnerHandler extends Handler {

        public InnerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msgFromServer) {
            if (msgFromServer.what == MessengerServer.MSG_TO_CLIENT) {
                Bundle data = msgFromServer.getData();
                Toast.makeText(IPCDemoActivity.this,
                        String.format(ProcessUtil.getCurProcessLog() + " name: %s, userId: %d, isLogin: %b",
                                data.get(MessengerServer.NICK_NAME),
                                data.getInt(MessengerServer.USER_ID),
                                data.getBoolean(MessengerServer.IS_LOGIN)),
                        Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msgFromServer);
        }
    }

}