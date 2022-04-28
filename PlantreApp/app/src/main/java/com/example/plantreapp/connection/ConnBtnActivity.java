package com.example.plantreapp.connection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantreapp.R;
import com.example.plantreapp.entities.Plant;
import com.example.plantreapp.entities.PlantIdentity;
import com.example.plantreapp.myPlants.MyPlantsActivity;
import com.example.plantreapp.myPlants.SelectPlantActivity;
import com.example.plantreapp.repository.PlantIdentityRepository;
import com.example.plantreapp.repository.PlantRepository;
import com.example.plantreapp.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Handler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plantreapp.MainActivity;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


import android.os.Handler;
import android.widget.ProgressBar;

//import cz.msebera.android.httpclient.Header;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;


public class ConnBtnActivity extends AppCompatActivity implements WaterInfoAdapter.WaterInfoInterface {

    //Button ButtonConnectionPage;

    /*Button ButtonPump, ButtonPump2;
    ProgressBar circular_pro, circular_pro2;

    TextView status, status2;
    private int progressStatus;
    private Handler handler = new Handler();


    private final static String TAG = MainActivity.class.getSimpleName();

    //TextView textViewPrompt;
    public static boolean pumpOn, secondPumpOn;
    static final int UdpServerPORT = 4445;
    UdpServerThread udpServerThread;
    boolean udpConnected = false;*/

    public static boolean pumpOn, secondPumpOn;

    private int soilMoisture = 0;
    private int soilMoisture2 = 0;
    TextView test;

    boolean justStarted = false;

    private Handler handler = new Handler();

    private final static String TAG = ConnBtnActivity.class.getSimpleName();
    UdpServerThread udpServerThread;
    static final int UdpServerPORT = 4445;
    boolean udpConnected = false;
    InetAddress savedAddress = null;
    int savedPort = 0;
    TextView tv;

    //Declare Recyclerview , Adapter and ArrayList
    private RecyclerView recyclerView;
    private WaterInfoAdapter adapter;
    private ArrayList<WaterInfo> waterInfoArrayList;
    private WaterInfo wInfo;
    private boolean firstSensorReceiving;
    private boolean secondSensorReceiving;
    private PlantIdentityRepository plantIdentityRepository; // Could be replaced with a view model
    private PlantRepository plantRepository;
    private String firstWaterPumpUrl;
    private String secondWaterPumpUrl;
    private String lightUrl;
    private boolean lightOn;

    private Button lights;

    //1st recycler view in the list
    int minMoisture1 = 0;
    String wateringMethod1 = "";
    int timer1 = 0;

    boolean infoExists;
    //2nd recyler view in the list
    int minMoisture2 = 0;
    String wateringMethod2 = "";
    int timer2 = 0;

    int wTime1;
    int wTime2;

    //boolean wateringMethod1Exists;
    //boolean wateringMethod2Exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conn_btn);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home_item);

        wInfo = new WaterInfo(10,"buttonName","test", "");
        // init recycler view
        initView();

        infoExists = false;
        boolean wateringMethod1Exists = false;
        boolean wateringMethod2Exists = false;
        lightOn = false;
        firstSensorReceiving = false;
        secondSensorReceiving = false;
        //tv = (TextView) findViewById(R.id.testText);

        // Setup repository
        // plantIdentityRepository = new PlantIdentityRepository(getApplicationContext());
        plantRepository = new PlantRepository(getApplicationContext());
        firstWaterPumpUrl = "http://blynk-cloud.com/ihbYhRnEL8H3lw84v8fyU-CPtH-BJs00/update/V1?value=1";
        secondWaterPumpUrl = "http://blynk-cloud.com/ihbYhRnEL8H3lw84v8fyU-CPtH-BJs00/update/V2?value=1";
        lightUrl = "http://blynk-cloud.com/ihbYhRnEL8H3lw84v8fyU-CPtH-BJs00/update/v3?value=1";
        lights = (Button) findViewById(R.id.myUVLight);

        wTime1 = 10;
        wTime2 = 10;
        // nav click handler
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_item:
                        //startActivity(new Intent(getApplicationContext(), ConnBtnActivity.class));
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


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(savedAddress != null)
                    {
                        try {
                            if(savedAddress.isReachable(savedPort))
                            {
                                //test.setText("Got it");
                                udpConnected = true;
                            }
                            else
                            {
                                //test.setText("Went out of Home Network");
                                udpConnected = false;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            udpConnected = false;
                        }
                    }
                    else
                    {
                        //test.setText("Not in Home Network");
                        //waterInfoArrayList.get(0).setPlantText("Not in Home Network");
                        udpConnected = false;
                    }
                }

            }
        }).start();


        receiveData();

        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (udpConnected)
                {
                    lightOn = true;
                }
                else
                {
                    turnOnLightViaCloud(lightUrl);
                }
                //lights.setText("Lights Off");

            }
        });


    }
    public void updateWaterList() {
        WaterInfo w1 = waterInfoArrayList.get(0);
        WaterInfo w2 = waterInfoArrayList.get(1);

        // First Water Pump
        plantRepository.findByPosition(0, new Continuation<Plant>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void resumeWith(@NonNull Object o) {
                if (o != null) {
                    Plant plant = (Plant) o;
                    String s = showInfo(plant, 0);
                    if(s.length() > 3)
                    {
                        infoExists = true;
                    }
                    else
                    {
                        infoExists = false;
                    }
                    w1.setPlantText(s);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // First Water Pump
        plantRepository.findByPosition(1, new Continuation<Plant>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void resumeWith(@NonNull Object o) {
                if (o != null) {
                    Plant plant = (Plant) o;
                    String s = showInfo(plant, 1);
                    if(s.length() > 3)
                    {
                        infoExists = true;
                    }
                    else
                    {
                        infoExists = false;
                    }
                    w2.setPlantText(s);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    public String showInfo(Plant plant, int pos)
    {
        String pInfo = "";
        String wateringPath = "";

        String[] seed = new String[3];
        float[] minMoisture = new float[3];
        int[] minWaterTimer = new int[3];
        seed[0] = "Seed";
        seed[1] = "Seedling";
        seed[2] = "Mature";

        minMoisture[0] = plant.getMin_seed_moisture();
        minMoisture[1] = plant.getMin_seedling_moisture();
        minMoisture[2] = plant.getMin_mature_moisture();

        minWaterTimer[0] = plant.getSeed_water_rate();
        minWaterTimer[1] = plant.getSeedling_water_rate();
        minWaterTimer[2] = plant.getMature_water_rate();



        for (int i = 0; i < 3; i++)
        {

            if(plant.getStage().equals(seed[i])) {
                //infoExists = true;
                if (minMoisture[i] != 0f) {
                    wateringPath = "\nWatering Method: Moisture Rate";
                    pInfo = "Name: " + plant.getName() + "\nStage:" + plant.getStage() + wateringPath + "\nMin Moisture Rate:" + minMoisture[i]+ "\nWatering Duration:" + plant.getWater_running_time();
                    if(pos == 0)
                    {
                        minMoisture1 = (int)minMoisture[i];
                        wateringMethod1 = "Moisture1";
                        //pInfo = pInfo + "Watering Duration:" + plant.getWater_running_time();
                        wTime1 = plant.getWater_running_time();
                        //wateringMethod1Exists = true;
                    }
                    if(pos == 1)
                    {
                        minMoisture2 = plant.getMin_seed_moisture().intValue();
                        wateringMethod2 = "Moisture2";
                        wTime1 = plant.getWater_running_time();
                        //wateringMethod2Exists = true;
                    }

                }
                if (minWaterTimer[i] != 0)
                {
                    wateringPath = "\nWatering Method: Timer";
                    pInfo = "Name: " + plant.getName() + "\nStage:" + plant.getStage() + wateringPath + "\nWatering Hour:" + minWaterTimer[i]+ "\nWatering Duration:" + plant.getWater_running_time();

                    if(pos == 0)
                    {
                        timer1 = (int)minWaterTimer[i];
                        wateringMethod1 = "Timer1";
                        wTime2 = plant.getWater_running_time();
                       // wateringMethod1Exists = true;
                    }
                    if(pos == 1)
                    {
                        timer2 = (int)minWaterTimer[i];
                        wateringMethod2 = "Timer2";
                        wTime2 = plant.getWater_running_time();
                        //wateringMethod2Exists = true;
                    }

                }

            }

        }

        return pInfo;
    }

    @Override
    protected void onStart() {

       udpServerThread = new UdpServerThread(UdpServerPORT);
       udpServerThread.start();
       udpServerThread.setPgStopped(false);
       //udpServerThread.getRunning();

        updateWaterList();
        super.onStart();

    }
    @Override
    protected void onStop() {
        if(udpServerThread != null){
            udpServerThread.setPgStopped(true);
            udpServerThread.setRunning(false);
            udpServerThread = null;
        }

        super.onStop();
    }

/*    @Override
    protected void onPause() {
        if(udpServerThread != null){
            udpServerThread.setRunning(false);
            udpServerThread = null;
        }

        super.onPause();
    }*/

    /*private void updatePrompt(final String prompt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewPrompt.append(prompt);
            }
        });
    }*/

    private class UdpServerThread extends Thread{

        int serverPort;
        DatagramSocket socket;

        boolean running;
        boolean pageStopped;

        public UdpServerThread(int serverPort) {
            super();
            this.serverPort = serverPort;
            this.pageStopped = false;
        }

        public void setRunning(boolean running){
            this.running = running;
        }

        public boolean getRunning(){ return this.running; }

        public void setPgStopped(boolean pageStopped){
            this.pageStopped = pageStopped;
        }

        @Override
        public void run() {

                running = true;
                justStarted = true;
                try {
                    socket = new DatagramSocket(serverPort);
                    Log.e(TAG, "UDP Server is running");

                    while(running){
                        byte[] buf = new byte[256];
                        byte[] buf1 = new byte[256];

                        // receive request
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);     //this code block the program flow

                        //tv.setText("Came this far");
                        InetAddress address = packet.getAddress();
                        int port = packet.getPort();
                        savedAddress = address;
                        savedPort = port;


                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        String[] allValues = msg.split(",");
                        int l = Integer.valueOf(allValues[0]);
                        int m = Integer.valueOf(allValues[1]);
                        //wInfo.setPercentage(soilMoisture);
                        //wInfo.setText(String.valueOf(soilMoisture));
                        //adapter.notifyDataSetChanged();
                        if(soilMoisture <0 )
                        {
                            soilMoisture = 0;
                        }
                        else if(soilMoisture > 100)
                        {
                            soilMoisture = 100;
                        }
                        else
                        {
                            soilMoisture = l;
                        }

                        //wInfo.setPercentage(soilMoisture2);
                        //wInfo.setText(String.valueOf(soilMoisture2));
                        //adapter.notifyDataSetChanged();
                        if(soilMoisture2 <0 )
                        {
                            soilMoisture2 = 0;
                        }
                        else if(soilMoisture2 > 100)
                        {
                            soilMoisture2 = 100;
                        }
                        else
                        {
                            soilMoisture2 = m;
                        }

                        //int li = ByteBuffer.wrap(packet.getData()).getInt();

                        //String m = String.valueOf(li);
                        // send the response to the client at "address" and "port"


                        //updatePrompt("Request from: " + address + ":" + port + "\n");
                        //updatePrompt("Message: "+ soilMoisture +"\n");



                        if(pumpOn == true)
                        {
                            String dString = "5";
                            buf = dString.getBytes();
                            packet = new DatagramPacket(buf, buf.length, address, port);
                            socket.send(packet);
                            pumpOn = false;

                        }

                        if(secondPumpOn == true)
                        {
                            String dString = "4";
                            buf = dString.getBytes();
                            packet = new DatagramPacket(buf, buf.length, address, port);
                            socket.send(packet);
                            secondPumpOn = false;
                        }

                        if(lightOn)
                        {
                            String dString = "6";
                            buf = dString.getBytes();
                            packet = new DatagramPacket(buf, buf.length, address, port);
                            socket.send(packet);
                            lightOn = false;
                        }

                        if(justStarted && infoExists)
                        {
                            int timeOrMoisture1 = 0;
                            int timeOrMoisture2 = 0;

                            int sTest1 = wTime1;
                            int sTest2 = wTime2;
                            if(wateringMethod1.equals("Moisture1"))
                            {
                                timeOrMoisture1 = minMoisture1;
                            }
                            else if(wateringMethod1.equals("Timer1"))
                            {
                                timeOrMoisture1 = timer1;
                            }
                            if(wateringMethod2.equals("Moisture2"))
                            {
                                timeOrMoisture2 = minMoisture2;
                            }
                            else if(wateringMethod2.equals("Timer2"))
                            {
                                timeOrMoisture2 = timer2;
                            }
                            String dString = wateringMethod1+","+ Integer.toString(timeOrMoisture1) + "," +Integer.toString(sTest1 ) +"\n" + wateringMethod2 + "," +Integer.toString(timeOrMoisture2) +"," +Integer.toString(sTest2);
                            /*if(!wateringMethod1Exists)
                            {
                                dString = "M1,0,20\n" + wateringMethod2 + "," +Integer.toString(timeOrMoisture2) +"," +Integer.toString(sTest2);
                            }
                            if(!wateringMethod2Exists)
                            {
                                dString = wateringMethod1+","+ Integer.toString(timeOrMoisture1) + "," +Integer.toString(sTest1 ) +"\nM2,0,20";
                            }*/
                            Log.e(TAG, dString);
                            buf1 = dString.getBytes();
                            packet = new DatagramPacket(buf1, buf1.length, address, port);
                            socket.send(packet);
                            buf1 = dString.getBytes();
                            packet = new DatagramPacket(buf1, buf1.length, address, port);
                            socket.send(packet);
                            justStarted = false;
                            //buf1

                        }

                    }

                    Log.e(TAG, "UDP Server ended");

                } catch (SocketException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception 1");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception 2");
                } finally {
                    if(socket != null){
                        socket.close();
                        Log.e(TAG, "socket.close()");
                    }
                }
                if(!pageStopped)
                {
                    threadRestart();
                    Log.e(TAG, "Thread Restarting");
                }

        }

    }

    public void threadRestart()
    {
        udpServerThread = new UdpServerThread(UdpServerPORT);
        udpServerThread.start();
        udpServerThread.setPgStopped(false);
        //udpServerThread.getRunning();
        updateWaterList();
    }
    private void initView() {
        // Initialize RecyclerView and set Adapter
        recyclerView = findViewById(R.id.connBtnRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        waterInfoArrayList = new ArrayList<>();
        adapter = new WaterInfoAdapter(this, waterInfoArrayList, this);
        recyclerView.setAdapter(adapter);
        initList(2);
    }

    private void initList(int numItems) {
        for (int i = 0; i < numItems; i++) {
            //data to be shown in list
            waterInfoArrayList.add(new WaterInfo(0, "Water Plant", "Percentage", ""));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onWaterBtnClick(int pos, ArrayList<WaterInfo> w)
    {
        if(udpConnected == true)
        {
            if(pos == 0)
            {
                pumpOn = true;
            }
            else if(pos == 1)
            {
                secondPumpOn = true;
            }
            //testbtn.setText("Watering via UDP");
        }
        else
        {
            if(pos == 0)
            {
                turnOnWaterPumpViaCloud(firstWaterPumpUrl);
            }
            else if(pos == 1)
            {
                turnOnWaterPumpViaCloud(secondWaterPumpUrl);
            }
            //status2.setText("UDP not Connected");
            //httpClient.newCall(request2);
            //testbtn.setText("Watering via Cloud");

        }
    }


    private void turnOnWaterPumpViaCloud(String url)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //
            }
        });
    }

    private void turnOnLightViaCloud(String url)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //
            }
        });
    }

    public void receiveData()
    {
        WaterInfo w1 = waterInfoArrayList.get(0);
        WaterInfo w2 = waterInfoArrayList.get(1);
        updateWaterList();

/*
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            String pName = extras.getString("plantName");
            int position = extras.getInt("position");

            if(position == 0)
            {
                w1.setPlantText(pName);
            }
            if(position == 1)
            {
                w2.setPlantText(pName);
            }

        }
        else
        {
            w1.setPlantText("No Name Found");
            w2.setPlantText("No Name Found");
        }*/


        //int l = waterInfoArrayList.size();
        //w1.setText(String.valueOf(l));
        adapter.notifyDataSetChanged();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            setProg(soilMoisture, w1);
                            adapter.notifyDataSetChanged();
                            setProg(soilMoisture2, w2);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    try {
                        Thread.sleep(200);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
    @Override
    public void onSelectPlantClick(int position) {
        Intent intent = new Intent(ConnBtnActivity.this, SelectPlantActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        return;
    }

    private void setProg(int sMoisture, WaterInfo w)
    {
        w.setPercentage(sMoisture);
        if(sMoisture <0 )
        {
            sMoisture = 0;
        }
        if(sMoisture > 100)
        {
            sMoisture = 100;
        }
        w.setText(sMoisture+"%");
    }
}