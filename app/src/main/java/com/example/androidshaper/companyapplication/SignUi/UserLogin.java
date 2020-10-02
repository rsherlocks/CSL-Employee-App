package com.example.androidshaper.companyapplication.SignUi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.MainActivity;
import com.example.androidshaper.companyapplication.PanelActivity.AdminPanelActivity;
import com.example.androidshaper.companyapplication.PanelActivity.UserPanelActivity;
import com.example.androidshaper.companyapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private String url="https://benot.xyz/api/api/employees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.login_progress);
        btnLogin.setOnClickListener(this);
        loginProgress.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.loginBtn)
        {
            loginProgress.setVisibility(View.VISIBLE);


            final String mail = userMail.getText().toString();
            final String password = userPassword.getText().toString();

            if (mail.isEmpty() || password.isEmpty()|| password.length()<6) {
                showMessage("Please Verify All Field");
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            }
            else
            {
                signIn(mail,password);
            }

        }



    }

    private void signIn(final String mail, final String password) {

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String checkMail,checkPassword,eId;
                        int m=0;

                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                                checkMail=jsonObjectReceive.getString("email").toString().trim();

                                checkPassword=jsonObjectReceive.getString("password").toString().trim();



                                if (mail.equals(checkMail) && password.equals(checkPassword))
                                {
                                    m++;
                                    eId=jsonObjectReceive.getString("employee_id").toString().trim();;
                                    SharedPreferences sharedPreferences=getSharedPreferences("userLogin", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putString("status","on");
                                    editor.putString("user","employee");
                                    editor.putString("eid",eId);

                                    editor.commit();
                                    Intent intent=new Intent(UserLogin.this, UserPanelActivity.class);

                                    startActivity(intent);
                                    finish();

                                }



                            }
                            if (m<1)
                            {
                                Toast.makeText(getApplicationContext(),"Your Email or Password is wrong",Toast.LENGTH_SHORT).show();

                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }







                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showMessage("Server Error");


                loginProgress.setVisibility(View.INVISIBLE);

            }
        }

        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params=new HashMap<String, String>();
                params.put("name",mail);
                params.put("password",password);
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void showMessage( String message) {
        {
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

        }
    }

}