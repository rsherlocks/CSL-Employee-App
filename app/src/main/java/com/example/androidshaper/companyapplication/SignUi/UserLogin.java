package com.example.androidshaper.companyapplication.SignUi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.androidshaper.companyapplication.MainActivity;
import com.example.androidshaper.companyapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText userMail,userPassword;
    private Button btnLogin,buttonReg;
    private ProgressBar loginProgress;
    private String url="http://192.168.1.101/company/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        buttonReg=findViewById(R.id.regBtn);
        loginProgress = findViewById(R.id.login_progress);
        buttonReg.setOnClickListener(this);
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

            if (mail.isEmpty() || password.isEmpty()) {
                showMessage("Please Verify All Field");
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            }
            else
            {
                signIn(mail,password);
            }

        }

       else  if (view.getId()==R.id.regBtn)
        {
            Intent regUser=new Intent(UserLogin.this, UserRegistration.class);
            startActivity(regUser);

        }

    }

    private void signIn(final String mail, final String password) {

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject= new JSONObject(response);

                            if(jsonObject.getString("code").equals("1"))
                            {
                                showMessage(jsonObject.getString("message"));
                                Intent intent=new Intent(UserLogin.this, MainActivity.class);
                                startActivity(intent);

                            }
                            else{
                                showMessage(jsonObject.getString("message"));
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