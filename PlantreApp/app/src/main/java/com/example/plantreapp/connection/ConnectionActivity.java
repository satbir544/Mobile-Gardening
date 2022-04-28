package com.example.plantreapp.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.plantreapp.R;
import com.example.plantreapp.myPlants.MyPlantsActivity;
import com.example.plantreapp.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;
import java.util.UUID;

public class ConnectionActivity extends AppCompatActivity {

    private Button ButtonActive, ButtonInactive, ButtonListPairedDevices;
    private TextView textView;
    private TextView connectStat;
    //private TextView msg_box;
    BluetoothAdapter bluetoothAdapter;
    private ListView emp;
    private String NAME;
    private String[] permissions = {"android.permission.BLUETOOTH_CONNECT"};
    private boolean permissionGranted = false;
    private EditText textMessage;

    String error = "";
    String address = "";
    String receivedMsg = "";
    BluetoothDevice[] BTArray;

    private int port = 4445;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    static final int STATE_CONNECTION_TEST = 6;
    ArrayList<String> devices;
    ArrayAdapter arrayAdapter;
    SendReceive sendReceive;
    //public static final String TAG = "BTTag";

    private static final String APP_NAME = "Plantre";
    private UUID MY_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.connection_item);

        // nav click handler
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_item:
                        startActivity(new Intent(getApplicationContext(), ConnBtnActivity.class));
                        return true;
                    case R.id.my_plants_item:
                        startActivity(new Intent(getApplicationContext(), MyPlantsActivity.class));
                        return true;
                    case R.id.search_item:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        return true;
                    case R.id.connection_item:
                        startActivity(new Intent(getApplicationContext(), ConnectionActivity.class));
                        return true;
                }
                return false;
            }
        });

        ButtonActive = (Button) findViewById(R.id.onB);
        ButtonInactive = (Button) findViewById(R.id.offB);
        ButtonListPairedDevices = (Button) findViewById(R.id.listDevices);

        textView = (TextView) findViewById(R.id.stat);
        connectStat = (TextView) findViewById(R.id.connectionStatus);
        ButtonInactive.setEnabled(false);
        ButtonListPairedDevices.setEnabled(false);
        emp = (ListView) findViewById(R.id.pairedDeviceList);
        //msg_box = (TextView)findViewById(R.id.messageBox);
        //permissions[0] = "android.permission.BLUETOOTH_CONNECT";

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            permissionRequestCheck();

        }

        ButtonActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    checkPButtonActive();

                } else {
                    textView.setText("Active");
                }
                ButtonInactive.setEnabled(true);
                ButtonListPairedDevices.setEnabled(true);
            }
        });

        ButtonInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPButtonInactive();
                ButtonInactive.setEnabled(false);
                ButtonListPairedDevices.setEnabled(false);

                try{
                    devices.clear();
                    arrayAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    //do sth here
                }

                Toast.makeText(getApplicationContext(), "got Button Inactive", Toast.LENGTH_SHORT).show();
            }
        });

        ButtonListPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_paired_Devices();
            }
        });


        emp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass = new ClientClass(BTArray[i]);
                clientClass.start();
                if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) { }

                address = BTArray[i].getAddress().toString();
                ParcelUuid[] c =  BTArray[i].getUuids();
                MY_UUID = UUID.fromString(c[0].toString());
                connectStat.setText(BTArray[i].getName() + " "+ BTArray[i].getAddress()+"\n"+ MY_UUID.toString() +" Connecting as Client");
            }
        });

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case STATE_LISTENING:
                    connectStat.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    connectStat.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    connectStat.setText("Connected");
                    showCredDialog();
                    break;
                case STATE_CONNECTION_FAILED:
                    connectStat.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[])msg.obj;
                    String tempMsg = new String(readBuff,0,msg.arg1);
                    if(Integer.valueOf(tempMsg) == 1)
                    {
                        showConnectedDialog("Invalid Wifi Credentials Given");
                    }
                    if(Integer.valueOf(tempMsg) == 0)
                    {
                        showConnectedDialog("Plantre Device is Successfully Connected to Wifi!!!");
                    }
                    break;
                case STATE_CONNECTION_TEST:
                    connectStat.setText(error);
                    break;
            }
            return true;
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this,"Download Code", Toast.LENGTH_SHORT).show();

                permissionGranted = true;
            } else {
                permissionGranted = false;
                Toast.makeText(this, "Download Cancel", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showCredDialog()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(ConnectionActivity.this);
        myDialog.setTitle("Wifi Credentials");
        //final TextView ssidText = new TextView(ConnectionActivity.this);
        //final EditText ssid = new EditText(ConnectionActivity.this);
        final View credLayout = getLayoutInflater().inflate(R.layout.credential_dialog,null);
        //ssidText.setText("Wifi Name");

        myDialog.setView(credLayout);

        myDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText ssid = credLayout.findViewById(R.id.wifiSsid);
                EditText pass = credLayout.findViewById(R.id.wifiPass);
                String[] arrOfStr = getIpAddress().split(",");
                String string = ssid.getText().toString() + "\n" + pass.getText().toString() + "\n" + arrOfStr[0] + "\n" + String.valueOf(port);

                //sendReceive.write(string.getBytes());
                //Toast.makeText(ConnectionActivity.this, "Wifi is:" + string, Toast.LENGTH_LONG).show();
                sendReceive.write(string.getBytes());
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
        });
        AlertDialog dialog = myDialog.create();
        dialog.show();
    }

    private void showConnectedDialog(String title)
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(ConnectionActivity.this);
        TextView tv = new TextView(ConnectionActivity.this);
        myDialog.setTitle(title);
        myDialog.setCancelable(true);
        myDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        myDialog.show();
    }

    private void permissionRequestCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 80);
        }
    }

    private void toastTest(String text) {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void list_paired_Devices() {

        if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            try {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                devices = new ArrayList<>();

                int index = 0;
                BTArray = new BluetoothDevice[pairedDevices.size()];

                for (BluetoothDevice bt : pairedDevices) {
                    BTArray[index] = bt;
                    devices.add(bt.getName() + "\n" + bt.getAddress());
                    index++;
                }


                arrayAdapter = new ArrayAdapter(ConnectionActivity.this, R.layout.rows, devices);
                emp.setAdapter(arrayAdapter);


            } catch (Exception e) {
                connectStat.setText(e.toString());
            }
            Toast.makeText(getApplicationContext(), "got paired Devices", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkPButtonActive() {
        Intent ak = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(ak, 0);
            //Toast.makeText(ConnectionActivity.this,"got Button Permission", Toast.LENGTH_SHORT).show();

            textView.setText("Active");

            if (!bluetoothAdapter.isEnabled()) {

                Toast.makeText(ConnectionActivity.this, "Bluetooth not Enabled", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkPButtonInactive() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            permissionRequestCheck();
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.disable();
            textView.setText("Inactive");
        }
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress() +",";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    private class ServerClass extends Thread {
        private BluetoothServerSocket serverSocket;

        public ServerClass() {
            try {
                if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                }
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
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
                    //write code for send/ Receive
                    break;
                }
            }
        }

    }

    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;
        public boolean connected = true;

        public ClientClass(BluetoothDevice device1) {
            device = device1;
            try {
                if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) { }
                socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        public void run() {
            try {
                if (ActivityCompat.checkSelfPermission(ConnectionActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) { }
                socket.connect();
                if(socket.isConnected())
                {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendReceive=new SendReceive(socket);
                    sendReceive.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
                error = "Couldn't Connect to Device, Try Again!";
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_TEST;
                handler.sendMessage(message);
            }
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket)
        {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tempIn;
            outputStream = tempOut;

        }

        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while(true)
            {
                try {
                    bytes = inputStream.read(buffer);
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


