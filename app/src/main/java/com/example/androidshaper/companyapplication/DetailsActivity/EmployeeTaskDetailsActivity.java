package com.example.androidshaper.companyapplication.DetailsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTasks;
import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.R;

public class EmployeeTaskDetailsActivity extends AppCompatActivity {

    TextView textViewPId,textViewEId,textViewDescription,textViewDuo;

    TaskModel taskModel;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeee_task_details);

        textViewPId=findViewById(R.id.textViewTaskEmployeeDetailsProjectId);
        textViewEId=findViewById(R.id.textViewTaskEmployeeDetailsEmployeeId);
        textViewDuo=findViewById(R.id.textViewTaskEmployeeDetailsDuo);
        textViewDescription=findViewById(R.id.textViewTaskEmployeeDetailsDescription);

        position=getIntent().getExtras().getInt("position");
        taskModel= EmployeeToTask.taskModelsList.get(position);


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