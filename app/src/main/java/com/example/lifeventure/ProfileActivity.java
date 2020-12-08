package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lifeventure.Dialogs.LevelDisplayDialog;

import java.util.ArrayList;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE = null;
    public static final String GEAR = null;
    public static final int PROFILE_LEVEL = 1;
    public static final int PROFILE_EXP = 0;

    private int lvl;
    private int exp;

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

        SharedPreferences gear = getSharedPreferences(GEAR, MODE_PRIVATE);
        Set<String> gearSet = gear.getStringSet("gear",null);
        if(gearSet==null){
            for(int i=0;i<7;i++){
                switch(i){
                    case 7:
                        gearSet.add("0");
                        break;
                    case 6:
                        gearSet.add("0");
                        break;
                    case 5:
                        gearSet.add("0");
                        break;
                    default:
                        gearSet.add("1");
                }
                gearSet.add("1");
            }
            gear.edit().putStringSet("gear",gearSet);
            gear.edit().commit();
        }


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
    }
}
