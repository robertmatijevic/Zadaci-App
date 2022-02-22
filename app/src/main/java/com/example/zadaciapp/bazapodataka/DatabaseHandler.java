package com.example.zadaciapp.bazapodataka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.zadaciapp.modeli.Task;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksManager";
    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_DONE = "done";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE =
                " CREATE TABLE " + TABLE_TASKS + " ( "
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + KEY_NAME + " TEXT, "
                        + KEY_DESCRIPTION + " TEXT, "
                        + KEY_DATE + " TEXT, "
                        + KEY_DONE + " INTEGER " + ")";
        Log.i("prova1", "Kreirana tablica nije jos");
        db.execSQL(CREATE_TASKS_TABLE);

        Log.i("prova", "Kreirana tablica");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void addTask(Task task){
        Log.i("test" ,"Try to add");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_DONE, task.getDone());

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public Task getTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TASKS,
                new String[] {
                        KEY_ID,
                        KEY_NAME,
                        KEY_DESCRIPTION,
                        KEY_DATE,
                        KEY_DONE
                },
                KEY_ID + "=?",
                new String [] {String.valueOf(id)},
                null,
                null,
                null,
                null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Task task = new Task(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                Integer.parseInt(cursor.getString((4))));

        return task;
    }

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setDone(Integer.parseInt(cursor.getString(4)));

                taskList.add(task);
            } while(cursor.moveToNext());
        }


        return taskList;
    }

    public int getTasksCount(){
        String countQuery = "SELECT * FROM" +TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_DONE, task.getDone());

        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(task.getId())});
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] {String.valueOf(task.getId())});
        db.close();
    }
}
