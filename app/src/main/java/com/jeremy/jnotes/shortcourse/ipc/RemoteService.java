package com.jeremy.jnotes.shortcourse.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.jeremy.jnotes.IConnectionService;
import com.jeremy.jnotes.IMessageService;
import com.jeremy.jnotes.IServiceManager;
import com.jeremy.jnotes.MessageReceiveListener;
import com.jeremy.jnotes.shortcourse.ipc.entity.Message;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 管理和提供子进程连接和消息服务
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    private final RemoteCallbackList<MessageReceiveListener> mMessageReceiveListeners = new RemoteCallbackList<>();
    private boolean isConnected = false;
    private ScheduledThreadPoolExecutor mExecutor;
    private ScheduledFuture<?> mScheduledFuture;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                bundle.setClassLoader(Message.class.getClassLoader());
                Message message = bundle.getParcelable("message");
                Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();
            }

            // 回复消息给客户端
            try {
                Messenger clienMessenger = msg.replyTo;
                android.os.Message message = new android.os.Message();
                Message messageBean = new Message("message is reply from server by Messenger");
                bundle.putParcelable("message", messageBean);
                message.setData(bundle);
                clienMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private Messenger mMessenger = new Messenger(mHandler);

    private IConnectionService mConnectionService = new IConnectionService.Stub() {

        @Override
        public void connect() throws RemoteException {
//            try {
//                Thread.sleep(5000);
            isConnected = true;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                }
            });
            mScheduledFuture = mExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    int count = mMessageReceiveListeners.beginBroadcast();
                    for (int i = 0; i < count; i++) {
                        try {
                            Message message = new Message("this is from remote ---" + i);
                            mMessageReceiveListeners.getBroadcastItem(i).onReceiveMessage(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    mMessageReceiveListeners.finishBroadcast();
                }
            }, 5000, 5000, TimeUnit.MILLISECONDS);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void disconnect() throws RemoteException {
            isConnected = false;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });
            mScheduledFuture.cancel(true);
        }

        @Override
        public boolean isConnected() throws RemoteException {
            return isConnected;
        }
    };

    private IMessageService mMessageService = new IMessageService.Stub() {
        @Override
        public void sendMessage(final Message message) throws RemoteException {
            Log.d(TAG, String.valueOf(message.getContent()));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
            message.setSendSuccess(isConnected);
        }

        @Override
        public void registerMessageReceiveListener(MessageReceiveListener listener) throws RemoteException {
            if (listener != null) {
                mMessageReceiveListeners.register(listener);
            }
        }

        @Override
        public void unRegisterMessageReceiveListener(MessageReceiveListener listener) throws RemoteException {
            if (listener != null) {
                mMessageReceiveListeners.unregister(listener);
            }
        }
    };

    private IServiceManager mServiceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            if (IConnectionService.class.getSimpleName().equals(serviceName)) {
                return mConnectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(serviceName)) {
                return mMessageService.asBinder();
            } else if (android.os.Messenger.class.getSimpleName().equals(serviceName)) {
                Log.d(TAG, "get meesenger binder");
                return mMessenger.getBinder();
            }
            return null;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceManager.asBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = new ScheduledThreadPoolExecutor(1);
    }
}
