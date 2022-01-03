package com.example.todoapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.todoapp.R;
import com.example.todoapp.database.DataAccess;

public class MainActivity extends AppCompatActivity {

    DataAccess mDataAccess = DataAccess.getInstance(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}