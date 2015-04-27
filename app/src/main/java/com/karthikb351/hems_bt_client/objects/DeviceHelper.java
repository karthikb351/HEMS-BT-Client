package com.karthikb351.hems_bt_client.objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by karthikbalakrishnan on 31/03/15.
 */
public class DeviceHelper {

    public static List<Device> getPermanentDevices() {
        List<Device> devices = new ArrayList<>();

        Device fan = new Device();
        fan.setName("My device 1");
        fan.setRfidTag("123233232");
        fan.setVoltageRating("1.2");
        fan.setPowerRating("100");
        fan.setCurrentRating("1");
        fan.setPlug(PlugHelper.PLUG_FAN);

        Device light = new Device();
        light.setName("My device 1");
        light.setRfidTag("123233232");
        light.setVoltageRating("1.2");
        light.setPowerRating("100");
        light.setCurrentRating("1");
        light.setPlug(PlugHelper.PLUG_LIGHT);


        devices.add(fan);
        devices.add(light);

        return devices;
    }

    public static List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();

        Device d1 = new Device();
        d1.setName("Iron");
        d1.setRfidTag("0C0045D2C65D");
        d1.setVoltageRating("240");
        d1.setPowerRating("1000");
        d1.setCurrentRating("2.5");

        Device d2 = new Device();
        d2.setName("My device 2");
        d2.setRfidTag("123233232");
        d2.setVoltageRating("1.2");
        d2.setPowerRating("100");
        d2.setCurrentRating("1");

        Device d3 = new Device();
        d3.setName("My device 3");
        d3.setRfidTag("123233232");
        d3.setVoltageRating("1.2");
        d3.setPowerRating("100");
        d3.setCurrentRating("1");

        Device d4 = new Device();
        d4.setName("My device 4");
        d4.setRfidTag("123233232");
        d4.setVoltageRating("1.2");
        d4.setPowerRating("100");
        d4.setCurrentRating("1");

        Device d5 = new Device();
        d5.setName("My device 5");
        d5.setRfidTag("123233232");
        d5.setVoltageRating("1.2");
        d5.setPowerRating("100");
        d5.setCurrentRating("1");


        devices.add(d1);
        devices.add(d2);
        devices.add(d3);
        devices.add(d4);
        devices.add(d5);

        devices.addAll(getPermanentDevices());


        return devices;
    }

    public static double getTimeDifference(Device d) {
        double time = 0.0;
        if(d.getEndTime()>=d.getStartTime())
            time = time + (d.getEndTime()-d.getStartTime())/(1000*60*60.0);
        else
            time = time + (Calendar.getInstance().getTimeInMillis()-d.getStartTime())/(1000*60*60.0);
        Log.i("TimeDiff", "Is :"+String.valueOf(time));
        return time;
    }

    public static double generateBill(Device d) {
        double cost = 0;
        cost = (Double.parseDouble(d.getPowerRating())/1000.0)*getTimeDifference(d)*5.0;  // TODO: Arbitrary tariff
        return cost;
    }

    public static Device findDeviceByRfid(String rfid) {
        List<Device> devices = getDevices();
        Device result = null;
        for(Device d: devices) {
            if(d.getRfidTag().equals(rfid)) {
                result=d;
                break;
            }
        }
        return result;
    }
}
