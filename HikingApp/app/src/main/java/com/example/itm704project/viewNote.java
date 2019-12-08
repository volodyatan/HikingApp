package com.example.itm704project;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class viewNote extends AppCompatActivity {

    DatabaseHelper myDB;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        textView = findViewById(R.id.textView);

        myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();

        StringBuffer buffer = new StringBuffer();
        Cursor show = myDB.getAll();

        while(show.moveToNext()){
            buffer.append(show.getString(0)+ "\n"); // NOTE ID
            buffer.append(show.getString(1)+ "\nDate: "); // NOTE
            buffer.append(show.getString(2)+ "\nLocation: "); // DATE/TIME
            buffer.append(show.getString(3)+ "\n_________\n"); // THIS IS THE LOCATION
        }

        textView.setText(buffer.toString());


    }

    public boolean addNotes(){
        return true;
    }
}
