package com.example.todoapp.database;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.database.model.Todo;
import com.example.todoapp.utility.Helper;
import com.example.todoapp.utility.OnCompleteListener;

import org.json.JSONArray;
import org.json.JSONObject;

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
        if (null == userId || "".equals(userId)) {
            userId = Helper.md5(new Date().toString());
            Helper.setStringSharedPreference("userId", userId, context);
        }
        return userId;
    }

    public void addTodo(Todo todo, OnCompleteListener<Boolean> onCompleteListener) {
        localDatabase.insertTask(todo);
        if (Helper.isNetworkAvailable(context)) {
            RequestQueue queue = Volley.newRequestQueue(context);


            JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.0.186:5000/task/insert", todo.toJson(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("ID:", response.toString());
                    onCompleteListener.OnComplete(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("TAG", "ONERRORresponse:" + error.getMessage());
                    todo.setTaskSynced(false);
                    localDatabase.updateTask(todo);
                    onCompleteListener.OnComplete(true);
                }


            });
            queue.add(jsonobjectRequest);
        } else {
            onCompleteListener.OnComplete(true);
        }
    }

    public void deleteTodo(Todo todo) {
        localDatabase.deleteTask(todo);
        if (Helper.isNetworkAvailable(context)) {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.0.186:5000/task/delete", todo.toJson(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("ID:", response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("TAG", "ONERRORresponse:" + error.getMessage());
                }


            });
            queue.add(jsonobjectRequest);
        }
    }

    public void updateTodo(Todo todo, OnCompleteListener<Boolean> onCompleteListener) {
        localDatabase.updateTask(todo);
        if (Helper.isNetworkAvailable(context)) {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.0.186:5000/task/update", todo.toJson(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("ID:", response.toString());
                    onCompleteListener.OnComplete(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("TAG", "ONERRORresponse:" + error.getMessage());
                    todo.setTaskSynced(false);
                    localDatabase.updateTask(todo);
                    onCompleteListener.OnComplete(true);
                }


            });
            queue.add(jsonobjectRequest);
        } else {
            onCompleteListener.OnComplete(true);
        }
    }


    public Todo getTodoById(String id) {
        return localDatabase.getTaskById(id);
    }

    public List<Todo> getTodoList() {
        List<Todo> todoList = localDatabase.getTodoTasks();
        return todoList;
    }

    public void syncData(OnCompleteListener<Boolean> onCompleteListener) {
        List<Todo> list = localDatabase.getasyncTodoTasks();
        JSONArray array = new JSONArray();
        for (Todo todo : list) {
            array.put(todo.toJson());
            todo.setTaskSynced(true);

        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest(Request.Method.POST, "http://192.168.0.186:5000/task/updateall", array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                onCompleteListener.OnComplete(true);
                Log.v("ID:", response.toString());
                Log.d("Listing", "onResponse: Sync COmplete");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("TAG", "on error update all:" + error.getMessage());
                for (Todo todo : list) {
                    array.put(todo.toJson());
                    todo.setTaskSynced(false);
                    localDatabase.updateTask(todo);
                }
            }


        });
        queue.add(jsonarrayRequest);
        //request update all
    }
}
