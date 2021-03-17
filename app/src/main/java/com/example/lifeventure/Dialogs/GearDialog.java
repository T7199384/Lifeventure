package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.R;

import java.util.ArrayList;

public class GearDialog extends AppCompatDialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_gear, null);

        builder.setView(view);

        TextView head = view.findViewById(R.id.headDisplay);
        TextView body = view.findViewById(R.id.bodyDisplay);
        TextView leg = view.findViewById(R.id.legDisplay);
        TextView foot = view.findViewById(R.id.footDisplay);
        TextView neck = view.findViewById(R.id.neckDisplay);
        TextView ring = view.findViewById(R.id.ringDisplay);
        TextView brace = view.findViewById(R.id.braceletDisplay);
        TextView total = view.findViewById(R.id.totalDisplay);

        TextView headItem = view.findViewById(R.id.headItem);
        TextView bodyItem = view.findViewById(R.id.bodyItem);
        TextView legItem = view.findViewById(R.id.legItem);
        TextView footItem = view.findViewById(R.id.feetItem);
        TextView neckItem = view.findViewById(R.id.neckItem);
        TextView ringItem = view.findViewById(R.id.ringItem);
        TextView braceItem = view.findViewById(R.id.wristItem);

        ArrayList<TextView> gearName = new ArrayList<TextView>(7);
        gearName.add(head); gearName.add(body);gearName.add(leg); gearName.add(foot);gearName.add(neck); gearName.add(ring);gearName.add(brace);
        ArrayList<TextView> itemName = new ArrayList<TextView>(7);
        itemName.add(headItem); itemName.add(bodyItem);itemName.add(legItem); itemName.add(footItem);itemName.add(neckItem); itemName.add(ringItem);itemName.add(braceItem);

        int totalDefence=0;
        Bundle args = getArguments();
        ArrayList<Integer> aGear = args.getIntegerArrayList("gear");
        for(int i=0; i<7; i++){
            //gearName.get(i).setText(String.valueOf(i));
            //totalDefence=totalDefence+i;
            gearName.get(i).setText(String.valueOf(aGear.get(i)));
            switch (aGear.get(i)){
                case 0: switch (i){
                    case 0:  itemName.get(i).setText("ragged cap"); break;
                    case 1:  itemName.get(i).setText("ragged shirt"); break;
                    case 2:  itemName.get(i).setText("ragged breeches"); break;
                    case 3:  itemName.get(i).setText("ragged shoes"); break;
                    case 4:  itemName.get(i).setText("ragged scarf"); break;
                    case 5:  itemName.get(i).setText("no ring"); break;
                    case 6:  itemName.get(i).setText("no bracelet"); break;
                } break;
                case 1: switch (i){
                    case 0:  itemName.get(i).setText("leather helmet"); break;
                    case 1:  itemName.get(i).setText("leather chest plate"); break;
                    case 2:  itemName.get(i).setText("leather leggings"); break;
                    case 3:  itemName.get(i).setText("leather boots"); break;
                    case 4:  itemName.get(i).setText("copper necklace"); break;
                    case 5:  itemName.get(i).setText("copper ring"); break;
                    case 6:  itemName.get(i).setText("copper bracelet"); break;
                } break;
            }
            totalDefence=totalDefence+aGear.get(i);
        }

        total.setText(String.valueOf(totalDefence));

        return builder.create();
    }
}
