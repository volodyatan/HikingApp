package com.example.itm704project;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Button buttonAdd, buttonAllMap, buttonEdit, buttonDelete, buttonTest;
    DatabaseHelper myDB;
    boolean exitButton;
    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 123);

        //toolbar = findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAllMap = findViewById(R.id.buttonViewLoc);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        //buttonTest = findViewById(R.id.buttonTest);
        exitButton = false;

        addButton();
        viewMap();
        editButton();
        deleteButton();
        //testButton();
    }

    /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
 **/


    public void addButton(){
        buttonAdd.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent myIntent1 = new Intent(getApplicationContext(), addNote.class);
                        startActivity(myIntent1);
                    }
                }
        );
    }

    public void viewMap(){
        buttonAllMap.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(myDB.getTotal() == 0){
                            Toast.makeText(MainActivity.this, "Your note board is empty", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent myIntent2 = new Intent(getApplicationContext(), mapAllNotes.class);
                            startActivity(myIntent2);
                        }
                    }
                }
        );
    }

    public void editButton(){
        buttonEdit.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent myIntent3 = new Intent(getApplicationContext(), editNote.class);
                        myIntent3.putExtra("skipPopup", false);
                        myIntent3.putExtra("sortNew", false);
                        startActivity(myIntent3);
                    }
                }
        );
    }
    public void deleteButton(){
        buttonDelete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent myIntent4 = new Intent(getApplicationContext(), deleteNote.class);
                        myIntent4.putExtra("skipPopup", false);
                        startActivity(myIntent4);
                    }
                }
        );
    }
/**
    public void testButton(){
        buttonTest.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent myIntent5 = new Intent(getApplicationContext(), viewNote.class);
                        startActivity(myIntent5);
                    }
                }
        );
    }
*/
    @Override
    public void onBackPressed() {
        if (exitButton) {
            super.onBackPressed();
            return;
        }

        this.exitButton = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                exitButton =false;
            }
        }, 2000);
    }
}
