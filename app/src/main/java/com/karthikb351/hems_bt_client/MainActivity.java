package com.karthikb351.hems_bt_client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karthikb351.hems_bt_client.adapters.DevicesAdapter;
import com.karthikb351.hems_bt_client.objects.Device;
import com.karthikb351.hems_bt_client.objects.DeviceHelper;
import com.karthikb351.hems_bt_client.objects.PlugHelper;

import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class MainActivity extends ActionBarActivity {

    BluetoothSPP bt;
    List <Device> currentDevices = new ArrayList<>();
    DevicesAdapter mAdapter;

    LinearLayout btnLayout;

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(MainActivity.this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {

            public void onDataReceived(byte[] data, String message) {
                Log.i("onDataReceived", "got data: " + message);
                String rfid_tag = "";
                String plug_tag = "";
                if(message.length()==15) {
                    plug_tag = message.substring(13);
                    rfid_tag = message.substring(0,12);

                    Device device = DeviceHelper.findDeviceByRfid(rfid_tag);
                    if(device==null)   {
                        Toast.makeText(MainActivity.this, "Unidentified tag: "+rfid_tag, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Adding device: "+device.getRfidTag(), Toast.LENGTH_SHORT).show();
                        int plug = PlugHelper.getPlugForTag(plug_tag);
                        for(Device d: currentDevices) {
                            if(d.getPlug()==plug)
                                currentDevices.remove(d);
                        }
                        device.setPlug(plug);
                        currentDevices.add(device);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Can't parse data: "+message, Toast.LENGTH_LONG).show();
                }
                update();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                btnLayout.setVisibility(View.VISIBLE);
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                btnLayout.setVisibility(View.GONE);
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = (Button)findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        btnLayout = (LinearLayout)findViewById(R.id.btnLayout);
        btnLayout.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.device_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        currentDevices = DeviceHelper.getPermanentDevices();

        mAdapter = new DevicesAdapter(getApplicationContext(), currentDevices) {
            @Override
            public void sendBTCommand(String cmd) {
                Log.d("Sending BT Command", "Command:"+cmd);
                bt.send(cmd, true);
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    public String nullSafe(String d)
    {
        if(d != null)
            return d;
        else
            return "null";

    }

    public void update() {
        mAdapter.setDevices(currentDevices);
        mAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void setup() {
        Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                bt.send("gathik", true);
            }
        });
        Button btnReset = (Button)findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                currentDevices=new ArrayList<Device>();
                update();
                Toast.makeText(MainActivity.this, "Cleared database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
