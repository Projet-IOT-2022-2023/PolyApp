package com.example.polyapp.ui.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.polyapp.R;
import com.example.polyapp.edt.DBManager;
import com.example.polyapp.edt.UserManager;
import com.example.polyapp.ui.friends.qrscanner.QRCodeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    // Declaration of all the objects necessary for this activity
    private Button mButtonlisten;
    private Button mButtongetvisible;
    private Button mButtonsend;
    private TextView mTextreceiveddisplayed;
    private TextView mTextstatus;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice[] btArray;
    private ListView mListdevices;
    private ArrayAdapter<String> mArrayAdapter;
    private IntentFilter filter;

    private DBManager m_db;
    private UserManager users;

    SendReceive sendReceive;

    int REQUEST_ENABLE_BLUETOOTH = 1;
    int REQUEST_ENABLE_BLUETOOTH_SCAN = 2;
    int ACTION_REQUEST_DISCOVERABLE = 3;

    // Declaration of all the constant necessary
    private static final UUID MY_UUID = UUID.fromString("8b8eb972-ac3e-11eb-8529-0242ac130003");

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    //call at the creation of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Init database
        m_db = new DBManager(this);
        m_db.open();
        users = new UserManager(m_db);
        // Init of all the button, text and the list
        mButtonlisten = (Button) findViewById(R.id.buttonlisten);
        mButtongetvisible = (Button) findViewById(R.id.buttongetvisible);
        mButtonsend = (Button) findViewById(R.id.buttonsend);
        mTextreceiveddisplayed = (TextView) findViewById(R.id.textreceiveddisplayed);
        mTextstatus = (TextView) findViewById(R.id.textstatus);
        mListdevices = (ListView) findViewById(R.id.listdevices);
        // Init BluetoothAdapter and ArrayAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        //Init the device as a server instance
        ServerClass serverClass = new ServerClass();
        serverClass.start();

        //Search if the bluetooth is available for the device and activate this one
        if (mBluetoothAdapter == null) {
            // affichage de courte durée
            Toast.makeText(getApplicationContext(), "Votre appareil ne prend pas en charge le Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_ENABLE_BLUETOOTH);
            }
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);

        }

        //When the user click on listen, we find all the devices in proximity that is paired with the device
        mButtonlisten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check the permission for bluetooth
                if (ActivityCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_ENABLE_BLUETOOTH);
                }

                if (!mArrayAdapter.isEmpty()) mArrayAdapter.clear();
                Set<BluetoothDevice> bt = mBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                btArray = new BluetoothDevice[bt.size()];
                int index = 0;

                if (bt.size() > 0) {
                    for (BluetoothDevice device : bt) {
                        btArray[index] = device;
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();
                        strings[index] = deviceName + "\n" + deviceHardwareAddress;
                        index++;
                    }
                    mArrayAdapter.addAll(strings);
                    mListdevices.setAdapter(mArrayAdapter);
                }
            }
        });

        //when the user click on get visible the device will become visible during a period of time
        mButtongetvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDiscoverable();
            }
        });

        //when the user click on a detected device in the list (after clicking on listen) a connection will be established
        mListdevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass = new ClientClass(btArray[i]);
                clientClass.start();

                mTextstatus.setText("Connecting");
            }
        });

        //when the user click on send data from the database will be transferred to the other device connected
        mButtonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] usersByte = null;

                usersByte = users.usersToBytes();

                sendReceive.write(usersByte);
                BluetoothActivity.super.onBackPressed();

            }
        });

    }

    //set the status of connection
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case STATE_LISTENING:
                    mTextstatus.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    mTextstatus.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    mTextstatus.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    mTextstatus.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;

                    if( users.addUsersWithByteArray(readBuff) == 0){
                        mTextreceiveddisplayed.setText("Nouvel appareil synchronisé");
                    }else{
                        mTextreceiveddisplayed.setText("Erreur lors du parsing");
                    }


                    break;
            }
            return true;
        }
    });

    //Make the device discoverable through system settings
    private void makeDiscoverable() {
        Intent discoverableIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 180);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BluetoothActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_ADVERTISE},
                    ACTION_REQUEST_DISCOVERABLE);
        }
        startActivity(discoverableIntent);
    }

    //Creation of a BroadcastReceiver for ACTION_FOUND
    public final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // if a device is discoverable
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // get the BluetoothDevice object and its information
                // from the intent
                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_ENABLE_BLUETOOTH);
                }
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                mArrayAdapter.add(deviceName + "\n" + deviceHardwareAddress);
                mListdevices.setAdapter(mArrayAdapter);
            }
        }
    };

    //function call when the activity is destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BluetoothActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    REQUEST_ENABLE_BLUETOOTH_SCAN);
        }

        //unregisterReceiver(receiver);
    }

    //class that manage the server instance
    private class ServerClass extends Thread {
        private BluetoothServerSocket serverSocket;

        public ServerClass() {
            try {
                if (ActivityCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_ENABLE_BLUETOOTH);
                }
                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Android_project", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            BluetoothSocket socket = null;

            while (socket == null) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if (socket != null) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive = new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    //class that manage the client instance
    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1) {
            device = device1;

            try {
                if (ActivityCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_ENABLE_BLUETOOTH);
                }
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                if (ActivityCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_ENABLE_BLUETOOTH);
                }
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    //class that manage the data between the server and the client
    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}