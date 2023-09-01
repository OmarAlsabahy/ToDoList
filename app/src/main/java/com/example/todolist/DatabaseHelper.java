package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Todo_List";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_NAME = "note";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    private static String QUERY = "CREATE TABLE " + TABLE_NAME + " ("+ COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_TITLE + " Text , "+
            COLUMN_BODY + " Text );";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("Drop Table If Exists "+ TABLE_NAME);

    }


    public  void addNote(String noteTitle , String noteBody) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_TITLE, noteTitle);
            cv.put(COLUMN_BODY, noteBody);

            Log.i("testingSaving", noteTitle);
            sqLiteDatabase.insert(TABLE_NAME, null, cv);
            Toast.makeText(context, "Note Added Successfully", Toast.LENGTH_LONG).show();

    }


    public List<Note> getAllNotes()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        List<Note> notes = new ArrayList<>();
        if (sqLiteDatabase !=null)
        {
            cursor = sqLiteDatabase.rawQuery(query , null);
        }

        while (cursor.moveToNext())
        {
            String title;
            String body;
            String id;
            Note note ;
            id = cursor.getString(0);
            title = cursor.getString(1);
            body = cursor.getString(2);
            Log.i("testingRetrieve" , title);
            note = new Note(title , body , id);
            notes.add(note);

        }

        return notes;
    }


    public void deleteItem(String id)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long result = sqLiteDatabase.delete(TABLE_NAME , "id="+id , null);
        if (result==-1)
        {
            Toast.makeText(context , "Failed!" , Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context , "Note Deleted Successfully" , Toast.LENGTH_LONG).show();
        }
    }


    public void updateItem(String id , String title , String body)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COLUMN_TITLE , title);
        cv.put(COLUMN_BODY , body);

      int result = sqLiteDatabase.update(TABLE_NAME , cv , "id=?" ,new String[]{id});
        Log.i("testUpdate" ,""+result);

           if (result==1) {

               Toast.makeText(context, "Note Updated Successfully", Toast.LENGTH_LONG).show();
               Log.i("testUpdate", title + " " + body + " " + id);
           }else {

               Toast.makeText(context, "Failed!", Toast.LENGTH_LONG).show();
           }


    }


    public List<Note> selectItems(String query)
    {
        String Query = "SELECT * FROM note WHERE title = ?";
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase!=null)
        {
            cursor = sqLiteDatabase.rawQuery(Query , new String[]{query});
        }

        while (cursor.moveToNext())
        {
            String title;
            String body;
            String id;
            Note note ;
            id = cursor.getString(0);
            title = cursor.getString(1);
            body = cursor.getString(2);
            Log.i("testingRetrieve" , body);
            note = new Note(title , body , id);
            notes.add(note);

        }



       return notes;
    }

    public void deleteAllItems(List<Note> notes){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        for (int i =0 ; i< notes.size() ; i++)
        {
            sqLiteDatabase.delete(TABLE_NAME , "id=?" , new String[]{notes.get(i).getId()});
        }



    }


    {

    }
}