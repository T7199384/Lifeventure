package com.example.lifeventure.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.lifeventure.R;
import com.example.lifeventure.TaskActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapAddressDialog extends AppCompatDialogFragment implements OnMapReadyCallback {

    private Context context;
    private GoogleMap map;
    private View view;
    private Geocoder geocoder;
    List<Address> addressList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.layout_dialog_map,null);

        builder.setView(view);
        builder.setTitle("Plan your journey");

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Context geoContext = this.getActivity();
        geocoder = new Geocoder( geoContext,Locale.getDefault());

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

        return builder.create();
    }

    public void addressSearch(){

        EditText editLocate = view.findViewById(R.id.locateName);
        String location = editLocate.getText().toString();
        EditText editPostcode = view.findViewById(R.id.postcode);
        String postcode = editPostcode.getText().toString();

        String fullSearch = location+", "+postcode;

        try {
            addressList=geocoder.getFromLocation(18.944620, 72.822278,1);

            String address = addressList.get(0).getAddressLine(0);

            String full = address;
            TextView test = view.findViewById(R.id.mrTest);
            test.setText(full);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addressList=geocoder.getFromLocationName(fullSearch,1);
            if (addressList==null){ }
            else{
                String searchLocation=addressList.get(0).getAddressLine(0);

                TextView test = view.findViewById(R.id.mrTest);
                test.setText(searchLocation);
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
    }
}
