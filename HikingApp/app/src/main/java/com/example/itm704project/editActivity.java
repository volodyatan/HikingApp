package com.example.itm704project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class editActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    Button buttonEdit, mapButton, buttonTogLoc;
    EditText note;
    Date currTime;
    GpsHelper currLoc;
    Location loc;
    int noteNum;
    String noteStr, loca, strLoc, togLocOff, togLocOn;
    String[] noteArr, locArr;
    boolean exit, useLoc, sortN;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        exit = false;
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonTogLoc = findViewById(R.id.includeLocation);
        note = findViewById(R.id.editText);
        currLoc = new GpsHelper(getApplicationContext());

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();

        noteArr = new String[myDB.getTotal()];
        locArr = new String[myDB.getTotal()];
        Cursor show = myDB.getAll();
        //myDB.close();

        for(int i = 0; i < myDB.getTotal(); i++){
            show.moveToNext();
            noteArr[i] = show.getString(1); // store notes in array
            locArr[i] = show.getString(3);
        }
        show.close();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null){
            sortN = (boolean) b.get("sortNew");
            noteStr = (String) b.get("id");
            noteNum = Integer.parseInt(noteStr);
        }

        Cursor getNote = myDB.getOne(noteStr);
        getNote.moveToNext();
        String n = getNote.getString(1);
        loca = getNote.getString(3);
        note.setText(n);

        //saveChanges(Integer.toString(noteNum), noteArr[noteNum], currTime.toString(), loc.toString());
        editNote();

        mapButton = findViewById(R.id.mapButton);
        showMap();

        togLocOff = "Change Location: On";
        togLocOn = "Change Location: Off";
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

    public void showMap(){
        mapButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        if(loca.matches("0")){
                            Toast.makeText(editActivity.this, "No location for this note", Toast.LENGTH_SHORT).show();
                        }else {
                            if (loca != null) {
                                Intent myIntent = new Intent(getApplicationContext(), viewMap.class);
                                myIntent.putExtra("longlat", loca);
                                startActivity(myIntent);

                            } else {
                                Toast.makeText(editActivity.this, "Location unavailable", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }
        );
    }

    public void editNote(){
        buttonEdit.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String noteStr = note.getText().toString();
                        final String finalNoteStr = noteStr;
                        loc = currLoc.getLocation();
                        currTime = Calendar.getInstance().getTime();

                        myDB = new DatabaseHelper(editActivity.this);
                        myDB.getWritableDatabase();

                        if(noteStr.matches("")){
                            Toast.makeText(editActivity.this, "Note is empty", Toast.LENGTH_SHORT).show();
                        }else {
                            if(useLoc) {
                                if (loc != null) {
                                    AlertDialog.Builder confirm = new AlertDialog.Builder(editActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                    confirm.setCancelable(true);
                                    confirm.setTitle("Location will be changed\nContinue?");
                                    confirm.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    strLoc = Double.toString(loc.getLatitude()) + "," + Double.toString(loc.getLongitude());
                                                    if (myDB.editData(Integer.toString(noteNum), finalNoteStr, currTime.toString(), strLoc)) {
                                                        Toast message = Toast.makeText(editActivity.this, "Note Edited\nLocation Changed", Toast.LENGTH_SHORT);
                                                        TextView vi = message.getView().findViewById(android.R.id.message);
                                                        vi.setGravity(Gravity.CENTER);
                                                        message.show();

                                                        //myDB.close();

                                                        Intent myIntent = new Intent(getApplicationContext(), editNote.class);
                                                        myIntent.putExtra("skipPopup", true);
                                                        myIntent.putExtra("sortNew", sortN);
                                                        startActivity(myIntent);
                                                    } else {
                                                        Toast.makeText(editActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

                                } else {
                                    AlertDialog.Builder confirm = new AlertDialog.Builder(editActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                    confirm.setCancelable(true);
                                    confirm.setTitle("Location Unavailable\nLocation will not be changed");
                                    confirm.setPositiveButton("Continue",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (myDB.editData(Integer.toString(noteNum), finalNoteStr, currTime.toString())) {
                                                        //myDB.close();
                                                        Toast.makeText(editActivity.this, "Note Edited", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(getApplicationContext(), editNote.class);
                                                        myIntent.putExtra("skipPopup", true);
                                                        myIntent.putExtra("sortNew", sortN);
                                                        startActivity(myIntent);
                                                    } else {
                                                        Toast.makeText(editActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
                                if (myDB.editData(Integer.toString(noteNum), noteStr, currTime.toString())) {
                                    //myDB.close();
                                    Toast.makeText(editActivity.this, "Note Edited", Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(getApplicationContext(), editNote.class);
                                    myIntent.putExtra("skipPopup", true);
                                    myIntent.putExtra("sortNew", sortN);
                                    startActivity(myIntent);
                                } else {
                                    Toast.makeText(editActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
            Intent myIntent = new Intent(getApplicationContext(), editNote.class);
            myIntent.putExtra("skipPopup", true);
            myIntent.putExtra("sortNew", sortN);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            return;
        }

        this.exit = true;
        Toast message = Toast.makeText(this, "Press back again to cancel editing\nChanges will not be saved", Toast.LENGTH_SHORT);
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
