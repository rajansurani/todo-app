package com.example.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.database.model.Todo;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>
{

    Context context;
    int resource;
    ArrayList<Todo> todolist;

    public TodoListAdapter(Context context, int resource, ArrayList<Todo> todolist) {
        this.context = context;
        this.resource = resource;
        this.todolist = todolist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo t=todolist.get(position);
        holder.taskid.setText(String.valueOf(t.getTaskId()));
        holder.taskname.setText(String.valueOf(t.getTaskName()));
        holder.taskdetail.setText(String.valueOf(t.getTaskDetails()));
        holder.taskstatus.setText(String.valueOf(t.getTaskStatus()));
        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView taskname;
        TextView taskdetail;
        TextView taskstatus;
        TextView taskid;
        ImageButton delbtn,editbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskid = itemView.findViewById(R.id.taskid);
            taskname=itemView.findViewById(R.id.taskname);
            taskdetail=itemView.findViewById(R.id.taskdetail);
            taskstatus=itemView.findViewById(R.id.taskstatus);
            editbtn=itemView.findViewById(R.id.editimgbtn);
            delbtn=itemView.findViewById(R.id.deleteimgbtn);
        }



    }
}
