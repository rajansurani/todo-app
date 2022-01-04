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
        if(Helper.isNetworkAvailable(context)){
            String url = context.getResources().getString(R.string.moneytap_url) + "/v3/partner/buildprofile";
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, customer,
                    response -> {
                        completeSingleListener.OnComplete(response.optString("customerId", ""));
                        if (!"".equals(response.optString("customerId", ""))) {

                        }
                    },
                    error -> {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                            Toast.makeText(context, jsonObject.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        completeSingleListener.OnComplete("");
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("accept", "application/json");
                    map.put("content-type", "application/json");
                    map.put("authorization", "Bearer " + moneyTapAccessToken);
                    return map;
                }
            };
            queue.add(jsonObjectRequest);
        }
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
