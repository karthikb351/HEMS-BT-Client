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
        fan.setName("Fan");
        fan.setRfidTag("123233232");
        fan.setVoltageRating("240");
        fan.setPowerRating("75");
        fan.setCurrentRating("0.31");
        fan.setPlug(PlugHelper.PLUG_FAN);

        Device light = new Device();
        light.setName("Light");
        light.setRfidTag("123233232");
        light.setVoltageRating("240");
        light.setPowerRating("60");
        light.setCurrentRating("0.25");
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
        d1.setCurrentRating("4.17");

        Device d2 = new Device();
        d2.setName("Induction Stove");
        d2.setRfidTag("0C00473FBFCB");
        d2.setVoltageRating("240");
        d2.setPowerRating("2000");
        d2.setCurrentRating("8.34");

        Device d3 = new Device();
        d3.setName("Microwave Owen");
        d3.setRfidTag("0C004793B961");
        d3.setVoltageRating("240");
        d3.setPowerRating("1200");
        d3.setCurrentRating("5");

        Device d4 = new Device();
        d4.setName("Heater");
        d4.setRfidTag("0C004B5B627E");
        d4.setVoltageRating("240");
        d4.setPowerRating("3000");
        d4.setCurrentRating("12.5");

        Device d5 = new Device();
        d5.setName("Rice Cooker");
        d5.setRfidTag("0C004794D807");
        d5.setVoltageRating("240");
        d5.setPowerRating("650");
        d5.setCurrentRating("2.7");

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
