package com.example.todoapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.R;
import com.example.todoapp.adapter.TodoListAdapter;
import com.example.todoapp.database.DataAccess;
import com.example.todoapp.database.model.Todo;
import com.example.todoapp.utility.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class View extends AppCompatActivity {


    RecyclerView recyclerView;
    TodoListAdapter todoadapter;
    List<Todo> todolist=new ArrayList<Todo>();
    String res;
    DataAccess dataAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        recyclerView=findViewById(R.id.todorcv);
        dataAccess=DataAccess.getInstance(getApplicationContext());

        recyclerView.setAdapter(todoadapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);

        findViewById(R.id.fabAdd).setOnClickListener(v->{
            startActivity(new Intent(this, EditTodoActivity.class));
        });

        if(Helper.isNetworkAvailable(getApplicationContext())) {
            dataAccess.syncData(v->getTodoList());
        }

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_200);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                try {
                   getTodoList();
                   mSwipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTodoList();

    }

    private void getTodoList() {
        if(Helper.isNetworkAvailable(this))
        {
            Log.d("Listing", "getTodoList: ");
            JSONObject jsonobject=new JSONObject();
            try {
                jsonobject.put("USERID",DataAccess.getInstance(this).getUserId(this));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue= Volley.newRequestQueue(this);


            JsonObjectRequest jsonobjectRequest=new JsonObjectRequest(Request.Method.POST,"http://192.168.0.186:5000/task/all", jsonobject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("ID:",response.toString());
                    JSONArray array = null;
                    try {
                        array = response.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    todolist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject json = array.getJSONObject(i);
                            Todo t = new
                                    Todo(json.getString("taskid"),
                                    json.getString("userid"),
                                    json.getString("taskname"),
                                    json.getString("taskdetails"),
                                    json.getString("taskstatus"),
                                    new Timestamp(json.getLong("datecreated"))

                            );
                            todolist.add(t);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    todoadapter = new TodoListAdapter(View.this, R.layout.item_row,todolist);
                    recyclerView.setAdapter(todoadapter);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("TAG","ONERRORresponse:"+error.getMessage());
                }


            });
            jsonobjectRequest.setShouldCache(false);

            queue.add(jsonobjectRequest);
        }else
        {
            todoadapter = new TodoListAdapter(View.this, R.layout.item_row,DataAccess.getInstance(this).getTodoList());
            recyclerView.setAdapter(todoadapter);
        }


    }


}