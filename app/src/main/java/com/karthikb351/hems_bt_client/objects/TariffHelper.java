package com.karthikb351.hems_bt_client.objects;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by karthikbalakrishnan on 02/05/15.
 */
public class TariffHelper {

    // TARIFF RATES
    // 0700 hrs to 1100 hrs
    // 1800 hrs to 2300 hrs- Rs. 7/kWh
    //
    // 1100 hrs to 1800 hrs - Rs. 6.30/kWh
    // 2300 hrs to 0700 hrs - Rs. 4.50/kWh

    public static boolean isPeakTime() {
        boolean flag = false;
        int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if((currHour>=7 && currHour<11) || (currHour>=18 && currHour<23))
            flag=true;
        return flag;
    }

    public static double calculateCost(Device d) {
        double cost = 0.0;
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(d.getStartTime());

        Calendar end = Calendar.getInstance();
        if(d.getEndTime()>=d.getStartTime())
            end.setTimeInMillis(d.getEndTime());


        Calendar temp = (Calendar)start.clone();


        while(temp.getTimeInMillis()<end.getTimeInMillis())
        {
            int tempHour = temp.get(Calendar.HOUR_OF_DAY);
            int tempMin = temp.get(Calendar.MINUTE);
            int endHour = end.get(Calendar.HOUR_OF_DAY);
            int endMin = end.get(Calendar.MINUTE);

            if(tempHour>=7 && tempHour <11) {
                if(endHour<11) {
                    Log.d("TariffHelper","End: 7-11: "+(endHour-tempHour)+", "+((60-tempMin)%60));
                    cost += (endHour-tempHour)*7.0 + (endMin-tempMin)*7.0/60.0;
                    temp.setTimeInMillis(end.getTimeInMillis());
                }
                else {
                    Log.d("TariffHelper","Continue: 7-11: "+(10-tempHour)+", "+((60-tempMin)%60));
                    cost += (10-tempHour)*7.0 + ((60-tempMin)%60)*7.0/60.0;
                    temp.set(Calendar.HOUR_OF_DAY, 11);
                    temp.set(Calendar.MINUTE, 0);
                }
            }

            else if(tempHour>=11 && tempHour <18) {
                if(endHour<18) {
                    Log.d("TariffHelper","End: 11-19: "+(endHour-tempHour)+", "+((60-tempMin)%60));
                    cost += (endHour-tempHour)*6.3 + (endMin-tempMin)*6.3/60.0;
                    temp.setTimeInMillis(end.getTimeInMillis());
                }
                else {
                    Log.d("TariffHelper","Continue: 11-19: "+(17-tempHour)+", "+((60-tempMin)%60));
                    cost += (17-tempHour)*6.3 + ((60-tempMin)%60)*6.3/60.0;
                    temp.set(Calendar.HOUR_OF_DAY, 19);
                    temp.set(Calendar.MINUTE, 0);
                }
            }

            else if(tempHour>=18 && tempHour <23) {
                if(endHour<23) {
                    Log.d("TariffHelper","End: 19-23: "+(endHour-tempHour)+", "+((60-tempMin)%60));
                    cost += (endHour-tempHour)*7.0 + (endMin-tempMin)*7.0/60.0;
                    temp.setTimeInMillis(end.getTimeInMillis());
                }
                else {
                    Log.d("TariffHelper","Continue: 19-23: "+(22-tempHour)+", "+((60-tempMin)%60));
                    cost += (22-tempHour)*7.0 + ((60-tempMin)%60)*7.0/60.0;
                    temp.set(Calendar.HOUR_OF_DAY, 23);
                    temp.set(Calendar.MINUTE, 0);
                }
            }

            else if(tempHour>=23) {
                if(endHour>=23) {
                    Log.d("TariffHelper","End: 23+: "+(endHour-tempHour)+", "+((60-tempMin)%60));
                    cost += (endHour-tempHour)*4.5 + (endMin-tempMin)*4.5/60.0;
                    temp.setTimeInMillis(end.getTimeInMillis());
                }
                else {
                    Log.d("TariffHelper","Continue: 23+: "+(22-tempHour)+", "+((60-tempMin)%60));
                    cost += (22-tempHour)*4.5 + ((60-tempMin)%60)*4.5/60.0;
                    temp.set(Calendar.HOUR_OF_DAY, 0);
                    temp.set(Calendar.MINUTE, 0);
                }
            }

            else if(tempHour<7) {
                if(endHour<7) {
                    Log.d("TariffHelper","End: 7-: "+(endHour-tempHour)+", "+((60-tempMin)%60));
                    cost += (endHour-tempHour)*4.5 + (endMin-tempMin)*4.5/60.0;
                    temp.setTimeInMillis(end.getTimeInMillis());
                }
                else {
                    Log.d("TariffHelper","Continue: 7-: "+(6-tempHour)+", "+((60-tempMin)%60));
                    cost += (6-tempHour)*4.5 + ((60-tempMin)%60)*4.5/60.0;
                    temp.set(Calendar.HOUR_OF_DAY, 7);
                    temp.set(Calendar.MINUTE, 0);
                }
            }

//            else if(tempHour>=11 && tempHour <18 || (tempHour==18 && tempMin < 30) ) {
//                if(endHour<18) {
//                    cost += (endHour-tempHour)*6.30 + (endMin-tempMin)*6.30/60.0;
//                    temp.setTimeInMillis(end.getTimeInMillis());
//                }
//                else if((endHour==18 && endMin < 30) ) {
//                    cost += (endHour-tempHour)*6.30 + (30-tempMin)*6.30/60.0;
//                    temp.setTimeInMillis(end.getTimeInMillis());
//                }
//                else {
//                    cost += (11-tempHour)*7.0 + (60-tempMin)*7.0/60.0;
//                    temp.add(Calendar.HOUR_OF_DAY, (18-tempHour));
//                    temp.add(Calendar.MINUTE, (60-tempMin));
//                }
//            }


        }
        return cost*Double.parseDouble(d.getPowerRating())/1000.0;
    }


}
