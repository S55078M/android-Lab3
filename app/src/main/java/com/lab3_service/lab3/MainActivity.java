package com.lab3_service.lab3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class MainActivity extends AppCompatActivity {

    final static String KEY_MESSAGE_SERVICE = "MESSAGE_SERVICE";

    final static String ACTION_SEND_MESSAGE_SERVICE = "SEND_MESSAGE_SERVICE";


    @BindView(R.id.editName)
    EditText editName;

    @BindView(R.id.switchService)
    Switch switchService;

    @BindView(R.id.messages)
    TextView messages;

    @BindView(R.id.send)
    TextView send;

    Intent intent;
    MainActivityReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = null;

        ButterKnife.bind(this);
    }

    @OnClick(R.id.send)
    void onEditNameAction() {
        if (intent == null) {
            addMessage("### Service is offline ###");
            makeToast("Nope");
        } else {
            send();
        }
    }

    @OnCheckedChanged(R.id.switchService)
    void onSwitchServiceChecked(CompoundButton view, boolean checked) {
        if (checked) {
            startService();
            makeToast("Starting service");
        }
        else {
            stopService();
            makeToast("Stopping service");
        }
    }



    void startService() {
        intent = new Intent(MainActivity.this, EchoService.class);
        startService(intent);
    }

    void stopService() {

        if (intent != null) {
            stopService(intent);
        }

        intent = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        createReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }


    private void createReceiver() {
        receiver = new MainActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EchoService.ACTION_SEND_MESSAGE_ACTIVITY);
        registerReceiver(receiver, intentFilter);
    }

    void send() {
        String message = editName.getText().toString();

        Intent intent = new Intent();
        intent.setAction(ACTION_SEND_MESSAGE_SERVICE);
        intent.putExtra(KEY_MESSAGE_SERVICE, message);
        sendBroadcast(intent);
    }


    void addMessage(String string) {
        String str = messages.getText().toString() + "\n" + string;
        messages.setText(str);
    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }



    private class MainActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (EchoService.ACTION_SEND_MESSAGE_ACTIVITY.equals(action)) {
                String message = intent.getStringExtra(EchoService.KEY_MESSAGE_ACTIVITY);
                addMessage(String.valueOf(message));
            }
        }
    }
}
