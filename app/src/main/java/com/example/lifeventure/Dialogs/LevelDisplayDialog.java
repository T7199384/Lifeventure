package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.R;

public class LevelDisplayDialog extends AppCompatDialogFragment {

    private TextView levelDisplay;
    private TextView expDisplay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_level, null);

        builder.setView(view);

        Bundle args = getArguments();
        int lvl = args.getInt("lvl",0);
        int exp = args.getInt("exp",0);

        levelDisplay=view.findViewById(R.id.levelDisplay);
        levelDisplay.setText(String.valueOf(lvl));
        expDisplay=view.findViewById(R.id.expDisplay);
        expDisplay.setText(String.valueOf(exp)+"/"+lvl*1000);

        return builder.create();

    }
}
