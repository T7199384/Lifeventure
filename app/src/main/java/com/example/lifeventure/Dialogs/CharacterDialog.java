package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.R;

public class CharacterDialog extends AppCompatDialogFragment {
    private Button male,female;
    private int character;
    private CharacterListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog_character,null);

        builder.setView(view);
        builder.setTitle("Pick a character");

        male = view.findViewById(R.id.male);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                character = 0;
                listener.apply(character);
                dismiss();
            }
        });
        female = view.findViewById(R.id.female);
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                character = 1;
                listener.apply(character);
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CharacterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface CharacterListener{
        void apply(int uCharacter);
    }

}
