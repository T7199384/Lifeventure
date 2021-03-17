package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.FightActivity;
import com.example.lifeventure.FightScreenActivity;
import com.example.lifeventure.R;

import java.util.Random;

public class PrizeDialog extends AppCompatDialogFragment {

    TextView prize, uItem, pItem, uStat, pStat, uStatTitle, pStatTitle;
    int monsterId;
    SharedPreferences playerGear;
    Context context;

    public static final String GEAR = "Gear";
    public static final String TOKEN = "Token";

    String[] typeList = new String[]{"Helmet","Chest plate","Leggings","Boots","Necklace","Ring","Bracelet", "Sword", "Staff"};
    String[] weaponRarity = new String[] {"Wood", "Copper", "Bronze","Iron","Steel"};
    String[] armorRarity = new String[] {"Cloth","Leather","Iron"};
    String[] jewelRarity = new String[] {"Tin","Copper","Bronze"};

    String playerGearType;
    int playerGearStat;
    int pDamage;

    int itemType;
    int itemRarity;

    public PrizeDialog(Bundle bundle, Context context) {
        monsterId=bundle.getInt("monID");
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog_prize,null);

        builder.setView(view);

        playerGear = context.getSharedPreferences(GEAR,Context.MODE_PRIVATE);

        itemType = new Random().nextInt(8)+1;
        itemRarity = new Random().nextInt(monsterId+1)+1;

        pStatTitle=view.findViewById(R.id.thisitemstrength);
        switch(itemType){
            case 8: pStatTitle.setText("Magic");
                playerGearStat=playerGear.getInt("magic",0);

                if(playerGearStat!=0)playerGearType="Staff";
                else if(playerGear.getInt("attack",0)!=0)playerGearType="Sword";
                else playerGearType="Fists";

                if(playerGear.getBoolean("weaponEquipped",false)==true) playerGearType=typeList[itemType];
                break;
            case 7: pStatTitle.setText("Strength");
                playerGearStat=playerGear.getInt("attack",0);

                if(playerGearStat!=0)playerGearType="Sword";
                else if(playerGear.getInt("magic",0)!=0)playerGearType="Staff";
                else playerGearType="Fists";

                if(playerGear.getBoolean("weaponEquipped",false)==true) playerGearType=typeList[itemType];
                break;
            case 6: playerGearStat=playerGear.getInt("brace",0);
                if(playerGear.getBoolean("braceEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence");  break;
            case 5: playerGearStat=playerGear.getInt("ring",0);
                if(playerGear.getBoolean("ringEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence");  break;
            case 4: playerGearStat=playerGear.getInt("neck",0);
                if(playerGear.getBoolean("neckEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence");  break;
            case 3: playerGearStat=playerGear.getInt("foot",0);
                if(playerGear.getBoolean("footEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence");  break;
            case 2: playerGearStat=playerGear.getInt("leg",0);
                if(playerGear.getBoolean("legEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence");  break;
            case 1: playerGearStat=playerGear.getInt("body",0);
            if(playerGear.getBoolean("bodyEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence");  break;
            case 0: playerGearStat=playerGear.getInt("head",0);
                if(playerGear.getBoolean("headEquipped",false)==true) playerGearType=typeList[itemType]; pStatTitle.setText("Defence"); break;
        }

        if(playerGearType==null) { playerGearType="Rags"; playerGearStat=0;}

        uStatTitle=view.findViewById(R.id.youritemstrength);
        if(playerGearType==null) uStatTitle.setText("Defense");
        else if(playerGearType.equals("Staff"))uStatTitle.setText("Magic");
        else if(playerGearType.equals("Sword"))uStatTitle.setText("Strength");
        else if(playerGearType.equals("Fists"))uStatTitle.setText("Strength");
        else uStatTitle.setText("Defense");

        if(typeList[itemType].equals("Staff"))uStatTitle.setText("Magic");
        else if(typeList[itemType].equals("Sword"))uStatTitle.setText("Strength");
        else uStatTitle.setText("Defence");

        uItem=view.findViewById(R.id.youritemname);
        switch (playerGearType) {
            case "Rags":
            case "Fists":
                uItem.setText(playerGearType);
                break;
            case "Sword":
            case "Staff":
                uItem.setText(weaponRarity[playerGearStat] + " " + playerGearType);
                break;
            case "brace":
            case "ring":
            case "neck":
                uItem.setText(jewelRarity[playerGearStat] + " " + playerGearType);
                break;
            default:
                uItem.setText(armorRarity[playerGearStat] + " " + playerGearType);
                break;
        }

        prize=view.findViewById(R.id.prize);
        if(typeList[itemType].equals("Sword") || typeList[itemType].equals("Staff")) prize.setText(weaponRarity[itemRarity]+" "+typeList[itemType]);
        else if(typeList[itemType].equals("brace") || typeList[itemType].equals("ring")|| typeList[itemType].equals("neck")) prize.setText(jewelRarity[itemRarity]+" "+typeList[itemType]);
        else prize.setText(armorRarity[itemRarity]+" "+typeList[itemType]);

        pItem=view.findViewById(R.id.thisitemname);
        if(typeList[itemType].equals("Sword") || typeList[itemType].equals("Staff")) pItem.setText(weaponRarity[itemRarity]+" "+typeList[itemType]);
        else if(typeList[itemType].equals("brace") || typeList[itemType].equals("ring")|| typeList[itemType].equals("neck")) pItem.setText(jewelRarity[itemRarity]+" "+typeList[itemType]);
        else pItem.setText(armorRarity[itemRarity]+" "+typeList[itemType]);

        uStat=view.findViewById(R.id.youritemstrengthstat);
        uStat.setText(String.valueOf(playerGearStat));
        pStat=view.findViewById(R.id.thisitemstrengthstat);
        pDamage=itemRarity;
        pStat.setText(String.valueOf(pDamage));

        builder.setPositiveButton("Equip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = playerGear.edit();
                switch (typeList[itemType]){
                    case "Sword":editor.putInt("attack",pDamage); editor.putString("weapon",typeList[itemType]); editor.putBoolean("weaponEquipped",true);break;
                    case "Staff": editor.putInt("magic",pDamage); editor.putString("weapon",typeList[itemType]); editor.putBoolean("weaponEquipped",true);break;
                    case "Helmet": editor.putInt("head",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("headEquipped",true);break;
                    case "Chest plate": editor.putInt("body",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("bodyEquipped",true);break;
                    case "Leggings": editor.putInt("leg",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("legEquipped",true);break;
                    case "Boots": editor.putInt("foot",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("footEquipped",true);break;
                    case "Necklace": editor.putInt("neck",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("neckEquipped",true);break;
                    case "Ring": editor.putInt("ring",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("ringEquipped",true);break;
                    case "Bracelet": editor.putInt("brace",pDamage); editor.putString("type",typeList[itemType]); editor.putBoolean("braceEquipped",true);break;
                }
                editor.apply();

                SharedPreferences token = context.getSharedPreferences(TOKEN,Context.MODE_PRIVATE);
                int tokens = token.getInt("fightTokens",1);
                tokens=tokens--;
                SharedPreferences.Editor tokenEdit = token.edit();
                tokenEdit.putInt("fightTokens",tokens);
                tokenEdit.apply();

                startActivity(new Intent(context, FightActivity.class));
            }
        });

        builder.setNegativeButton("Abandon", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(context, FightActivity.class));
            }
        });

        return builder.create();
    }

}
