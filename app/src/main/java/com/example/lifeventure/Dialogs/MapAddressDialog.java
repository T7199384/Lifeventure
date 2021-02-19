package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.example.lifeventure.R;
import com.example.lifeventure.TaskActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapAddressDialog extends AppCompatDialogFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private View view;
    private Geocoder geocoder;
    List<Address> addressList;

    private EditText tName, tBuildingInfo;
    private int tDiffInt;
    private SeekBar tDiffBar;
    private TextView tDiffText;
    private TaskMapListener listener;
    private EditText editLocate, editPostcode;

    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        try {
            view = inflater.inflate(R.layout.layout_dialog_map, null);
        }
        catch (InflateException e){
            view=null;
            view = inflater.inflate(R.layout.layout_dialog_map, null);
        }
        builder.setView(view);
        builder.setTitle("Plan your journey");

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Context geoContext = this.getActivity();
        geocoder = new Geocoder( geoContext,Locale.getDefault());

        tName = view.findViewById(R.id.mapTask);
        tBuildingInfo = view.findViewById(R.id.buildingInfo);
        tDiffInt=0;
        tDiffBar=view.findViewById(R.id.difficultySlider);
        tDiffText=view.findViewById(R.id.difficultyText);

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

        Button search = view.findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressSearch();
            }
        });

        Button cancel = view.findViewById(R.id.cancelMapButton);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button create = view.findViewById(R.id.add);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = tName.getText().toString();
                String uAddress = editLocate.getText().toString()+", "+addressList.get(0).getAddressLine(0);
                listener.mapApply(uName, uAddress ,tDiffInt);

                dismiss();
            }
        });

        return builder.create();
    }

    public void addressSearch(){

        editLocate = view.findViewById(R.id.locateName);
        String location = editLocate.getText().toString();
        editPostcode = view.findViewById(R.id.postcode);
        String postcode = editPostcode.getText().toString();

        String fullSearch = location+", "+postcode;

        try {
            addressList=geocoder.getFromLocation(18.944620, 72.822278,1);

            String address = tBuildingInfo.toString()+", "+addressList.get(0).getAddressLine(0);

            String full = address;
            TextView test = view.findViewById(R.id.addressShow);
            test.setText(full);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addressList=geocoder.getFromLocationName(fullSearch,1);
            if (addressList==null){ }
            else{
                String searchLocation=addressList.get(0).getAddressLine(0);

                TextView test = view.findViewById(R.id.addressShow);
                test.setText(searchLocation);

                LatLng newMarker = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
                map.addMarker(new MarkerOptions()
                        .position(newMarker)
                        .title("Marker"));
                map.moveCamera(CameraUpdateFactory.newLatLng(newMarker));
                map.moveCamera(CameraUpdateFactory.zoomTo(20));
            }

        } catch (IOException e) {

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng sydney = new LatLng(-34,151);
        map.addMarker(new MarkerOptions()
        .position(sydney)
        .title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.moveCamera(CameraUpdateFactory.zoomTo(10));
        map.setBuildingsEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TaskMapListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface TaskMapListener{
        void mapApply (String uName, String uAddress, int tDiffInt);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = getFragmentManager().findFragmentById(R.id.map);
        if (f != null){
            getFragmentManager().beginTransaction().remove(f).commit();
        }
    }
}
