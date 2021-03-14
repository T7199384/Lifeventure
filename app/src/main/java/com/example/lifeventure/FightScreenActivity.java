package com.example.lifeventure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Range;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FightScreenActivity extends AppCompatActivity {

    int monsterId;

    TextView title,messageBox, playerHealthText;
    Button attack,defend,spells,escape;
    ImageView monsterImg;
    ProgressBar enemyHP;

    Handler handler;

    String[] monsterNames={"Pixie","Ogre","Goblin"};
    HashMap<String,int[]> monsterMap= new HashMap<String,int[]>();
    int[] monsterStats;

    int dmg;
    int healthMultiplier;
    int progressMarker=100;
    int eHealth;

    int pHealth;

    int MESSAGE_DELAY=3000;

    String intro = "You face a "+monsterNames[monsterId]+"!";
    String playerAttack, monsterAttack, playerDeath, monsterDeath, playerCast, playerRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fight_screen);

        MapCreate();
        buttonTrap(false);

        monsterId=getIntent().getExtras().getInt("Value")-1;

        title = findViewById(R.id.monsterTitle);
        title.setText(monsterNames[monsterId]);
        monsterStats=monsterMap.get(monsterNames[monsterId]);
        playerHealthText=findViewById(R.id.playerHealth);
        pHealth=100;

        monsterImg=findViewById(R.id.monsterImage);
        monsterImg.setImageResource(R.drawable.pixie1);

        enemyHP=findViewById(R.id.enemyHp);
        switch (monsterId){
            case 0:
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

        defend=findViewById(R.id.defend);
        defend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnemyHit(2);
            }
        });

        escape=findViewById(R.id.escape);
        escape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerRun="You escape from the "+monsterNames[monsterId]+"!";
                messageShow(playerRun);
                startActivity(new Intent(FightScreenActivity.this, FightActivity.class));
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonTrap(true);
            }
        }, MESSAGE_DELAY);

    }

    public void messageShow(String message){

        messageBox=findViewById(R.id.messageBox);
        messageBox.setText(message);
        messageBox.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageBox.setText("");
            }
        }, MESSAGE_DELAY);

    }

    public void PlayerHit(){
        buttonTrap(false);

        SharedPreferences playerStats = getSharedPreferences("stats",MODE_PRIVATE);
        SharedPreferences playerGear = getSharedPreferences("gear",MODE_PRIVATE);
        int strength=playerStats.getInt("strength",10);
        int weapon=playerGear.getInt("weapon",0);

        int random=new Random().nextInt(2)+2;


        dmg=(strength+weapon)/(random*monsterStats[1]);
        eHealth=eHealth-dmg;
        progressMarker=eHealth*healthMultiplier;
        enemyHP.setProgress(progressMarker);

        playerAttack = "You hit for "+dmg+" to the "+monsterNames[monsterId]+"!";
        messageShow(playerAttack);

        Toast.makeText(FightScreenActivity.this, String.valueOf(eHealth) ,Toast.LENGTH_SHORT).show();

        if(eHealth<=0){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Dead(true);
                }
            }, 5000);
        }
        else {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EnemyHit(1);
                }
            }, 2000);
        }
    }

    private void EnemyHit(int defence) {

        int monsterStr=monsterStats[0];
        int random=new Random().nextInt(2)+2;

        dmg=monsterStr*random/defence;
        pHealth=pHealth-dmg;

        playerHealthText.setText(String.valueOf(pHealth)+"/100");

        monsterAttack = "The "+monsterNames[monsterId]+" hits for "+dmg+"!";
        if(defence==2){
            monsterAttack=monsterAttack+"\nYou defended yourself from the attack!";
        }
        messageShow(monsterAttack);

        if(pHealth<=0){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Dead(false);
                }
            }, 5000);
        }
        else{
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonTrap(true);
                }
            }, 1000);
        }
    }

    private void Dead(Boolean monsterDead) {
        if(monsterDead==true){
            monsterDeath="The "+monsterNames[monsterId]+" is dead!";
            messageShow(monsterDeath);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(FightScreenActivity.this, FightActivity.class));
                }
            }, 4000);
        }
        else{
            playerDeath="The "+monsterNames[monsterId]+" has defeated you!";
            messageShow(playerDeath);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(FightScreenActivity.this, FightActivity.class));
                }
            }, 4000);
        }
    }

    private void MapCreate() {
        monsterMap.put("Pixie", new int[]{1, 1, 1, 1});
    }

    private void buttonTrap(Boolean on){
        attack=findViewById(R.id.attack); defend=findViewById(R.id.defend); spells=findViewById(R.id.spells); escape=findViewById(R.id.escape);
        if(on==true){
            attack.setEnabled(true);defend.setEnabled(true);spells.setEnabled(true);escape.setEnabled(true);
        }
        else if(on==false){
            attack.setEnabled(false);defend.setEnabled(false);spells.setEnabled(false);escape.setEnabled(false);
        }
    }

}
