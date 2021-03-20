package com.example.lifeventure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.lifeventure.Classes.GPSChecker;
import com.example.lifeventure.Dialogs.GearDialog;
import com.example.lifeventure.Dialogs.LevelDisplayDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE = "Profile";
    public static final String GEAR = "Gear";
    public static final int PROFILE_LEVEL = 1;
    public static final int PROFILE_EXP = 0;
    private static final int PROFILE_GENDER = 2;

    private int lvl;
    private int exp;
    private ArrayList<String> gearPart;
    private int character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Your adventurer");

        final ImageButton tasks= findViewById(R.id.taskButton);
        final ImageButton fight = findViewById(R.id.fightButton);
        final ImageButton settings = findViewById(R.id.settingsButton);

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, TaskActivity.class));
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FightActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });


        SharedPreferences profile = getSharedPreferences(PROFILE,MODE_PRIVATE);
        lvl=profile.getInt(String.valueOf(PROFILE_LEVEL),1);
        exp=profile.getInt(String.valueOf(PROFILE_EXP),0);
        character=profile.getInt(String.valueOf(PROFILE_GENDER),1);

        ImageView profileImg = findViewById(R.id.profileImg);

        switch (character){
            case 0: profileImg.setImageResource(R.drawable.maleprofile); break;
            case 1: profileImg.setImageResource(R.drawable.femaleprofile); break;

        }

        gearPart = new ArrayList<>(7);
        gearPart.add(0,"head");gearPart.add(1,"body");
        gearPart.add(2,"leg");gearPart.add(3,"foot");
        gearPart.add(4,"neck");gearPart.add(5,"ring");
        gearPart.add(6,"brace");

        SharedPreferences gear = getSharedPreferences(GEAR, MODE_PRIVATE);
        /*SharedPreferences.Editor gearEditor =gear.edit();
        for(int i=0;i<4;i++){
            gearEditor.putInt(String.valueOf(gearPart.get(i)),1);
        }
        for(int i=4;i<7;i++){
            gearEditor.putInt(String.valueOf(gearPart.get(i)),0);
        }
        gearEditor.apply();*/



        Button level = findViewById(R.id.lvlButton);
        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelDisplayDialog levelDisplayDialog = new LevelDisplayDialog();
                Bundle args = new Bundle();
                args.putInt("lvl",lvl);
                args.putInt("exp",exp);
                levelDisplayDialog.setArguments(args);
                levelDisplayDialog.show(getSupportFragmentManager(),null);
            }
        });

        Button bGear = findViewById(R.id.gearButton);
        bGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GearDialog gearDialog = new GearDialog();
                Bundle args = new Bundle();
                SharedPreferences gearPref = getSharedPreferences(GEAR, MODE_PRIVATE);
                ArrayList<Integer> argsList = new ArrayList<>(7);

                for(int i=0;i<7;i++){
                    int a;
                    Boolean equip = gearPref.getBoolean(gearPart.get(i)+"Equipped",false);
                    if(equip==true) {
                        a = gearPref.getInt(gearPart.get(i), 0); }
                    else {a = 0;}
                    argsList.add(a);
                }
                args.putIntegerArrayList("gear",argsList);
                gearDialog.setArguments(args);
                gearDialog.show(getSupportFragmentManager(),null);
            }
        });

        Button spells= findViewById(R.id.spellbookButton);
        spells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> spellsList=new ArrayList<String>();
                SharedPreferences playerLevel = getSharedPreferences("profile",MODE_PRIVATE);
                int level = playerLevel.getInt(String.valueOf(PROFILE_LEVEL),1);
                switch(level){
                    default: spellsList.add("Heal"); spellsList.add("Fire"); spellsList.add("Freeze");
                }
                String[] playerSpells = spellsList.toArray(new String[spellsList.size()]);

                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Spells")
                        .setItems(playerSpells, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String spellDesc;
                                switch(which){
                                    case 0: spellDesc="Heal yourself for a amount of damage relative to your casting power."; break;
                                    case 1: spellDesc="Deal fire damage relative to your casting power."; break;
                                    case 2: spellDesc="Have a 70% or lower change to freeze the target for a free hit, casting power affects how low."; break;
                                    default:spellDesc="";
                                }
                                new AlertDialog.Builder(ProfileActivity.this)
                                        .setTitle("Description")
                                        .setMessage(spellDesc)
                                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        Button stats = findViewById(R.id.statButton);
        stats.setOnClickListener(new View.OnClickListener() {

            SharedPreferences statPrefs = getSharedPreferences("Stats", MODE_PRIVATE);

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Stats")
                        .setMessage("Strength :     "+statPrefs.getInt("Strength",0)+"\n"+
                                "Magic :          "+statPrefs.getInt("Magic",0)+"\n"+
                                "Health :     "+statPrefs.getInt("Health",100)+"\n"+
                                "Defence :     "+statPrefs.getInt("Defence",0)+"\n")
                        .show();
            }
        });


        final Button records = findViewById(R.id.recordButton);
        records.setOnClickListener(new View.OnClickListener() {

            SharedPreferences recordsPrefs = getSharedPreferences("Records",MODE_PRIVATE);

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Records")
                        .setMessage(
                                "Tasks complete: "+recordsPrefs.getInt("TasksComplete",0)+"\n"+
                                "Battles taken: "+recordsPrefs.getInt("BattlesTaken",0)+"\n"+
                                "Battles won: "+recordsPrefs.getInt("BattlesWon",0)+"\n"+
                                "Battles lost: "+recordsPrefs.getInt("BattlesLost",0)
                                )
                        .show();
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

