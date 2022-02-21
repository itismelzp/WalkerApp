package com.demo.ipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;

public class IPCDemoActivity extends AppCompatActivity {

    private static final String TAG = "IPCDemoActivity";

    // AIDL
    private IMyAidlInterface mIMyAidlInterface;

    // Messenger
    private final Messenger mMessenger = new Messenger(new InnerHandler(Looper.myLooper()));
    private static final int MSG_FROM_CLIENT = 0x10001;
    private static final int MSG_TO_CLIENT = 0x10002;

    private static final String IS_LOGIN = "isLogin";
    private static final String NICK_NAME = "nickName";
    private static final String USER_ID = "userId";

    private boolean isConn;
    private Messenger mService;
    private TextView tv_state;
    private TextView tv_message;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcdemo);

        initView();


        bindAIDLService();
    }


    private void initView() {
        Button getAIDLBtn = findViewById(R.id.get_aidl_service);
        getAIDLBtn.setOnClickListener(view -> {
            if (mIMyAidlInterface == null) {
                Toast.makeText(IPCDemoActivity.this,
                        "mIMyAidlInterface == null", Toast.LENGTH_SHORT
                ).show();
                return;
            }
            try {
                Toast.makeText(IPCDemoActivity.this,
                        String.format("Process: %s, accept name: %s",
                                ProcessUtil.getCurrentProcessName(this),
                                mIMyAidlInterface.getName()),
                        Toast.LENGTH_SHORT).show();

                Log.i(TAG, String.format("Process: %s, accept name: %s",
                        ProcessUtil.getCurrentProcessName(this),
                        mIMyAidlInterface.getName()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void bindAIDLService() {
        Intent intent = new Intent();
        intent.setAction("com.demo.ipc.myservice");
        intent.setPackage("com.demo");

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
                Log.d(TAG, "[onServiceConnected] mIMyAidlInterface: " + mIMyAidlInterface);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, BIND_AUTO_CREATE);
    }

    private class InnerHandler extends Handler {

        public InnerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msgFromServer) {
            if (msgFromServer.what == MSG_TO_CLIENT) {
                Bundle data = msgFromServer.getData();
                tv_message.setText("服务器返回内容\n" +
                        data.get(NICK_NAME) + "\n" +
                        data.get(USER_ID) + "\n" +
                        data.get(IS_LOGIN) + "\n");
            }
            super.handleMessage(msgFromServer);
        }
    }

    @SuppressLint("SetTextI18n")
    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            isConn = true;
            tv_state.setText("连接状态：connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isConn = false;
            tv_state.setText("连接状态：disconnected!");
        }
    };
}