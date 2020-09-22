package com.example.androidshaper.companyapplication.PanelActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.DetailsActivity.EmployeeTaskDetailsActivity;
import com.example.androidshaper.companyapplication.R;

public class UserPanelActivity extends AppCompatActivity implements View.OnClickListener {


    Button buttonTask,buttonApproval,buttonDue,buttonAttendance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        buttonTask=findViewById(R.id.taskEmployeeButton);
        buttonTask.setOnClickListener(this);

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