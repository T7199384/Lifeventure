package com.example.lifeventure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateTaskDialog extends AppCompatDialogFragment {

    private EditText tName, tDesc;
    private SeekBar tDiffBar;
    private TextView tDiffText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_taskcreate, null);

        builder.setView(view);
        builder.setTitle("Create Task");
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        tName=view.findViewById(R.id.taskCreateName);
        tDesc=view.findViewById(R.id.taskCreateDesc);
        tDiffBar=view.findViewById(R.id.difficultySlider);
        tDiffText=view.findViewById(R.id.difficultyText);

        tDiffBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 1: tDiffText.setText("Difficulty: Very Easy");
                    case 2: tDiffText.setText("Difficulty: Easy");
                    case 3: tDiffText.setText("Difficulty: Medium");
                    case 4: tDiffText.setText("Difficulty: Hard");
                    case 5: tDiffText.setText("Difficulty: Very Hard");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return builder.create();    }
}
