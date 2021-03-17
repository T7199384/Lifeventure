package com.example.lifeventure;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifeventure.Dialogs.PrizeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FightScreenActivity extends AppCompatActivity {

    public static final int PROFILE_LEVEL = 1;
    public static final String GEAR = "Gear";
    public static final String TOKEN = "Token";
    int monsterId;

    TextView title,messageBox, playerHealthText;
    Button attack,defend,spells,escape;
    ImageView monsterImg;
    ProgressBar enemyHP;

    Handler handler;

    String[] monsterNames={"Pixie","Ogre","Goblin"};
    ArrayList<String> spellsList;

    String[] playerSpells;
    HashMap<String,int[]> monsterMap= new HashMap<String,int[]>();
    int[] monsterStats;

    int dmg;
    int healthMultiplier;
    int progressMarker=100;
    int eHealth;

    int pHealth;
    int maxHealth;

    Boolean skip;
    int skipTurns;

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

        SharedPreferences stats = getSharedPreferences("stats",MODE_PRIVATE);
        maxHealth = 100 + (10 * (stats.getInt("defense",1)-1));
        pHealth=maxHealth;
        playerHealthText.setText(pHealth+"/"+maxHealth);

        monsterImg=findViewById(R.id.monsterImage);
        monsterImg.setImageResource(R.drawable.pixie1);

        spellsList=new ArrayList<String>();

        enemyHP=findViewById(R.id.enemyHp);
        switch (monsterId){
            case 0:
                eHealth=50; healthMultiplier=2; enemyHP.setProgress(100);break;
        }

        SharedPreferences playerLevel = getSharedPreferences("profile",MODE_PRIVATE);
        int level = playerLevel.getInt(String.valueOf(PROFILE_LEVEL),1);
        switch(level){
            default: spellsList.add("Heal"); spellsList.add("Fire"); spellsList.add("Freeze");
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
                if(!skip) {
                    EnemyHit(2);
                }
                else{
                    skip = false;
                    if(skipTurns>0){
                        skip=true;
                        skipTurns=skipTurns-1;
                    }
                    else{
                        skip=false;
                    }
                }
            }
        });

        spells=findViewById(R.id.spells);
        spells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cast();
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

    public void cast(){

        buttonTrap(false);

        skip = false;
        if(skipTurns>0){
            skip=true;
            skipTurns=skipTurns-1;
        }
        else{
            skip=false;
        }

        playerSpells = spellsList.toArray(new String[spellsList.size()]);

        new AlertDialog.Builder(this)
                .setTitle("Spells")
                .setItems(playerSpells, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpellSelection(which);
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    //TODO This you fool
    public void SpellSelection(int which){
        SharedPreferences stats = getSharedPreferences("stats", MODE_PRIVATE);
        int castingPower = stats.getInt("casting",1);

        SharedPreferences playerGear = getSharedPreferences("weapon",MODE_PRIVATE);
        int magic=playerGear.getInt("magic",0);

        int potency = magic+castingPower;
        skip = false;

        switch(which){
            case 0:
                int heal=new Random().nextInt(5)+1+potency;
                pHealth=pHealth+heal;
                playerHealthText.setText(pHealth+"/"+maxHealth);
                playerCast="You healed yourself for "+heal+"!";
                messageShow(playerCast);
                break;
            case 1:
                dmg = (new Random().nextInt(3)+1)*potency;
                eHealth=eHealth-dmg;
                progressMarker=eHealth*healthMultiplier;
                enemyHP.setProgress(progressMarker);

                playerCast = "You set the "+monsterNames[monsterId]+" alight for "+dmg+"!";
                messageShow(playerCast);
                break;
            case 2:
                int freeze = 70 -( (
                        (new Random().nextInt(7)+1)
                                *10 ) - (5*potency) );
                if (freeze>70){freeze=70;}
                int chance = new Random().nextInt(100)+1;
                if(chance<freeze){
                    skip=true;
                    playerCast="You successfully froze the "+ monsterNames[monsterId]+" For these 2 turns!";
                    skipTurns=1;
                    messageShow(playerCast);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonTrap(true);
                        }
                    }, 1000);
                    break;
                }
                else{
                    skip=false;
                    playerCast="You failed to freeze "+ monsterNames[monsterId]+" this time!";
                    messageShow(playerCast);
                }
        }

        if(eHealth<=0){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Dead(true);
                }
            }, 5000);
        }
        else if(!skip) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EnemyHit(1);
                }
            }, 2000);
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
        SharedPreferences playerGear = getSharedPreferences(GEAR,MODE_PRIVATE);
        int strength=playerStats.getInt("strength",10);
        int weapon=playerGear.getInt("attack",0);

        int random=new Random().nextInt(2)+2;


        dmg=(strength+weapon)/(random*monsterStats[1])+10000/*TODO remove this after test*/;
        eHealth=eHealth-dmg;
        progressMarker=eHealth*healthMultiplier;
        enemyHP.setProgress(progressMarker);

        playerAttack = "You hit for "+dmg+" to the "+monsterNames[monsterId]+"!";
        messageShow(playerAttack);

        if(skipTurns>0){
            skip=true;
            skipTurns=skipTurns-1;
        }
        else{
            skip=false;
        }

        if(eHealth<=0){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Dead(true);
                }
            }, 5000);
        }
        else if(!skip){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EnemyHit(1);
                }
            }, 2000);
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

    private void EnemyHit(int defence) {

        int monsterStr=monsterStats[0];
        int random=new Random().nextInt(2)+2;

        dmg=monsterStr*random/defence;
        pHealth=pHealth-dmg;

        playerHealthText.setText(String.valueOf(pHealth)+"/"+String.valueOf(maxHealth));

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
            prize(monsterId);
/*            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 4000);*/
        }
        else{
            playerDeath="The "+monsterNames[monsterId]+" has defeated you!";
            messageShow(playerDeath);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences token = getSharedPreferences(TOKEN,Context.MODE_PRIVATE);
                    int tokens = token.getInt("fightTokens",1);
                    tokens=tokens--;
                    SharedPreferences.Editor tokenEdit = token.edit();
                    tokenEdit.putInt("fightTokens",tokens);
                    tokenEdit.apply();

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

    private void prize(int monsterId){
        Bundle bundle =new Bundle();
        bundle.putInt("monID",monsterId);

        Context context = FightScreenActivity.this;

        new PrizeDialog(bundle, context).show(getSupportFragmentManager(),null);
    }

}
