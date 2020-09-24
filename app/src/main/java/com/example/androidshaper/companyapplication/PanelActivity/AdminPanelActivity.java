package com.example.androidshaper.companyapplication.PanelActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToAttendance;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToEmployee;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToProjectActivity;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTasks;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTeam;
import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.R;

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonTask,buttonEmployee,buttonProject,buttonTeam,buttonAttendance;
   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        buttonTask=findViewById(R.id.taskAdminButton);
        buttonProject=findViewById(R.id.projectButtonManagement);
        buttonEmployee=findViewById(R.id.employeeButtonManagement);
        buttonTeam=findViewById(R.id.teamButton);
        buttonAttendance=findViewById(R.id.attendanceButton);
        toolbar=findViewById(R.id.toolbarAdminPanel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonProject.setOnClickListener(this);
        buttonEmployee.setOnClickListener(this);
        buttonTask.setOnClickListener(this);
        buttonTeam.setOnClickListener(this);
        buttonAttendance.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();

        }
        else if (item.getItemId()==R.id.signOut)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("adminLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString("status","off");
            editor.commit();
            finish();


        }
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.taskAdminButton)
        {
            Intent intent=new Intent(this, AdminToTasks.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.employeeButtonManagement)
        {
            Intent intent=new Intent(this, AdminToEmployee.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.projectButtonManagement)
        {
            Intent intent=new Intent(this, AdminToProjectActivity.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.teamButton)
        {
            Intent intent=new Intent(this, AdminToTeam.class);
            intent.putExtra("check",0);
            startActivity(intent);
        }
        else if (v.getId()==R.id.attendanceButton)
        {
            Intent intent=new Intent(this, AdminToAttendance.class);
            startActivity(intent);
        }


    }
}