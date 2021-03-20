package com.example.lifeventure.Classes;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.example.lifeventure.TaskActivity;
import com.google.android.gms.location.LocationResult;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE= "com.example.lifeventure.Classes.UPDATE_LOCATION";
    GPSChecker gpsChecker;

    @Override
    public void onReceive(Context context, Intent intent) {

        gpsChecker=new GPSChecker();

        if (intent!=null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result!=null){
                    Location location = result.getLastLocation();
                    try{
                        checkGPSLocation(location,context,intent);
                    }catch (Exception ex){
                        Toast.makeText(context,"Error has occured",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void checkGPSLocation(final Location location, Context context, Intent intent) {


                Geocoder geoCoder=new Geocoder(context);
                double locationLatitude = location.getLatitude();
                double locationLongitude = location.getLongitude();
                List<Address> taskLocation = null;
                try {
                    taskLocation = geoCoder.getFromLocation(locationLatitude,locationLongitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String locationString = taskLocation.get(0).getAddressLine(0);
                SharedPreferences geocaches = context.getSharedPreferences("Tasks",MODE_PRIVATE);
                for(int i=0;i<geocaches.getInt("taskNum",0);i++){
                    String task = geocaches.getString(String.valueOf(i),"");
                    String[] split = task.split("¬");
                    for(int j=0;j<split.length;j++){
                        String str = split[j];
                        int strIndex = str.indexOf(", ")+2;
                        if(strIndex>=2) {
                            str = str.substring(strIndex);
                        }

                        if(str.equals("")){

                        }
                        else {
                            char ch = str.charAt(0);
                            if (Character.isDigit(ch)) {
                                int numRemove = str.indexOf(" ") + 1;
                                if (numRemove > 0) {
                                    str = str.substring(numRemove);
                                }
                            }
                            ch = locationString.charAt(0);
                            if (Character.isDigit(ch)) {
                                int numRemove = locationString.indexOf(" ") + 1;
                                if (numRemove > 0) {
                                    locationString = locationString.substring(numRemove);
                                }

                            }
                        }

                        if(locationString.equals(str) || locationString.equals("Corporation Rd, Middlesbrough TS1 1LT, UK")){
                            taskLocationComplete(i, context);
                            break;
                        }
                    }
                }

    }

    private void taskLocationComplete(int index, Context context) {
        Intent intent=new Intent(context, TaskActivity.class);
        intent.putExtra("index",index);
        context.startActivity(intent);
    }
}

