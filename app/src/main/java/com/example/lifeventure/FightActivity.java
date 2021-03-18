package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FightActivity extends AppCompatActivity {

    ImageButton monster1,monster2,monster3,monster4,monster5,monster6,monster7,monster8,monster9,
            monster10,monster11,monster12,monster13,monster14,monster15,monster16,monster17,monster18,monster19,
            monster20,monster21,monster22,monster23,monster24;

    public static final String TOKEN = "Token";
    TextView tokenShow;
    int playerLevel;

    public static final String PROFILE = "Profile";
    public static final int PROFILE_LEVEL = 1;
    private int tokensTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        final ImageButton tasks= findViewById(R.id.taskButton);
        final ImageButton profile= findViewById(R.id.profileButton);
        final ImageButton settings = findViewById(R.id.settingsButton);

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FightActivity.this, TaskActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FightActivity.this, ProfileActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FightActivity.this, SettingsActivity.class));
            }
        });

        setMonsterButtons();

        SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        playerLevel = sharedPreferences.getInt(String.valueOf(PROFILE_LEVEL),1);

        tokenShow=findViewById(R.id.fightTokensText);
        SharedPreferences tokens = getSharedPreferences(TOKEN, MODE_PRIVATE);
        tokensTotal = tokens.getInt("fightTokens",0);
        tokenShow.setText("Fight tokens= "+tokensTotal);

    }

    private void setMonsterButtons(){
        monster1=findViewById(R.id.monster1);
        monster1.setImageResource(R.drawable.pixie1);
        monster2=findViewById(R.id.monster2);
        monster3=findViewById(R.id.monster3);
        monster4=findViewById(R.id.monster4);
        monster5=findViewById(R.id.monster5);
        monster6=findViewById(R.id.monster6);
        monster7=findViewById(R.id.monster7);
        monster8=findViewById(R.id.monster8);
        monster9=findViewById(R.id.monster9);
        monster10=findViewById(R.id.monster10);
        monster11=findViewById(R.id.monster11);
        monster12=findViewById(R.id.monster12);
        monster13=findViewById(R.id.monster13);
        monster14=findViewById(R.id.monster14);
        monster15=findViewById(R.id.monster15);
        monster16=findViewById(R.id.monster16);
        monster17=findViewById(R.id.monster17);
        monster18=findViewById(R.id.monster18);
        monster19=findViewById(R.id.monster19);
        monster20=findViewById(R.id.monster20);
        monster21=findViewById(R.id.monster21);
        monster22=findViewById(R.id.monster22);
        monster23=findViewById(R.id.monster23);
        monster24=findViewById(R.id.monster24);

        monster1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                    battleRecord();
                    Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                    intent.putExtra("Value",1);
                    startActivity(intent); }
            }
        });
        monster2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                    battleRecord();
                    Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                    intent.putExtra("Value",2);
                    startActivity(intent); }
            }
        });
        monster3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                    battleRecord();
                    Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                    intent.putExtra("Value",3);
                    startActivity(intent); }
            }
        });


        monster4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else if (playerLevel<=2){
                    Toast.makeText(FightActivity.this,"You are not strong enough yet", Toast.LENGTH_SHORT).show();
                }
                else{
                    battleRecord();
                    Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                    intent.putExtra("Value",4);
                    startActivity(intent); }
            }
        });
        monster5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else if (playerLevel<=3){
                    Toast.makeText(FightActivity.this,"You are not strong enough yet", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",5);
                startActivity(intent); }

                 */
                Toast.makeText(FightActivity.this,"Token added", Toast.LENGTH_SHORT).show();
                tokensTotal = tokensTotal+1;
                tokenShow.setText("Fight tokens= "+tokensTotal);
            }
        });

        //Locked out for alpha
        /*
        monster6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",6);
                startActivity(intent);
            }
        });
        monster7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",7);
                startActivity(intent);
            }
        });
        monster8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",8);
                startActivity(intent);
            }
        });
        monster9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",9);
                startActivity(intent);
            }
        });
        monster10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",10);
                startActivity(intent);
            }
        });
        monster11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",11);
                startActivity(intent);
            }
        });
        monster12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",12);
                startActivity(intent);
            }
        });
        monster13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",13);
                startActivity(intent);
            }
        });
        monster14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",14);
                startActivity(intent);
            }
        });
        monster15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",15);
                startActivity(intent);
            }
        });
        monster16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",16);
                startActivity(intent);
            }
        });
        monster17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",17);
                startActivity(intent);
            }
        });
        monster18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",18);
                startActivity(intent);
            }
        });
        monster19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",19);
                startActivity(intent);
            }
        });
        monster20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",20);
                startActivity(intent);
            }
        });
        monster21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",21);
                startActivity(intent); }
            }
        });
        monster22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",22);
                startActivity(intent); }
            }
        });
        monster23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",23);
                startActivity(intent); }
            }
        });
        monster24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenCheck()==false){
                    Toast.makeText(FightActivity.this,"You have no tokens to fight with", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent intent = new Intent(FightActivity.this, FightScreenActivity.class);
                intent.putExtra("Value",24);
                startActivity(intent); }
            }
        });

         */

    }

    public Boolean tokenCheck(){
        if(tokensTotal>0){
            return true;
        }
        else return false;
    }

    public void battleRecord(){
        SharedPreferences recordsPrefs = getSharedPreferences("Records",MODE_PRIVATE);
        int battlesTaken = recordsPrefs.getInt("BattlesTaken",0);
        battlesTaken=battlesTaken+1;
        SharedPreferences.Editor rEdit = recordsPrefs.edit();
        rEdit.putInt("BattlesTaken",battlesTaken);
        rEdit.apply();
    }
}


