package com.example.androidshaper.companyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
            SharedPreferences sharedPreferences=getSharedPreferences("adminLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            String status=sharedPreferences.getString("status","off").toString().trim();
            if (status.equals("on"))
            {
//                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this, AdminPanelActivity.class);
                startActivity(intent);
            }
            else if (status.equals("off"))
            {
//                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this, AdminLogin.class);
                startActivity(intent);
            }


        }
        else if (v.getId()==R.id.dashboardEmployeeButton)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("userLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();

                String status=sharedPreferences.getString("status","off").toString().trim();

                if (status.equals("on"))
                {
//                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this, UserPanelActivity.class);
                    startActivity(intent);

                }
                else if (status.equals("off"))
                {
//                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this, UserLogin.class);
                    startActivity(intent);

                }




        }

    }
}