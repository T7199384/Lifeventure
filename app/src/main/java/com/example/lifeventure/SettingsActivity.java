package com.example.lifeventure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.lifeventure.Classes.GPSChecker;
import com.example.lifeventure.Classes.LocationService;
import com.google.android.gms.location.LocationRequest;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    public static final String PROFILE = "Profile";
    private static final int PROFILE_GENDER = 2;
    private Switch batterySwitch;
    private Switch locationOnSwitch;
    SharedPreferences settings;
    LocationRequest locationRequest;

    Button characterChange;
    Button reset;

    SharedPreferences profile;
    String[] characters = new String[]{"Male","Female"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("Settings");

        settings=getSharedPreferences("SETTINGS",MODE_PRIVATE);

        batterySwitch = findViewById(R.id.batterySwitch);
        locationRequest= new LocationRequest();

        if(settings.getInt("BATTERY_POWER",0)==1){
            batterySwitch.setChecked(true);  batterySwitch.setText("ON"); }
        else{
            batterySwitch.setChecked(false);  batterySwitch.setText("OFF");}

        batterySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!batterySwitch.isChecked()){
                    batterySwitch.setText("OFF");
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    SharedPreferences.Editor batteryEdit = settings.edit();
                    batteryEdit.putInt("BATTERY_POWER",0);
                    batteryEdit.apply();
                }
                else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    batterySwitch.setText("ON");
                    SharedPreferences.Editor batteryEdit = settings.edit();
                    batteryEdit.putInt("BATTERY_POWER",1);
                    batteryEdit.apply();
                }
            }
        });

        profile = getSharedPreferences(PROFILE,MODE_PRIVATE);

        characterChange=findViewById(R.id.changeCharacterButton);
        characterChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Change character");
                builder.setItems(characters, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor charEdit = profile.edit();
                                charEdit.putInt(String.valueOf(PROFILE_GENDER),which);
                                charEdit.apply();
                            }
                        });
                builder.create().show();
            }
        });

        reset=findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("WARNING");
                builder.setMessage("This will delete all data of tasks progress, close the app, and you will have to start from scratch, are you sure you wish to continue?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File sharedPreferencesFile = new File("/data/data/"+ getPackageName()+ "/shared_prefs/");
                        File[] listFiles = sharedPreferencesFile.listFiles();
                        for (File file : listFiles) {
                            file.delete();
                        }
                        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                        System.exit(0);
                    }
                });
                builder.create().show();
            }
        });

        final ImageButton tasks= findViewById(R.id.taskButton);
        final ImageButton profile= findViewById(R.id.profileButton);
        final ImageButton fight = findViewById(R.id.fightButton);


        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, TaskActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FightActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        GPSChecker gpsChecker = new GPSChecker();
        gpsChecker.create(this, this);
    }
}
