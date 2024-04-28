package com.example.todolisttest.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolisttest.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static  final String DATABASE_NAME = "TODO_DATABASE";
    private static  final String TABLE_NAME = "TODO_TABLE";
    private static  final String COL_1 = "ID";
    private static  final String COL_2 = "TASK";
    private static  final String COL_3 = "STATUS";
    private static final String COL_4 = "PRIORITY";
    private static final String COL_5 = "DEADLINE";


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " INTEGER, " +
                COL_4 + " INTEGER, " +
                COL_5 + " LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(ToDoModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        values.put(COL_3, model.getStatus());
        values.put(COL_4, model.getPriority());
        values.put(COL_5, model.getDeadline());
        db.insert(TABLE_NAME, null, values);
    }

    public void updateTask(int id, String task, int priority) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        values.put(COL_4, priority);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cursor.getInt(cursor.getColumnIndex(COL_1))); // Akses ID
                    task.setTask(cursor.getString(cursor.getColumnIndex(COL_2))); // Akses TASK
                    task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3))); // Akses STATUS
                    task.setPriority(cursor.getInt(cursor.getColumnIndex(COL_4))); // Akses PRIORITY
                    task.setDeadline(cursor.getLong(cursor.getColumnIndex(COL_5))); // Akses DEADLINE
                    modelList.add(task);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return modelList;
    }
}