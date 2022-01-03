package com.example.todoapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.database.DataAccess;
import com.example.todoapp.database.model.Todo;
import com.example.todoapp.utility.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Timestamp;
import java.util.Date;

public class EditTodoActivity extends AppCompatActivity {

    TextInputLayout etTaskName, etTaskDetails;
    MaterialButton btnProgress, btnAssigned, btnComplete;
    MaterialButton btnSave;
    DataAccess dataAccess;
    boolean edit = false;
    Todo todo= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        etTaskName = findViewById(R.id.etTaskName);
        etTaskDetails = findViewById(R.id.etTaskDetails);
        btnAssigned = findViewById(R.id.btnAssigned);
        btnComplete = findViewById(R.id.btnComplete);
        btnProgress = findViewById(R.id.btnProgress);
        btnSave = findViewById(R.id.btnSave);
        dataAccess=DataAccess.getInstance(getApplicationContext());

        if(getIntent().getStringExtra("taskId") != null && !"".equals(getIntent().getStringExtra("taskId")))
            edit = true;


        if(!edit){
            todo=new Todo();
            todo.setTaskId(Helper.md5(new Date().toString()));
            todo.setUserID(dataAccess.getUserId(getApplicationContext()));
        }else{
            todo = dataAccess.getTodoById(getIntent().getStringExtra("taskId"));
            etTaskName.getEditText().setText(todo.getTaskName());
            etTaskDetails.getEditText().setText(todo.getTaskDetails());
            if("Assigned".equals(todo.getTaskStatus()))
                btnAssigned.setChecked(true);
            else if("In-Progress".equals(todo.getTaskStatus()))
                btnProgress.setChecked(true);
            else if("Done".equals(todo.getTaskStatus()))
                btnComplete.setChecked(true);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickSave();
            }
        });

    }

    private void OnClickSave(){
      boolean error=errorcheck();
      if(!error)
      {

          todo.setTaskName(etTaskName.getEditText().getText().toString());
          todo.setTaskDetails(etTaskDetails.getEditText().getText().toString());
          if(btnAssigned.isChecked())
          {
              todo.setTaskStatus("Assigned");
          }
          else if(btnProgress.isChecked())
          {
              todo.setTaskStatus("In-Progress");
          }
          else if(btnComplete.isChecked())
          {
              todo.setTaskStatus("Done");
          }
          todo.setTaskCreatedDate(new Timestamp(new Date().getTime()));
          todo.setTaskSynced(Helper.isNetworkAvailable(getApplicationContext()));
          if(!edit)
            dataAccess.addTodo(todo);
          else
              dataAccess.updateTodo(todo);
          finish();
      }

    }

    private boolean errorcheck()
    {
        boolean error = false;
        if(TextUtils.isEmpty(etTaskName.getEditText().getText().toString())){
            etTaskName.setError("Task Name is Required");
            error = true;
        }else{
            etTaskName.setError(null);
        }
        if(TextUtils.isEmpty(etTaskDetails.getEditText().getText().toString())){
            etTaskDetails.setError("Task Details is Required");
            error = true;
        }else{
            etTaskDetails.setError(null);
        }
        if(!btnProgress.isChecked() && !btnAssigned.isChecked() && !btnComplete.isChecked()){
            Toast.makeText(this, "Status Required", Toast.LENGTH_LONG).show();
            error = true;
        }
        return error;
    }
}