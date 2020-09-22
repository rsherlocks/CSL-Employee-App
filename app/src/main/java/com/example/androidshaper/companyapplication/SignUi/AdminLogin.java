package com.example.androidshaper.companyapplication.SignUi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.PanelActivity.AdminPanelActivity;
import com.example.androidshaper.companyapplication.R;

public class AdminLogin extends AppCompatActivity {


    EditText editTextUsername,editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        editTextUsername=findViewById(R.id.adminlogin_mail);
        editTextPassword=findViewById(R.id.adminlogin_password);
        buttonLogin=findViewById(R.id.adminLoginBtn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUsername.getText().toString().equals("admin") && editTextPassword.getText().toString().equals("admin"))
                {
                    SharedPreferences sharedPreferences=getSharedPreferences("adminLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("status","on");
                    editor.commit();
                    Intent intent=new Intent(AdminLogin.this, AdminPanelActivity.class);
                    startActivity(intent);
                    finish();

                }

                else{
                    Toast.makeText(getApplicationContext(),"Your UserName and Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}