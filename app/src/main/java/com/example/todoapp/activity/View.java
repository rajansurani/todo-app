package com.example.todoapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.todoapp.R;
import com.example.todoapp.adapter.TodoListAdapter;
import com.example.todoapp.database.DataAccess;
import com.example.todoapp.database.model.Todo;

import java.util.ArrayList;
import java.util.List;


public class View extends AppCompatActivity {


    RecyclerView recyclerView;
    TodoListAdapter todoadapter;
    List<Todo> todolist=new ArrayList<Todo>();
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        recyclerView=findViewById(R.id.todorcv);

        todolist = DataAccess.getInstance(this).getTodoList();

        todoadapter = new TodoListAdapter(View.this, R.layout.item_row,todolist);
        recyclerView.setAdapter(todoadapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));


        findViewById(R.id.fabAdd).setOnClickListener(v->{
            startActivity(new Intent(this, EditTodoActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        todolist = DataAccess.getInstance(this).getTodoList();
        for(Todo todo : todolist){
            Log.d("TAG", "onResume: id= "+todo.getTaskId()+ todo.isTaskSynced());
        }
        todoadapter = new TodoListAdapter(View.this, R.layout.item_row,todolist);
        recyclerView.setAdapter(todoadapter);
    }
}