package com.example.barbara.trackkeeper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CON = 2;
    private static final int MESSAGE_READ = 3;
    private static final String ENDEREÇO_MAC = null;

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket meuSocket = null;
    BluetoothDevice meuDevice = null;

    Handler mHandler;
    ConnectedThread connectedThread;

    static Intent retorna = new Intent();

    boolean connection = false;

    UUID MYUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanButton = (Button) findViewById(R.id.botao);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(null);

                Toast.makeText(getApplicationContext(),"Procurando...",Toast.LENGTH_SHORT).show();
                doDiscovery();
            }
        });

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Don't forget to unregister during onDestroy

        BluetoothLostReceiver bluetoothLostReceiver = new BluetoothLostReceiver();
        filter = new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.registerReceiver(bluetoothLostReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){

                    Toast.makeText(getApplicationContext(),"Dispositivos pareados",Toast.LENGTH_SHORT).show();
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                    ArrayAdapter list = new ArrayAdapter(this, R.layout.device_name);
                    // If there are paired devices
                    if (pairedDevices.size() > 0) {
                        // Loop through paired devices
                        for (BluetoothDevice device : pairedDevices) {
                            // Add the name and address to an array adapter to show in a ListView
                            list.add(device.getName() + "\n" + device.getAddress());
                        }
                    }
                    ListView listView = (ListView) findViewById(R.id.list);
                    listView.setAdapter(list);
                    listView.setOnItemClickListener(mDeviceClickListener);

                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth desativado.\n App será encerrado",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_CON :
                if (resultCode == Activity.RESULT_OK){
                    //Obter endereço,
                    String MAC = data.getExtras().getString(ENDEREÇO_MAC);
                    //Toast.makeText(getApplicationContext(),"MAC final:"+ MAC,Toast.LENGTH_LONG).show();
                    meuDevice = mBluetoothAdapter.getRemoteDevice(MAC);

                    try {
                        meuSocket = meuDevice.createRfcommSocketToServiceRecord(MYUID);
                        meuSocket.connect();

                        connectedThread = new ConnectedThread(meuSocket);
                        connectedThread.start();


                        Toast.makeText(getApplicationContext(),"Conectado com Dispositivo Portátil",Toast.LENGTH_SHORT).show();
                        connection = true;

                        Intent wait = new Intent(MainActivity.this, Wait.class);
                        wait.putExtra("Status",connection);
                        startActivity(wait);

                    }catch (IOException erro){
                        connection = false;
                        // Unable to connect; close the socket and get out
                        try {
                            meuSocket.close();
                            connection = false;
                            Toast.makeText(getApplicationContext(),"Falha na conexão",Toast.LENGTH_SHORT).show();
                        } catch (IOException closeException) { }
                    }

                }else{
                    //erro em obter endereço
                    Toast.makeText(getApplicationContext(),"Falha em obter mac",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null)
            mBluetoothAdapter.cancelDiscovery();

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    private void doDiscovery() {
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    public void delay(final int c){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ArrayAdapter listDevice = new ArrayAdapter(context, R.layout.device_name);
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                listDevice.add(device.getName() + "\n" + device.getAddress());

            }
            ListView listViewD = (ListView) findViewById(R.id.list);
            listViewD.setAdapter(listDevice);
            listViewD.setOnItemClickListener(mDeviceClickListener);
        }
    };

    public class BluetoothLostReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {

            if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())&& connection==true)
            {
                connection = false;
                for (int i = 0; i <= 1; i++) {
                    Toast.makeText(getApplicationContext(), "Conexão Perdida", Toast.LENGTH_SHORT).show();
                    Vibrate();

                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    delay(1500);

                    if (i == 1) {
                        Intent wait = new Intent(MainActivity.this, Wait.class);
                        wait.putExtra("Status",connection);
                        startActivity(wait);
                    }
                }
            }
        }
    }




    private void Vibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }

    protected AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            String info = ((TextView) v).getText().toString();
            String endereço_mac = info.substring(info.length() - 17);

            retorna.putExtra(ENDEREÇO_MAC,endereço_mac);

            Toast.makeText(getApplicationContext(),"Aguarde conexão...",Toast.LENGTH_SHORT).show();
            Intent lista = new Intent(MainActivity.this,Conecta.class);
            startActivityForResult(lista, REQUEST_CON);

        }
    };

   private class ConnectedThread extends Thread {
       private final InputStream mmInStream;
       private final OutputStream mmOutStream;

       public ConnectedThread(BluetoothSocket socket) {

           InputStream tmpIn = null;
           OutputStream tmpOut = null;

           // Get the input and output streams, using temp objects because
           // member streams are final
           try {
               tmpIn = socket.getInputStream();
               tmpOut = socket.getOutputStream();
           } catch (IOException e) {
           }

           mmInStream = tmpIn;
           mmOutStream = tmpOut;
       }

       public void run() {
           byte[] buffer = new byte[1024];  // buffer store for the stream
           int bytes; // bytes returned from read()

           // Keep listening to the InputStream until an exception occurs
           while (true) {
               try {
                   // Read from the InputStream
                   bytes = mmInStream.read(buffer);
                   String trans = new String(buffer, 0, bytes);
                   Toast.makeText(getApplicationContext(), trans, Toast.LENGTH_LONG).show();
                   // Send the obtained bytes to the UI activity
                   mHandler.obtainMessage(MESSAGE_READ, bytes, -1, trans).sendToTarget();
               } catch (IOException e) {
                   break;
               }
           }
       }
   }
}

