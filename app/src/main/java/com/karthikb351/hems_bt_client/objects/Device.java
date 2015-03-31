package com.karthikb351.hems_bt_client.objects;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by karthikbalakrishnan on 08/03/15.
 */
public class Device {

    String name = "Device";
    String rfidTag;
    boolean status;
    String voltageRating = "1.2V";
    String powerRating = "100W";
    String currentRating = "1A";

    int plug = 0;

    String startTime;

    public int getPlug() {
        return plug;
    }

    public void setPlug(int plug) {
        this.plug = plug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getVoltageRating() {
        return voltageRating;
    }

    public void setVoltageRating(String voltageRating) {
        this.voltageRating = voltageRating;
    }

    public String getPowerRating() {
        return powerRating;
    }

    public void setPowerRating(String powerRating) {
        this.powerRating = powerRating;
    }

    public String getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(String currentRating) {
        this.currentRating = currentRating;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
