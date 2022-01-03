package com.example.todoapp.database;

import android.content.Context;

import com.example.todoapp.database.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    static DataAccess dataAccess;
    Context context;

    private DataAccess(Context context) {
        this.context = context;
    }

    public static DataAccess getInstance(Context context){
        if(dataAccess == null){
            dataAccess = new DataAccess(context);
        }
        return dataAccess;
    }

    public String getUserId(){
        String userId ="";


        return userId;
    }

    public void addTodo(Todo todo){

    }

    public void deleteTodo(Todo todo){

    }

    public void updateTodo(Todo todo){

    }

    public List<Todo> getTodoList(){
        List<Todo> todoList = new ArrayList<>();
        return todoList;
    }
}
