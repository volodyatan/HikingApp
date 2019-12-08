package com.example.itm704project;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class deleteNote extends AppCompatActivity {

    DatabaseHelper myDB;
    LinearLayout linearL;
    ScrollView scrollV;
    boolean skipPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        linearL = findViewById(R.id.linearLayDel);

        myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        skipPopup = (boolean) b.get("skipPopup");
        if(skipPopup){
        }else {
            Toast.makeText(this, "Press on note to delete", Toast.LENGTH_SHORT).show();
        }
        addViews();
    }

    public void addViews(){
        String[] noteArr = new String[myDB.getTotal()];
        Cursor show = myDB.getAll();

        for(int i = 0; i < myDB.getTotal(); i++){
            show.moveToNext();
            noteArr[i] = show.getString(1); // store notes in array
        }
        show.close();

        for(int j = 0; j < myDB.getTotal(); j++){
            TextView text = new TextView(this);
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
                    AlertDialog.Builder confirm = new AlertDialog.Builder(deleteNote.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                        confirm.setCancelable(true);
                        confirm.setTitle("Confirm Delete?");
                        confirm.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(myDB.deleteOne(id)){
                                            Toast.makeText(deleteNote.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(getApplicationContext(), deleteNote.class);
                                            myIntent.putExtra("skipPopup", true);
                                            startActivity(myIntent);
                                        }
                                        else{
                                            Toast.makeText(deleteNote.this, "Note failed to delete", Toast.LENGTH_SHORT).show();
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
            });
            TextView divider = new TextView(this);
            divider.setText("_______________________\n");
            divider.setTextSize(25);
            divider.setGravity(Gravity.CENTER_HORIZONTAL);
            linearL.addView(text);
            linearL.addView(divider);
        }
    }
    /**
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
    }
    **/
}
