package com.example.todoapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.todoapp.R;
import com.example.todoapp.adapter.TodoListAdapter;
import com.example.todoapp.database.model.Todo;

import java.util.ArrayList;


public class View extends AppCompatActivity {


    RecyclerView recyclerView;
    TodoListAdapter todoadapter;
    ArrayList<Todo> todolist=new ArrayList<Todo>();
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        recyclerView=findViewById(R.id.todorcv);
        Todo todo = new Todo();
        todo.setTaskId("1");
        todo.setTaskName("hello");
        todo.setTaskDetails("hello");
        todo.setUserID("101");
        todo.setTaskStatus("In-Progress");
        todolist.add(todo);

        todoadapter = new TodoListAdapter(View.this, R.layout.item_row,todolist);
        recyclerView.setAdapter(todoadapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));

    }
}