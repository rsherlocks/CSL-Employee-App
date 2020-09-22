package com.example.androidshaper.companyapplication.PanelActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTasks;
import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.R;

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        buttonTask=findViewById(R.id.taskAdminButton);
        buttonTask.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.taskAdminButton)
        {
            Intent intent=new Intent(this, AdminToTasks.class);
            startActivity(intent);
        }

    }
}