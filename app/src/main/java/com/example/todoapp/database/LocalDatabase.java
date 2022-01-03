package com.example.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.database.model.Todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LocalDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "todo_app";
    private final String TABLE_TODO = "todo_task";
    private final String COL_USER_ID = "userId";
    private final String COL_TASK_ID = "taskId";
    private final String COL_TASK_NAME = "taskName";
    private final String COL_TASK_DETAILS = "taskDetails";
    private final String COL_TASK_STATUS = "taskStatus";
    private final String COL_TASK_SYNC_STATE = "taskSyncState";
    private final String COL_TASK_CREATE_DATE = "taskCreateDate";

    public LocalDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_TODO +"(" +
                COL_TASK_ID +" TEXT PRIMARY KEY,"+
                COL_USER_ID +" TEXT,"+
                COL_TASK_NAME +" TEXT,"+
                COL_TASK_DETAILS +" TEXT,"+
                COL_TASK_STATUS +" TEXT,"+
                COL_TASK_SYNC_STATE +" NUMBER,"+
                COL_TASK_CREATE_DATE +" LONG"+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TODO);
        onCreate(sqLiteDatabase);
    }

    public void insertTask(Todo todo){
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, todo.getUserID());
        values.put(COL_TASK_ID, todo.getTaskId());
        values.put(COL_TASK_NAME, todo.getTaskName());
        values.put(COL_TASK_DETAILS, todo.getTaskDetails());
        values.put(COL_TASK_STATUS, todo.getTaskStatus());
        values.put(COL_TASK_SYNC_STATE, todo.isTaskSynced());
        values.put(COL_TASK_CREATE_DATE, todo.getTaskCreatedDate().getTime());

        long newRowId = this.getWritableDatabase().insert(TABLE_TODO, null, values);
    }

    public void updateTask(Todo todo){
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, todo.getUserID());
        values.put(COL_TASK_ID, todo.getTaskId());
        values.put(COL_TASK_NAME, todo.getTaskName());
        values.put(COL_TASK_DETAILS, todo.getTaskDetails());
        values.put(COL_TASK_STATUS, todo.getTaskStatus());
        values.put(COL_TASK_SYNC_STATE, todo.isTaskSynced());
        values.put(COL_TASK_CREATE_DATE, todo.getTaskCreatedDate().getTime());

        long newRowId = this.getWritableDatabase().update(TABLE_TODO,  values, COL_TASK_ID + "=?",new String[]{todo.getTaskId()});
    }

    public List<Todo> getTodoTasks(){
        Cursor cursor = this.getReadableDatabase().query(TABLE_TODO,
                new String[]{COL_USER_ID, COL_TASK_ID, COL_TASK_NAME, COL_TASK_DETAILS, COL_TASK_STATUS, COL_TASK_CREATE_DATE, COL_TASK_SYNC_STATE},
                null, null, null, null,null);
        List<Todo> todoList = new ArrayList<>();
        while(cursor.moveToNext()){
            Todo todo = new Todo();
            todo.setUserID(cursor.getString(0));
            todo.setTaskId(cursor.getString(1));
            todo.setTaskName(cursor.getString(2));
            todo.setTaskDetails(cursor.getString(3));
            todo.setTaskStatus(cursor.getString(4));
            todo.setTaskCreatedDate(new Timestamp(cursor.getLong(5)));
            todo.setTaskSynced(cursor.getInt(6) == 1);
            todoList.add(todo);
        }
        return todoList;
    }

    public int deleteTask(Todo todo){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_TODO,
                COL_TASK_ID+" = ? ",
                new String[]{todo.getTaskId()});
    }

    public Todo getTaskById(String id){
        Cursor cursor = this.getReadableDatabase().query(TABLE_TODO,
                new String[]{COL_USER_ID, COL_TASK_ID, COL_TASK_NAME, COL_TASK_DETAILS, COL_TASK_STATUS, COL_TASK_CREATE_DATE, COL_TASK_SYNC_STATE},
                COL_TASK_ID +"=?", new String[]{id}, null, null,null);
        Todo todo = null;
        while(cursor.moveToNext()){
            todo = new Todo();
            todo.setUserID(cursor.getString(0));
            todo.setTaskId(cursor.getString(1));
            todo.setTaskName(cursor.getString(2));
            todo.setTaskDetails(cursor.getString(3));
            todo.setTaskStatus(cursor.getString(4));
            todo.setTaskCreatedDate(new Timestamp(cursor.getLong(5)));
            todo.setTaskSynced(cursor.getInt(6) == 1);
        }
        return todo;
    }
}
