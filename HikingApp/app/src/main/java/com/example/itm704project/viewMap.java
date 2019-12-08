package com.example.itm704project;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class viewMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lon,lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null){
            String lonlat = (String) b.get("longlat");
                String[] coord = lonlat.split(",");
                lat = Double.parseDouble(coord[0]);
                lon = Double.parseDouble(coord[1]);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng noteLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(noteLocation).title("Note Location"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(noteLocation, zoomLevel));
    }
}
