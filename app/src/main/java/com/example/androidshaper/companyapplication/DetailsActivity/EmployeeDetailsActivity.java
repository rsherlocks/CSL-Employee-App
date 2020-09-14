package com.example.androidshaper.companyapplication.DetailsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToEmployee;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.R;

public class EmployeeDetailsActivity extends AppCompatActivity {

    TextView textViewName,textViewEmail,textViewPhone,textViewGender,textViewAddress,textViewJoinDate,textViewBirthDate,textViewManager;
    String name,email,phone,gender,address,joiningDate,birthDate,manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        textViewName=findViewById(R.id.textViewEmployeeDetailsName);
        textViewEmail=findViewById(R.id.textViewEmployeeDetailsEmail);
        textViewPhone=findViewById(R.id.textViewEmployeeDetailsPhone);
        textViewGender=findViewById(R.id.textViewEmployeeDetailsGender);
        textViewAddress=findViewById(R.id.textViewEmployeeDetailsAddress);
        textViewJoinDate=findViewById(R.id.textViewEmployeeDetailsJoinOfDate);
        textViewBirthDate=findViewById(R.id.textViewEmployeeDetailsBirthOfDate);
        textViewManager=findViewById(R.id.textViewEmployeeDetailsManager);


        int position=getIntent().getExtras().getInt("position");

        EmployeeModel employeeModel =AdminToEmployee.modelList.get(position);

        name=employeeModel.getName();
        email=employeeModel.getEmail();
        phone=employeeModel.getPhone();
        gender=employeeModel.getGender();

        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewPhone.setText(phone);
        textViewGender.setText(gender);
        textViewAddress.setText(employeeModel.getAddress());
        textViewJoinDate.setText(employeeModel.getJoining_date());
        textViewBirthDate.setText(employeeModel.getBirth_date());
        textViewManager.setText(employeeModel.getManager());
    }
}