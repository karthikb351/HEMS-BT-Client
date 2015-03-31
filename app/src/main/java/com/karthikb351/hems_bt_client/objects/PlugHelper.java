package com.karthikb351.hems_bt_client.objects;

import android.util.Log;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class PlugHelper {

    public final static int PLUG_P1 = 1;
    public final static int PLUG_P2 = 2;
    public final static int PLUG_P3 = 3;
    public final static int PLUG_LIGHT = 4;
    public final static int PLUG_FAN = 5;




    public static String getOnCommandForPlug(int plugNo) {
        switch (plugNo) {
            case PLUG_P1:
                return "Q";

            case PLUG_P2:
                return "E";

            case PLUG_P3:
                return "T";

            case PLUG_LIGHT:
                return "A";

            case PLUG_FAN:
                return "D";
        }
        return "";
    }

    public static String getOffCommandForPlug(int plugNo) {
        switch (plugNo) {
            case PLUG_P1:
                return "W";

            case PLUG_P2:
                return "R";

            case PLUG_P3:
                return "Y";

            case PLUG_LIGHT:
                return "S";

            case PLUG_FAN:
                return "F";
        }
        return "";
    }

    public static int getPlugForTag(String tag) {
        int plug = -1;
        switch (tag) {
            case "P1":
                plug = PLUG_P1;
                break;
            case "P2":
                plug = PLUG_P2;
                break;
            case "P3":
                plug = PLUG_P3;
                break;
        }
        Log.d("getPlugForTag", "Plug for tag"+tag+"  is "+plug);
        return plug;
    }

}
