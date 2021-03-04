package com.example.lifeventure;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Range;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class FightScreenActivity extends AppCompatActivity {

    int monsterId;

    TextView title,messageBox;
    Button attack,defend,spells,escape;
    ImageView monsterImg;
    ProgressBar enemyHP;

    String[] monsterNames={"Pixie","Ogre","Goblin"};

    int dmg;
    int healthMultiplier;
    int progressMarker=100;
    int eHealth;

    String intro = "You face a "+monsterNames[monsterId]+"!";
    String playerAttack;
    String monsterAttack = "The "+monsterNames[monsterId]+" hits for "+dmg+"!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fight_screen);

        monsterId=getIntent().getExtras().getInt("Value");

        title = findViewById(R.id.monsterTitle);
        title.setText(monsterNames[monsterId-1]);

        monsterImg=findViewById(R.id.monsterImage);
        monsterImg.setImageResource(R.drawable.pixie1);

        enemyHP=findViewById(R.id.enemyHp);
        switch (monsterId){
            case 1:
                eHealth=50; healthMultiplier=2; enemyHP.setProgress(100);break;
        }

        messageShow(intro);



        attack=findViewById(R.id.attack);
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerHit();
            }
        });
    }

    public void messageShow(String message){

        messageBox=findViewById(R.id.messageBox);
        messageBox.setText(message);
        try {
            wait(3000);
        } catch (InterruptedException e) {
            messageBox.setText("");
        }
        messageBox.setText("");
    }

    public void PlayerHit(){
        SharedPreferences playerStats = getSharedPreferences("stats",MODE_PRIVATE);
        SharedPreferences playerGear = getSharedPreferences("gear",MODE_PRIVATE);
        int strength=playerStats.getInt("strength",10);
        int weapon=playerGear.getInt("weapon",0);

        int random=new Random().nextInt(2)+2;


        dmg=(strength+weapon)/random;
        eHealth=eHealth-dmg;
        progressMarker=progressMarker-eHealth/healthMultiplier;
        enemyHP.setProgress(progressMarker);

        playerAttack = "You hit for "+dmg+" to the "+monsterNames[monsterId-1]+"!";
        messageShow(playerAttack);
    }

}
