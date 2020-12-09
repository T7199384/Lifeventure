package com.example.lifeventure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lifeventure.Dialogs.GearDialog;
import com.example.lifeventure.Dialogs.LevelDisplayDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE = null;
    public static final String GEAR = null;
    public static final int PROFILE_LEVEL = 1;
    public static final int PROFILE_EXP = 0;

    private int lvl;
    private int exp;
    private ArrayList<String> gearPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        exp=profile.getInt(String.valueOf(PROFILE_EXP),1);

        gearPart = new ArrayList<>(7);
        gearPart.add(0,"head");gearPart.add(1,"body");
        gearPart.add(2,"leg");gearPart.add(3,"foot");
        gearPart.add(4,"neck");gearPart.add(5,"ring");
        gearPart.add(6,"brace");

        SharedPreferences gear = getSharedPreferences(GEAR, MODE_PRIVATE);
        SharedPreferences.Editor gearEditor =gear.edit();
        for(int i=0;i<4;i++){
            gearEditor.putInt(String.valueOf(gearPart.get(i)),1);
        }
        for(int i=4;i<7;i++){
            gearEditor.putInt(String.valueOf(gearPart.get(i)),0);
        }
        gearEditor.apply();



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
                    int a = gearPref.getInt(gearPart.get(i),0);
                    argsList.add(a);
                }
                args.putIntegerArrayList("gear",argsList);
                gearDialog.setArguments(args);
                gearDialog.show(getSupportFragmentManager(),null);
            }
        });
    }
}
