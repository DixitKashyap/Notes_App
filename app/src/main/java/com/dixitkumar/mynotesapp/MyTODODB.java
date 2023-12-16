package com.dixitkumar.mynotesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyTODODB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NotesTODO_Database";

    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";

    private static final String TABLE_TODOLIST_NAME = "todolist_items";
    private static final String IS_CHECKED = "isChecked";
    private static final String TODOLIST_CONTENT = "todolist_content";
    public MyTODODB(@Nullable Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_TODOLIST_NAME+" ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+IS_CHECKED+" VARCAHR(20) ,"+TODOLIST_CONTENT +" VARCHAR(500) "+ " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TODOLIST_NAME);
        onCreate(db);
    }
    public void enterTodoItems(String isChecked,String todolist_content){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IS_CHECKED,isChecked);
        values.put(TODOLIST_CONTENT,todolist_content);

        database.insert(TABLE_TODOLIST_NAME,null,values);
    }
    public ArrayList<TODOItems> getTODOListItems(){
        ArrayList<TODOItems> todoItemsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_TODOLIST_NAME+" ORDER BY "+KEY_ID+" DESC" ,null);


        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String isChecked = cursor.getString(1);
            String content = cursor.getString(2);
            todoItemsArrayList.add(new TODOItems(id,isChecked+"",content));
        }
        return todoItemsArrayList;
    }
    public void updateTODOListItems(String id,String isChecked,String content){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IS_CHECKED,isChecked);
        values.put(TODOLIST_CONTENT,content);

        database.update(TABLE_TODOLIST_NAME,values,KEY_ID +" = " +id,null);
    }
    public void deleteTODOItems(String id){
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_TODOLIST_NAME,KEY_ID +" = " +id,null);
    }
}
