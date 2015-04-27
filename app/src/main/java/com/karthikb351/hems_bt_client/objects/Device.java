package com.karthikb351.hems_bt_client.objects;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by karthikbalakrishnan on 08/03/15.
 */
public class Device {

    String name = "Device";
    String rfidTag = "";
    boolean status;
    String voltageRating = "1.2";
    String powerRating = "100";
    String currentRating = "1";

    int plug = 0;

    long startTime;
    long endTime;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
