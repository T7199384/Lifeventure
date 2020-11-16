package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.R;

import java.util.Date;

public class ScheduleTaskDialog extends AppCompatDialogFragment{

    private EditText tName, tDesc;
    private int tDiffInt;
    private SeekBar tDiffBar;
    private TextView tDiffText;
    private TaskScheduleListener listener;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_taskschedule, null);

        builder.setView(view);
        builder.setTitle("Schedule Task");
 /*       builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

        tName=view.findViewById(R.id.taskCreateName);
        tDesc=view.findViewById(R.id.taskCreateDesc);
        tDiffInt=0;
        tDiffBar=view.findViewById(R.id.difficultySlider);
        tDiffText=view.findViewById(R.id.difficultyText);
        datePicker=view.findViewById(R.id.datePicker);
        timePicker=view.findViewById(R.id.timePicker);

        tDiffBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0: tDiffText.setText("Difficulty: Very Easy"); tDiffInt=0; break;
                    case 1: tDiffText.setText("Difficulty: Easy"); tDiffInt=1; break;
                    case 2: tDiffText.setText("Difficulty: Medium"); tDiffInt=2;break;
                    case 3: tDiffText.setText("Difficulty: Hard"); tDiffInt=3; break;
                    case 4: tDiffText.setText("Difficulty: Very Hard"); tDiffInt=4;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button create = view.findViewById(R.id.finaliseTaskButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = tName.getText().toString();
                String uDesc = tDesc.getText().toString();
                String uStrDate = datePicker.getDayOfMonth()+"."+datePicker.getMonth()+"."+datePicker.getYear()+"\r"+timePicker.getHour()+":"+timePicker.getMinute();
                listener.scheApply(uName, uDesc,tDiffInt, uStrDate);
                //Date uDate = datePicker.getFirstDayOfWeek()+datePicker.getDayOfMonth()+datePicker.getYear();
                dismiss();
            }
        });

        Button cancel = view.findViewById(R.id.cancelTaskButton);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TaskScheduleListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface TaskScheduleListener{
        void scheApply(String uName, String uDesc, int tDiffInt,String uStrDate);
        }
}
