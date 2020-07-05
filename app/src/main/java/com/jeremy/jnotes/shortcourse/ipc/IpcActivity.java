package com.jeremy.jnotes.shortcourse.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jeremy.jnotes.IConnectionService;
import com.jeremy.jnotes.IMessageService;
import com.jeremy.jnotes.IServiceManager;
import com.jeremy.jnotes.MessageReceiveListener;
import com.jeremy.jnotes.R;
import com.jeremy.jnotes.shortcourse.ipc.entity.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class IpcActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IpcActivity";
    private IConnectionService mConnectionServiceProxy;
    private IMessageService mMessageServiceProxy;
    private Messenger mMessengerProxy;

    private ServiceConnection mConn;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                bundle.setClassLoader(Message.class.getClassLoader());
                final Message message = bundle.getParcelable("message");
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(IpcActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        }
    };
    private Messenger mClientMessenger = new Messenger(mMainHandler);

    private MessageReceiveListener mMessageReceiveListener = new MessageReceiveListener.Stub() {
        @Override
        public void onReceiveMessage(final Message message) throws RemoteException {
            // 注意这里是在Binder线程池中操作，如需做UI操作，需要在UI线程操作
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(IpcActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);
        findViewById(R.id.bt_connect).setOnClickListener(this);
        findViewById(R.id.bt_disconnect).setOnClickListener(this);
        findViewById(R.id.bt_isconnected).setOnClickListener(this);
        findViewById(R.id.bt_connect).setOnClickListener(this);
        findViewById(R.id.bt_send_message).setOnClickListener(this);
        findViewById(R.id.bt_register_receiver).setOnClickListener(this);
        findViewById(R.id.bt_unregister_receiver).setOnClickListener(this);
        findViewById(R.id.bt_messenger).setOnClickListener(this);

        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    IServiceManager serviceManagerProxy = IServiceManager.Stub.asInterface(service);
                    IBinder iConnectBinder = serviceManagerProxy.getService(IConnectionService.class.getSimpleName());
                    mConnectionServiceProxy = IConnectionService.Stub.asInterface(iConnectBinder);

                    IBinder iMessageBinder = serviceManagerProxy.getService(IMessageService.class.getSimpleName());
                    mMessageServiceProxy = IMessageService.Stub.asInterface(iMessageBinder);

                    IBinder messengerBinder = serviceManagerProxy.getService(Messenger.class.getSimpleName());
                    mMessengerProxy = new Messenger(messengerBinder);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(new Intent(this, RemoteService.class), mConn, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_connect:
                try {
                    mConnectionServiceProxy.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_disconnect:
                try {
                    mConnectionServiceProxy.disconnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_isconnected:
                try {
                    boolean connected = mConnectionServiceProxy.isConnected();
                    Toast.makeText(this, "" + connected, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_send_message:
                Message message = new Message("message is from main!");
                try {
                    mMessageServiceProxy.sendMessage(message);
                    Log.d(TAG, String.valueOf(message.isSendSuccess()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_register_receiver:
                try {
                    mMessageServiceProxy.registerMessageReceiveListener(mMessageReceiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_unregister_receiver:
                try {
                    mMessageServiceProxy.unRegisterMessageReceiveListener(mMessageReceiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_messenger:
                try {
                    Message messageBean = new Message("this message is from main by Messenger");
                    android.os.Message messagerMessage = new android.os.Message();
                    messagerMessage.replyTo = mClientMessenger;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("message", messageBean);
                    messagerMessage.setData(bundle);
                    if (mMessengerProxy != null) {
                        mMessengerProxy.send(messagerMessage);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }
}
