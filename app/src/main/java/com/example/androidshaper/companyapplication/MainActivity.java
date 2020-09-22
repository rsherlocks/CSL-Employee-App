package com.example.androidshaper.companyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTasks;
import com.example.androidshaper.companyapplication.PanelActivity.AdminPanelActivity;
import com.example.androidshaper.companyapplication.PanelActivity.UserPanelActivity;
import com.example.androidshaper.companyapplication.SignUi.AdminLogin;
import com.example.androidshaper.companyapplication.SignUi.UserLogin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAdmin,buttonEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdmin=findViewById(R.id.dashboardAdminButton);
        buttonEmployee=findViewById(R.id.dashboardEmployeeButton);

        buttonAdmin.setOnClickListener(this);
        buttonEmployee.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.dashboardAdminButton)
        {
            Intent intent=new Intent(this, AdminLogin.class);
            startActivity(intent);

        }
        else if (v.getId()==R.id.dashboardEmployeeButton)
        {
            Intent intent=new Intent(this, UserLogin.class);
            startActivity(intent);
        }

    }
}