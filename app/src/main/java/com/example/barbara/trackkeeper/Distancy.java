package com.example.barbara.trackkeeper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Distancy extends AppCompatActivity {

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    double receb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distancy);

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        Button boton = (Button) findViewById(R.id.button1);
        boton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                doDiscovery();
                //v.setVisibility(View.GONE);
            }

        });



    }

    private void doDiscovery() {
        // If we're already discovering, stop it
        if (BTAdapter.isDiscovering()) {
            BTAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        BTAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                double rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                double Pw = (Math.pow(10,(rssi/10))/1000);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                TextView rssi_msg = (TextView) findViewById(R.id.distan);
                rssi_msg.setText(rssi_msg.getText() + name + " => " + rssi + "dBm\n");
                rssi_msg.setText(rssi_msg.getText() + " => " + Pw + "W\n\n");
               // Toast.makeText(getApplicationContext(),rssi+"dBm",Toast.LENGTH_LONG).show();
            }
        }
    };




}
