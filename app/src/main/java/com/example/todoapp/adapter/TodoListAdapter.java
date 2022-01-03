package com.example.todoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.activity.EditTodoActivity;
import com.example.todoapp.database.DataAccess;
import com.example.todoapp.database.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    Context context;
    int resource;
    List<Todo> todolist;

    public TodoListAdapter(Context context, int resource, List<Todo> todolist) {
        this.context = context;
        this.resource = resource;
        this.todolist = todolist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo t = todolist.get(position);
        holder.taskname.setText(String.valueOf(t.getTaskName()));
        holder.taskdetail.setText(String.valueOf(t.getTaskDetails()));
        holder.taskstatus.setText(String.valueOf(t.getTaskStatus()));
        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTodoActivity.class);
                intent.putExtra("taskId", t.getTaskId());
                context.startActivity(intent);
            }
        });
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAccess.getInstance(context).deleteTodo(t);
                todolist.remove(t);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView taskname;
        TextView taskdetail;
        TextView taskstatus;
        ImageButton delbtn, editbtn;
        LinearLayout layoutDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskname = itemView.findViewById(R.id.taskname);
            taskdetail = itemView.findViewById(R.id.taskdetail);
            taskstatus = itemView.findViewById(R.id.taskstatus);
            editbtn = itemView.findViewById(R.id.editimgbtn);
            delbtn = itemView.findViewById(R.id.deleteimgbtn);
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (layoutDetails.getVisibility() == View.GONE)
                        layoutDetails.setVisibility(View.VISIBLE);
                    else
                        layoutDetails.setVisibility(View.GONE);
                }
            });
        }


    }
}
