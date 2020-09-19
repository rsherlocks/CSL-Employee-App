package com.example.androidshaper.companyapplication.DetailsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToEmployee;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployeeDetailsActivity extends AppCompatActivity {

    TextView textViewName,textViewEmail,textViewPhone,textViewGender,textViewAddress,textViewJoinDate,textViewBirthDate,textViewManager;


    Toolbar toolbar;

    int position;

    RequestQueue requestQueue;

    EditText editTextName,editTextEmail,editTextPhone,editTextGender,editTextAddress,editTextJoiningDate,editTextBirthDate,editTextManger;
    Button buttonAddEmployee;

    String editUrl="https://benot.xyz/api/api/employees/";


    EmployeeModel employeeModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        toolbar=findViewById(R.id.toolbarEmployee);

        textViewName=findViewById(R.id.textViewEmployeeDetailsName);
        textViewEmail=findViewById(R.id.textViewEmployeeDetailsEmail);
        textViewPhone=findViewById(R.id.textViewEmployeeDetailsPhone);
        textViewGender=findViewById(R.id.textViewEmployeeDetailsGender);
        textViewAddress=findViewById(R.id.textViewEmployeeDetailsAddress);
        textViewJoinDate=findViewById(R.id.textViewEmployeeDetailsJoinOfDate);
        textViewBirthDate=findViewById(R.id.textViewEmployeeDetailsBirthOfDate);
        textViewManager=findViewById(R.id.textViewEmployeeDetailsManager);
        requestQueue= Volley.newRequestQueue(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




         position=getIntent().getExtras().getInt("position");

       employeeModel =AdminToEmployee.modelList.get(position);
        editUrl=editUrl+employeeModel.getEmployee_id();
        String name,email,phone,gender,address,joiningDate,birthDate,manager;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId()==android.R.id.home)
        {
            finish();

        }
        else if (item.getItemId()==R.id.deleteId)
        {

            new AlertDialog.Builder(this)
                    .setMessage("Do you want to delete this Note?")
                    .setTitle("WARNING!!!")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //delete function called

                            deleteEmployee();
                            finish();



                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    })
                    .show();


        }
        else if (item.getItemId()==R.id.editId)
        {
            editEmployee();
        }
        return true;

    }

    private void deleteEmployee() {

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, editUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty())
                        {
                            Log.d("error", "onResponse: null ");

                        }

                        Log.d("error", "onResponse: "+response.toString());


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Try Again"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(stringRequest);

    }

    private void editEmployee() {



        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetEmployee));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
        editTextName=bottomSheetDialog.findViewById(R.id.editTextEmployeeName);
        editTextEmail=bottomSheetDialog.findViewById(R.id.editTextEmployeeEmail);
        editTextPhone=bottomSheetDialog.findViewById(R.id.editTextEmployeePhone);
        editTextGender=bottomSheetDialog.findViewById(R.id.editTextEmployeeGender);
        editTextAddress=bottomSheetDialog.findViewById(R.id.editTextEmployeeAddress);
        editTextJoiningDate=bottomSheetDialog.findViewById(R.id.editTextEmployeeJoiningDate);
        editTextBirthDate=bottomSheetDialog.findViewById(R.id.editTextEmployeeBirthDate);
        editTextManger=bottomSheetDialog.findViewById(R.id.editTextEmployeeManager);
        editTextName.setText(employeeModel.getName());
        editTextEmail.setText(employeeModel.getEmail());
        editTextGender.setText(employeeModel.getGender());
        editTextAddress.setText(employeeModel.getAddress());
        editTextPhone.setText(employeeModel.getPhone());
        editTextJoiningDate.setText(employeeModel.getJoining_date());
        editTextBirthDate.setText(employeeModel.getBirth_date());
        editTextManger.setText(employeeModel.getManager());
        buttonAddEmployee=bottomSheetDialog.findViewById(R.id.addEmployeeButton);
        buttonAddEmployee.setText("Edit Employee");
        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String name,email,phone,gender,address,joiningDate,birthDate,manager;

                name=editTextName.getText().toString().trim();
                email=editTextEmail.getText().toString().trim();
                phone=editTextPhone.getText().toString().trim();
                gender=editTextGender.getText().toString().trim();
                address=editTextAddress.getText().toString().trim();
                joiningDate=editTextJoiningDate.getText().toString().trim();
                birthDate=editTextBirthDate.getText().toString().trim();
                manager=editTextManger.getText().toString().trim();



                if (!name.isEmpty()&&!email.isEmpty()&&!phone.isEmpty()&&!gender.isEmpty()&&!address.isEmpty()&&!joiningDate.isEmpty())
                {

                    StringRequest stringRequest=new StringRequest(Request.Method.PUT, editUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.isEmpty())
                                    {
                                        Log.d("error", "onResponse: null ");

                                    }

                                    Log.d("Edit", "onResponse: "+response.toString());

                                    try {
                                        JSONObject jsonObjectData=new JSONObject(response);
                                        String jsonString=jsonObjectData.getString("data");
                                        JSONObject jsonObject=new JSONObject(jsonString);

                                        //Toast.makeText(getApplicationContext(),jsonString,Toast.LENGTH_SHORT).show();
                                        textViewName.setText(jsonObject.getString("name"));
                                        textViewEmail.setText(jsonObject.getString("email"));
                                        textViewPhone.setText(jsonObject.getString("phone"));
                                        textViewGender.setText(jsonObject.getString("gender"));
                                        textViewAddress.setText(jsonObject.getString("address"));
                                        textViewJoinDate.setText(jsonObject.getString("joining_date"));
                                        textViewBirthDate.setText(jsonObject.getString("birth_date"));
                                        textViewManager.setText(jsonObject.getString("manager"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(),"Try Again"+error.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String,String> params=new HashMap<String, String>();
                            params.put("name",name);
                            params.put("email",email);
                            params.put("phone",phone);
                            params.put("gender",gender);
                            params.put("address",address);
                            params.put("joining_date",joiningDate);
                            params.put("birth_date",birthDate);
                            params.put("manager",manager);
                            params.put("password","something");
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);


                    bottomSheetDialog.dismiss();

                }

                else{
                    Toast.makeText(getApplicationContext(),"Fill up all field",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}