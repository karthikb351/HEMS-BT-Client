package com.karthikb351.hems_bt_client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karthikb351.hems_bt_client.objects.Device;

import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class MainActivity extends ActionBarActivity {

    BluetoothSPP bt;
    Device currentDevice;
    LinearLayout btnLayout;
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
                Log.i("onDataReceived", "got message: " + message);
                List<Device> devices = Device.find(Device.class,"rfid_tag = ?", message);
                if(devices.isEmpty())   {
                    Device d = new Device();
                    d.setRfidTag(message);
                    d.save();
                    Toast.makeText(MainActivity.this, "new device: "+message, Toast.LENGTH_SHORT).show();
                    currentDevice=d;
                }
                else    {
                    for(Device d: devices) {
                        Toast.makeText(MainActivity.this, "old device: "+d.getRfidTag(), Toast.LENGTH_SHORT).show();
                        currentDevice=d;
                    }
                }
                update();
//
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
    }

    public String nullSafe(String d)
    {
        if(d != null)
            return d;
        else
            return "null";

    }

    public void update() {
        TextView tag, name, pow, vol, cur, status;
        tag = (TextView)findViewById(R.id.dTag);
        name = (TextView)findViewById(R.id.dName);
        pow = (TextView)findViewById(R.id.dPow);
        vol = (TextView)findViewById(R.id.dVolt);
        cur = (TextView)findViewById(R.id.dCur);
        if(currentDevice!=null)
        {
            tag.setText(nullSafe(currentDevice.getRfidTag()));
            name.setText(nullSafe(currentDevice.getName()));
            pow.setText(nullSafe(currentDevice.getPowerRating()));
            vol.setText(nullSafe(currentDevice.getVoltageRating()));
            cur.setText(nullSafe(currentDevice.getCurrentRating()));
        }
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

        ToggleButton btnToggle = (ToggleButton)findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Is the toggle on?
                boolean on = ((ToggleButton) v).isChecked();

                if (on) {
                    bt.send("Q", true);
                    TextView status = (TextView)findViewById(R.id.dStatus);
                    status.setText("Enabled");
                } else {
                    bt.send("W", true);
                    TextView status = (TextView)findViewById(R.id.dStatus);
                    status.setText("Disabled");
                }
            }
        });
        Button btnReset = (Button)findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Device.deleteAll(Device.class);
                Toast.makeText(MainActivity.this, "Cleared db", Toast.LENGTH_SHORT).show();
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
