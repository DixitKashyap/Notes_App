package com.dixitkumar.mynotesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyNotesAppDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesApp_Database";

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Notes_Table";
    private static final String KEY_ID = "id";
    private static final String KEY_NOTES_TITLE = "Notes_Title";
    private static final String KEY_NOTES_CONTENT = "Notes_content";
    private static final String KEY_NOTES_TIME = "Notes_Time";

    public MyNotesAppDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE "+TABLE_NAME+" ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+KEY_NOTES_TITLE+" VARCAHR(200) ,"+KEY_NOTES_CONTENT +" VARCAHR(5000),"+KEY_NOTES_TIME+" VARCHAR(100) "+" ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
       onCreate(db);
    }
    public void enterNotes(String title,String content,String dateAndTime){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTES_TITLE,title);
        values.put(KEY_NOTES_CONTENT,content);
        values.put(KEY_NOTES_TIME,dateAndTime);

        database.insert(TABLE_NAME,null,values);
    }

    public ArrayList<Notes> getNotes(){
        ArrayList<Notes> notesArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_ID+" DESC",null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String timeAndDate = cursor.getString(3);

            notesArrayList.add(new Notes(id,title,content,timeAndDate));

        }
        return notesArrayList;

    }

    public void updateNotes(String id,String title,String content,String dateAndTime){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTES_TITLE,title);
        values.put(KEY_NOTES_CONTENT,content);
        values.put(KEY_NOTES_TIME,dateAndTime);

        database.update(TABLE_NAME,values,KEY_ID +" = " +id,null);
    }

    public void deleteNotes(String id){
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_NAME,KEY_ID +" = " +id,null);
    }
}
