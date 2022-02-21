package com.demo.ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public class MessengerServer extends Service {

    private static final int MSG_FROM_CLIENT = 0x10001;
    private static final int MSG_TO_CLIENT = 0x10002;

    private static final String IS_LOGIN = "isLogin";
    private static final String NICK_NAME = "nickName";
    private static final String USER_ID = "userId";

    private final Messenger mMessenger = new Messenger(new InnerHandler(Looper.myLooper()));
    private IBinder mIBinder;

    public MessengerServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mIBinder == null) {
            mIBinder = mMessenger.getBinder();
        }
        return mIBinder;
    }

    static class InnerHandler extends Handler {

        public InnerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msgFromClient) {

            Message msgToClient = Message.obtain(msgFromClient);
            switch (msgFromClient.what) {
                case MSG_FROM_CLIENT:
                    handleMsgFromClient(msgFromClient, msgToClient);
                    break;
                default:
                    break;
            }

            super.handleMessage(msgFromClient);
        }

        private void handleMsgFromClient(Message msgFromClient, Message msgToClient) {
            try {
                Thread.sleep(2000);

                Bundle toClientData = new Bundle();
                toClientData.putString(NICK_NAME, "zhang san");
                toClientData.putBoolean(IS_LOGIN, true);
                toClientData.putInt(USER_ID, 12345);
                msgToClient.setData(toClientData);
                msgToClient.what = MSG_TO_CLIENT;
                msgFromClient.replyTo.send(msgToClient);
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}