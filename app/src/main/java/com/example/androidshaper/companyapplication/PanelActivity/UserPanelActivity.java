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

import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.DetailsActivity.EmployeeTaskDetailsActivity;
import com.example.androidshaper.companyapplication.R;

public class UserPanelActivity extends AppCompatActivity implements View.OnClickListener {


    Button buttonTask,buttonApproval,buttonDue,buttonAttendance;


    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        buttonTask=findViewById(R.id.taskEmployeeButton);
        toolbar=findViewById(R.id.toolbarEmployeePanel);
        setSupportActionBar(toolbar);
        buttonTask.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
            SharedPreferences sharedPreferences=getSharedPreferences("userLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString("status","off");
            editor.commit();
            finish();


        }
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.taskEmployeeButton)
        {
            Intent intent=new Intent(this, EmployeeToTask.class);
            startActivity(intent);

        }

    }
}