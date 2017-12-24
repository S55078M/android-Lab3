package com.lab3_service.lab3;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


public class EchoService extends Service {

    final static String KEY_MESSAGE_ACTIVITY = "MESSAGE_ACTIVITY";

    final static String ACTION_SEND_MESSAGE_ACTIVITY = "SEND_MESSAGE_ACTIVITY";

    private ServiceReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createReceiver();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * Receives messages from MainActivity and sends responses to MainActivity
     */
    private class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (MainActivity.ACTION_SEND_MESSAGE_SERVICE.equals(action)) {
                String message = intent.getStringExtra(MainActivity.KEY_MESSAGE_SERVICE);

                message = "Hello " +
                        message +
                        ", my name is " +
                        System.identityHashCode(this);

                Intent intentBack = new Intent();
                intentBack.setAction(ACTION_SEND_MESSAGE_ACTIVITY);
                intentBack.putExtra(KEY_MESSAGE_ACTIVITY, message);
                sendBroadcast(intentBack);
            }
        }
    }

    /**
     * Creates and registers new receiver of messages from MainActivity
     */
    private void createReceiver() {

        receiver = new ServiceReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MainActivity.ACTION_SEND_MESSAGE_SERVICE);
        registerReceiver(receiver, intentFilter);
    }

}
