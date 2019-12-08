package com.example.itm704project;

import android.database.Cursor;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class mapAllNotes extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lon,lat;
    int noteCount;
    double currLon,currLat;
    GpsHelper currLoc;
    List<String> allNotes;
    List<String> allLocations;
    DatabaseHelper myDB;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_all_notes);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currLoc = new GpsHelper(getApplicationContext());

        allNotes = new ArrayList<>();
        allLocations = new ArrayList<>();

        myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();
        Cursor show = myDB.getAll();

        setLocation();

        noteCount = 0;
        for(int i = 0; i < myDB.getTotal(); i++){
            show.moveToNext();
            noteCount++;
            allNotes.add(show.getString(1)); // store notes in list
            allLocations.add(show.getString(3)); // store locations in list
        }
        show.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < noteCount; i++) {
            String lonlat = allLocations.get(i);
            if(lonlat.matches("0")) {
                continue; }
            String[] coord = lonlat.split(",");
            lat = Double.parseDouble(coord[0]);
            lon = Double.parseDouble(coord[1]);

            LatLng noteLocation = new LatLng(lat, lon);
            MarkerOptions marker = new MarkerOptions();
            marker.position(noteLocation);
            marker.title(allNotes.get(i));

            mMap.addMarker(marker);
        }
        LatLng currLocation = new LatLng(currLat, currLon);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, zoomLevel));
    }

    public void setLocation(){
        loc = currLoc.getLocation();
        if (loc != null) {
            currLat = loc.getLatitude();
            currLon = loc.getLongitude();
        }
    }
}
