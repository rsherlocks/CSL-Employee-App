package com.example.androidshaper.companyapplication.DetailsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToProjectActivity;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTasks;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.R;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView textViewPId,textViewEId,textViewDescription,textViewDuo;

    int position;
    TaskModel taskModel;
    RequestQueue requestQueue;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        textViewPId=findViewById(R.id.textViewTaskDetailsProjectId);
        textViewEId=findViewById(R.id.textViewTaskDetailsEmployeeId);
        textViewDuo=findViewById(R.id.textViewTaskDetailsDuo);
        textViewDescription=findViewById(R.id.textViewTaskDetailsDescription);

        toolbar=findViewById(R.id.toolbarTask);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        position=getIntent().getExtras().getInt("position");
        requestQueue= Volley.newRequestQueue(this);
        taskModel= AdminToTasks.taskModelsList.get(position);

        loadData();

    }

    private void loadData() {

        textViewPId.setText("Project Id: "+taskModel.getProject_id());
        textViewEId.setText("Employee Id: "+taskModel.getEmployee_id());
        textViewDescription.setText(" Description: "+taskModel.getDescription());
        if (taskModel.getDue().equals("1"))
        {
            textViewDuo.setText("True");
        }
        else
        {
            textViewDuo.setText("False");

        }
    }
}