package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.Adapter.EmployeeAdapterView;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DetailsActivity.EmployeeDetailsActivity;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminToEmployee extends AppCompatActivity implements EmployeeAdapterView.OnRecyclerItemClickInterface{

    SearchView searchView;
    RecyclerView recyclerViewEmployee;
    ImageView imageView;
    EmployeeAdapterView employeeAdapterView;
    EmployeeAdapterView.OnRecyclerItemClickInterface onRecyclerItemClickInterface;
    public static List<EmployeeModel> modelList;
    EditText editTextName,editTextEmail,editTextPhone,editTextGender,editTextAddress,editTextJoiningDate,editTextBirthDate,editTextManger;
    Button buttonAddEmployee;

    String fetchUrl="https://benot.xyz/api/api/employees";

    String uploadUrl="https://benot.xyz/api/api/employees";


    String name,email,phone,gender,address,joiningDate,birthDate,manager;

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_employee);
        searchView=findViewById(R.id.searchViewEmployee);
        imageView=findViewById(R.id.employeeAddButton);
        recyclerViewEmployee=findViewById(R.id.recyclerViewEmployee);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.employeeAddButton)
                {
                    bottomSheetPlugin();
                }
            }
        });

        onRecyclerItemClickInterface=AdminToEmployee.this;
        recyclerViewEmployee.setHasFixedSize(true);
        recyclerViewEmployee.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelList=new ArrayList<>();

         requestQueue= Volley.newRequestQueue(this);

         searchView.setFocusable(false);
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {

                 employeeAdapterView.getFilter().filter(newText);

                 return false;
             }
         });




        loadData();


    }

    private void loadData() {
        modelList.clear();

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        EmployeeModel employeeModel=new EmployeeModel(jsonObjectReceive.getString("employee_id"),jsonObjectReceive.getString("name"),jsonObjectReceive.getString("email"),
                                jsonObjectReceive.getString("phone"),jsonObjectReceive.getString("gender"),jsonObjectReceive.getString("address"),
                                jsonObjectReceive.getString("joining_date"),jsonObjectReceive.getString("birth_date"),jsonObjectReceive.getString("manager"));

                        modelList.add(employeeModel);

                    }
                    employeeAdapterView=new EmployeeAdapterView(onRecyclerItemClickInterface,modelList);
                    employeeAdapterView.notifyDataSetChanged();
                    recyclerViewEmployee.setAdapter(employeeAdapterView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(stringRequest);


    }

    private void bottomSheetPlugin() {

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
        buttonAddEmployee=bottomSheetDialog.findViewById(R.id.addEmployeeButton);
        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, uploadUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.isEmpty())
                                    {
                                        Log.d("error", "onResponse: null ");

                                    }

                                    Log.d("error", "onResponse: "+response.toString());

                                        try {
                                            JSONObject jsonObject=new JSONObject(response);

                                            Toast.makeText(AdminToEmployee.this,response.toString(),Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //Toast.makeText(MainActivity.this,"Try Again"+error.toString(),Toast.LENGTH_SHORT).show();

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





                    searchView.clearFocus();
                    bottomSheetDialog.dismiss();
                    loadData();
                }

                else{
                    Toast.makeText(getApplicationContext(),"Fill up all field",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public void OnItemClick(int position) {

        Intent intent=new Intent(AdminToEmployee.this, EmployeeDetailsActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}