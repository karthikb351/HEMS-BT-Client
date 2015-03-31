package com.karthikb351.hems_bt_client.objects;

import java.util.ArrayList;
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
        fan.setVoltageRating("1.2V");
        fan.setPowerRating("100W");
        fan.setCurrentRating("1A");
        fan.setPlug(PlugHelper.PLUG_FAN);

        Device light = new Device();
        light.setName("My device 1");
        light.setRfidTag("123233232");
        light.setVoltageRating("1.2V");
        light.setPowerRating("100W");
        light.setCurrentRating("1A");
        light.setPlug(PlugHelper.PLUG_LIGHT);


        devices.add(fan);
        devices.add(light);

        return devices;
    }

    public static List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();

        Device d1 = new Device();
        d1.setName("My device 1");
        d1.setRfidTag("123233232");
        d1.setVoltageRating("1.2V");
        d1.setPowerRating("100W");
        d1.setCurrentRating("1A");

        Device d2 = new Device();
        d2.setName("My device 1");
        d2.setRfidTag("123233232");
        d2.setVoltageRating("1.2V");
        d2.setPowerRating("100W");
        d2.setCurrentRating("1A");

        Device d3 = new Device();
        d3.setName("My device 1");
        d3.setRfidTag("123233232");
        d3.setVoltageRating("1.2V");
        d3.setPowerRating("100W");
        d3.setCurrentRating("1A");

        Device d4 = new Device();
        d4.setName("My device 1");
        d4.setRfidTag("123233232");
        d4.setVoltageRating("1.2V");
        d4.setPowerRating("100W");
        d4.setCurrentRating("1A");

        Device d5 = new Device();
        d5.setName("My device 1");
        d5.setRfidTag("123233232");
        d5.setVoltageRating("1.2V");
        d5.setPowerRating("100W");
        d5.setCurrentRating("1A");



        devices.add(d1);
        devices.add(d2);
        devices.add(d3);
        devices.add(d4);
        devices.add(d5);

        devices.addAll(getPermanentDevices());


        return devices;
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
