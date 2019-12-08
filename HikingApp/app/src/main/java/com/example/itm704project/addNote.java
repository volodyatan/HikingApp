package com.example.itm704project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class addNote extends AppCompatActivity {

    DatabaseHelper myDB;
    Button buttonSave, buttonTogLoc;
    EditText note;
    String strNote,strTime,strLoc, togLocOn, togLocOff;
    Date currTime;
    GpsHelper currLoc;
    Location loc;
    boolean exit, useLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        togLocOff = "Turn Location Off";
        togLocOn = "Turn Location On";

        exit = false;
        buttonSave = findViewById(R.id.buttonSave);
        buttonTogLoc = findViewById(R.id.includeLocation);
        note = findViewById(R.id.editTextAdd);
        currTime = Calendar.getInstance().getTime();
        currLoc = new GpsHelper(getApplicationContext());
        currLoc.getLocation();
        saveNote();

        buttonTogLoc.setText(togLocOff);
        useLoc = true;
        useLocationButton();
    }

    public void useLocationButton(){
        buttonTogLoc.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(useLoc) {
                            buttonTogLoc.setText(togLocOn);
                            useLoc = false;
                        }else{
                            buttonTogLoc.setText(togLocOff);
                            useLoc = true;
                        }
                    }
                }
        );
    }

    public void saveNote(){
        buttonSave.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        strNote = note.getText().toString();
                        strTime = currTime.toString();

                        myDB = new DatabaseHelper(addNote.this);
                        myDB.getWritableDatabase();

                        if(strNote.matches("")){
                            Toast.makeText(addNote.this, "Note is empty", Toast.LENGTH_SHORT).show();
                        }else {
                            if(useLoc) {
                                loc = currLoc.getLocation();
                                if (loc != null) {
                                    strLoc = Double.toString(loc.getLatitude()) + "," + Double.toString(loc.getLongitude());
                                    if (myDB.addData(strNote, strTime, strLoc)) {
                                        Toast.makeText(addNote.this, "Note Added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(addNote.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    AlertDialog.Builder confirm = new AlertDialog.Builder(addNote.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                    confirm.setCancelable(true);
                                    confirm.setTitle("Location Unavailable\nSave note with no location?");
                                    confirm.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (myDB.addData(strNote, strTime, "0")) {
                                                        Toast.makeText(addNote.this, "Note Added", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(addNote.this, "Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    confirm.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alert = confirm.create();
                                    alert.show();
                                }
                            }else{
                                if (myDB.addData(strNote, strTime, "0")) {
                                    Toast.makeText(addNote.this, "Note Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(addNote.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        }

        this.exit = true;
        Toast message = Toast.makeText(this, "Press back again to exit\nNote will not be saved", Toast.LENGTH_SHORT);
        TextView v = message.getView().findViewById(android.R.id.message);
        v.setGravity(Gravity.CENTER);
        message.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                exit = false;
            }
        }, 2500);
    }
}
