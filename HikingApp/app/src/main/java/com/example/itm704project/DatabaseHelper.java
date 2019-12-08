
package com.example.itm704project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotesDatabase.db";

    public static final String TABLE_NAME = "Notes";
    public static final String COL_1 = "noteText";
    public static final String COL_2 = "noteTime";
    public static final String COL_3 = "noteLoc";
    public static final String NOTE_ID = "noteID";
    public static int DATABASE_VERSION = 2;

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_1 + " TEXT, " +
                    COL_2 + " TEXT, " +
                    COL_3 + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Note, String Time, String Loc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Note);
        contentValues.put(COL_2, Time);
        contentValues.put(COL_3, Loc);
        //contentValues.put(COL_4, ID);
        long res = db.insert(TABLE_NAME, null, contentValues);
        if(-1 == res){
            db.close();
            return false;
        }else{db.close();
            return true;}
    }

    public boolean editData(String id, String Note, String Time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Note);
        contentValues.put(COL_2, Time);
        //contentValues.put(COL_3, Loc);
        db.update(TABLE_NAME, contentValues, "noteID = ?", new String[] {id});
        //db.close();
        return true;
    }
    public boolean editData(String id, String Note, String Time, String Loc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_1, Note);
        content.put(COL_2, Time);
        content.put(COL_3, Loc);
        String where ="noteID="+ String.valueOf(id);
        db.update("Notes", content, where, null);
        //db.update(TABLE_NAME, contentValues, "noteID = ?", new String[]{id});
        //db.close();
        return true;
    }

    public Cursor getAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor results = db.query(TABLE_NAME,null, null,null,null,null,null);

        //db.close();
        return results;
    }
    public Cursor getOne(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.query(TABLE_NAME, null,
                "noteID= ?", new String[]{id},
                null ,null, null);
        return result;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);

    }
    public boolean deleteOne(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Notes", "noteID=?", new String[]{id});
        return true;
    }

    public int getTotal(){
        int notesTotal = 0;
        Cursor show = getAll();

        while(show.moveToNext()){
            notesTotal++;
        }
        return notesTotal;
    }
}
