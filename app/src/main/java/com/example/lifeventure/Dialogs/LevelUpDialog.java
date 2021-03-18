package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.R;

import java.util.zip.Inflater;

public class LevelUpDialog extends AppCompatDialogFragment {

    int str,mag,hp,def;
    int strLimit, magLimit, hpLimit, defLimit;
    TextView strStat, magStat, hpStat, defStat;
    Button strPlus, magPlus, hpPlus, defPlus;
    Button strMinus, magMinus, hpMinus, defMinus;

    int pts;
    TextView points;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog_levelup,null);

        builder.setView(view);

        Context context = getActivity();

        final SharedPreferences stats = context.getSharedPreferences("Stats",Context.MODE_PRIVATE);
        str = stats.getInt("Strength", 0);
        mag = stats.getInt("Magic",0);
        hp = stats.getInt("Health",100);
        def = stats.getInt("Defence",0);
        strLimit=str;
        magLimit=mag;
        hpLimit=hp;
        defLimit=def;

        strStat = view.findViewById(R.id.strengthStat);
        strStat.setText(String.valueOf(str));
        magStat = view.findViewById(R.id.magicStat);
        magStat.setText(String.valueOf(mag));
        hpStat = view.findViewById(R.id.hpStat);
        hpStat.setText(String.valueOf(hp));
        defStat = view.findViewById(R.id.defenceStat);
        defStat.setText(String.valueOf(def));

        pts=3;
        points=view.findViewById(R.id.points);

        strPlus = view.findViewById(R.id.strengthIncrement);
        strPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==0){}
                else{
                    str=str+1;
                    strStat.setText(String.valueOf(str));
                    pts=pts-1;
                    points.setText(String.valueOf(pts));
                }
            }
        });
        magPlus = view.findViewById(R.id.magicIncrement);
        magPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==0){}
                else{
                    mag=mag+1;
                    magStat.setText(String.valueOf(mag));
                    pts=pts-1;
                    points.setText(String.valueOf(pts));
                }
            }
        });
        hpPlus = view.findViewById(R.id.hpIncrement);
        hpPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==0){}
                else{
                    hp=hp+10;
                    hpStat.setText(String.valueOf(hp));
                    pts=pts-1;
                    points.setText(String.valueOf(pts));
                }
            }
        });
        defPlus = view.findViewById(R.id.defenceIncrement);
        defPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==0){}
                else{
                    def=def+1;
                    defStat.setText(String.valueOf(def));
                    pts=pts-1;
                    points.setText(String.valueOf(pts));
                }
            }
        });

        strMinus = view.findViewById(R.id.strengthDecrement);
        strMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==3 || str==strLimit){}
                else{
                    str=str-1;
                    strStat.setText(String.valueOf(str));
                    pts=pts+1;
                    points.setText(String.valueOf(pts));
                }
            }
        });
        magMinus = view.findViewById(R.id.magicDecrement);
        magMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==3 || mag==magLimit){}
                else{
                    mag=mag-1;
                    magStat.setText(String.valueOf(mag));
                    pts=pts+1;
                    points.setText(String.valueOf(pts));
                }
            }
        });
        hpMinus = view.findViewById(R.id.hpDecrement);
        hpMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==3 || hp==hpLimit){}
                else{
                    hp=hp-10;
                    hpStat.setText(String.valueOf(hp));
                    pts=pts+1;
                    points.setText(String.valueOf(pts));
                }
            }
        });
        defMinus = view.findViewById(R.id.defenceeDecrement);
        defMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pts==3 || def==defLimit){}
                else{
                    def=def-1;
                    defStat.setText(String.valueOf(def));
                    pts=pts+1;
                    points.setText(String.valueOf(pts));
                }
            }
        });

        Button positiveButton = view.findViewById(R.id.levelApply);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(pts!=0){}
                    else{
                        SharedPreferences.Editor editor = stats.edit();
                        editor.putInt("Strength",str);
                        editor.putInt("Magic",mag);
                        editor.putInt("Health", hp);
                        editor.putInt("Defence", def);
                        editor.apply();
                        dismiss();
                    }
                }
        });

        return builder.create();
    }
}
