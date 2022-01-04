package com.example.todoapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.R;
import com.example.todoapp.activity.EditTodoActivity;
import com.example.todoapp.database.DataAccess;
import com.example.todoapp.database.model.Todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Deleting Task");
                builder.setMessage("Are you sure you want to delete?");
                builder.setNegativeButton("No", (dialog, view)->{dialog.dismiss();});
                builder.setPositiveButton("Yes", (dialog, view)->{
                    DataAccess.getInstance(context).deleteTodo(t);
                    todolist.remove(t);
                    notifyDataSetChanged();
                    dialog.dismiss();
                });
                builder.create().show();
            }
        });
        holder.ivStatus.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Task Status");
            builder.setMessage("Current Task Status: "+t.getTaskStatus()+"\nChange Task Status to?");
            builder.setNegativeButton("In-Progress", (dialog, view)->{
                t.setTaskStatus("In-Progress");
                DataAccess.getInstance(context).updateTodo(t, onComplete->{});
                notifyDataSetChanged();
                dialog.dismiss();
            });
            builder.setNeutralButton("Complete", (dialog, view)->{
                t.setTaskStatus("Done");
                DataAccess.getInstance(context).updateTodo(t, onComplete->{});
                notifyDataSetChanged();
                dialog.dismiss();
            });
            builder.setPositiveButton("Assigned", (dialog, view)->{
                t.setTaskStatus("Assigned");
                DataAccess.getInstance(context).updateTodo(t, onComplete->{});
                notifyDataSetChanged();
                dialog.dismiss();
            });
            builder.create().show();
        });
        if(t.getTaskStatus().equals("Assigned"))
            holder.ivStatus.setImageTintList(context.getColorStateList(R.color.grey));
        else if (t.getTaskStatus().equals("In-Progress"))
            holder.ivStatus.setImageTintList(context.getColorStateList(R.color.purple_700));
        else
            holder.ivStatus.setImageTintList(context.getResources().getColorStateList(R.color.green));
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
        ImageView ivStatus;
        LinearLayout layoutDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskname = itemView.findViewById(R.id.taskname);
            taskdetail = itemView.findViewById(R.id.taskdetail);
            taskstatus = itemView.findViewById(R.id.taskstatus);
            editbtn = itemView.findViewById(R.id.editimgbtn);
            delbtn = itemView.findViewById(R.id.deleteimgbtn);
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
            ivStatus = itemView.findViewById(R.id.ivStatus);
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
