package com.example.itm704project;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class editNote extends AppCompatActivity {

    DatabaseHelper myDB;
    LinearLayout linearL;
    boolean skipPopup, sortN;
    Button sortNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        linearL = findViewById(R.id.linearLay);
        //linearL.setLayoutParams(Gravity.CENTER_HORIZONTAL);
        sortNew = findViewById(R.id.buttonShortNew);

        myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        skipPopup = (boolean) b.get("skipPopup");
        sortN = (boolean) b.get("sortNew");
        if(!skipPopup){
            Toast.makeText(this, "Press on note to edit", Toast.LENGTH_SHORT).show();
        }

        if(sortN){
            newSortButton();
        }else{
            addViews();
        }
        newSort();
    }

    public void newSortButton(){
        String[] noteArr = new String[myDB.getTotal()];
        String[] noteDate = new String[myDB.getTotal()];
        Cursor show = myDB.getAll();

        for(int i = 0; i < myDB.getTotal(); i++){
            show.moveToNext();
            noteArr[i] = show.getString(1); // store notes in array
            noteDate[i] = show.getString(2); // store notes in array
        }
        show.close();

        for(int j = myDB.getTotal()-1; j >= 0; j--){ //int i = myDB.getTotal()-1; i >= 0; i--
            TextView text = new TextView(editNote.this);
            final int num = j;
            text.setText(noteArr[j]);
            text.setTextSize(25);
            //text.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
            text.setId(j+1);

            myDB = new DatabaseHelper(editNote.this);
            myDB.getWritableDatabase();
            Cursor show1 = myDB.getAll();

            String tempId = "999";
            int i = 0, k = j+1;
            while(show1.moveToNext() && i < k){
                i++;
                tempId = (show1.getString(0)); // NOTE ID
            }
            final String id = tempId;

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(editNote.this, editActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("sortNew", sortN);
                    startActivity(intent);
                }
            });
            TextView divider = new TextView(editNote.this);
            divider.setText("_______________________\n");
            divider.setTextSize(25);
            divider.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView textDate = new TextView(editNote.this);
            textDate.setText(noteDate[j]);
            textDate.setTextSize(20);
            textDate.setTypeface(null, Typeface.BOLD);
            textDate.setGravity(Gravity.CENTER_HORIZONTAL);
            linearL.addView(textDate);
            linearL.addView(text);
            linearL.addView(divider);
        }
        //myDB.close();
    }

    public void newSort(){
        sortNew.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sortN){
                            Toast.makeText(editNote.this, "Notes already sorted by new", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent myIntent3 = new Intent(getApplicationContext(), editNote.class);
                            myIntent3.putExtra("skipPopup", true);
                            myIntent3.putExtra("sortNew", true);
                            startActivity(myIntent3);
                        }
                    }
                }

        );
    }

    public void addViews(){
        String[] noteArr = new String[myDB.getTotal()];
        String[] noteDate = new String[myDB.getTotal()];
        Cursor show = myDB.getAll();

        for(int i = 0; i < myDB.getTotal(); i++){
            show.moveToNext();
            noteArr[i] = show.getString(1); // store notes in array
            noteDate[i] = show.getString(2); // store notes in array
        }
        show.close();

        for(int j = 0; j < myDB.getTotal(); j++){
            TextView text = new TextView(this);
            final int num = j;
            text.setText(noteArr[j]);
            text.setTextSize(25);
            //text.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
            text.setId(j+1);

            myDB = new DatabaseHelper(this);
            myDB.getWritableDatabase();
            Cursor show1 = myDB.getAll();

            String tempId = "999";
            int i = 0, k = j+1;
            while(show1.moveToNext() && i < k){
                i++;
                tempId = (show1.getString(0)); // NOTE ID
            }
            final String id = tempId;

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(editNote.this, editActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("sortNew", sortN);
                    startActivity(intent);
                }
            });
            TextView divider = new TextView(this);
            divider.setText("_______________________\n");
            divider.setTextSize(25);
            divider.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView textDate = new TextView(this);
            textDate.setText(noteDate[j]);
            textDate.setTextSize(20);
            textDate.setTypeface(null, Typeface.BOLD);
            textDate.setGravity(Gravity.CENTER_HORIZONTAL);
            linearL.addView(textDate);
            linearL.addView(text);
            linearL.addView(divider);
        }
    }
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }
}
