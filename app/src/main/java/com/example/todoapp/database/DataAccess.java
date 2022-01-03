package com.example.todoapp.database;

import android.content.Context;

import com.example.todoapp.database.model.Todo;
import com.example.todoapp.utility.Helper;

import java.util.Date;
import java.util.List;

public class DataAccess {

    static DataAccess dataAccess;
    LocalDatabase localDatabase;
    Context context;

    private DataAccess(Context context) {
        this.context = context;
        this.localDatabase = new LocalDatabase(context);
    }

    public static DataAccess getInstance(Context context) {
        if (dataAccess == null) {
            dataAccess = new DataAccess(context);
        }
        return dataAccess;
    }

    public String getUserId(Context context) {
        String userId = Helper.getStringSharedPreference("userId", context);
        if(null == userId || "".equals(userId)){
            userId = Helper.md5(new Date().toString());
            Helper.setStringSharedPreference("userId", userId, context);
        }
        return userId;
    }

    public void addTodo(Todo todo) {
        localDatabase.insertTask(todo);

    }

    public void deleteTodo(Todo todo) {
        localDatabase.deleteTask(todo);
    }

    public void updateTodo(Todo todo) {
        localDatabase.updateTask(todo);
    }

    public Todo getTodoById(String id){
        return localDatabase.getTaskById(id);
    }

    public List<Todo> getTodoList() {
        List<Todo> todoList = localDatabase.getTodoTasks();
        return todoList;
    }
}
