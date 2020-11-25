package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lifeventure.Dialogs.CharacterDialog;

public class MainActivity extends AppCompatActivity implements CharacterDialog.CharacterListener{

    Button tasks;
    Button profile;
    Button fight;
    Button settings;

    private int gender;
    private int lvl;

    public static final String PROFILE = null;
    public static final int PROFILE_GENDER = 2;
    public static final int PROFILE_LEVEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks= findViewById(R.id.taskButtonMain);
        profile= findViewById(R.id.profileButtonMain);
        fight = findViewById(R.id.fightButtonMain);
        settings = findViewById(R.id.settingsButtonMain);

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TaskActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FightActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        loadProfile();
        if (gender==-1){
            CharacterDialog characterDialog = new CharacterDialog();
            characterDialog.show(getSupportFragmentManager(), "Pick a character");
        }


    }

    public void createProfile(int character) {
        SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(PROFILE_GENDER), character);

        editor.apply();
        Toast.makeText(this, "Profile Created", Toast.LENGTH_SHORT).show();
    }

    public void loadProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        gender = sharedPreferences.getInt(String.valueOf(PROFILE_GENDER),-1);
        lvl = sharedPreferences.getInt(String.valueOf(PROFILE_LEVEL),1);
    }

    @Override
    public void apply(int uCharacter) {
        createProfile(uCharacter);
    }
}
