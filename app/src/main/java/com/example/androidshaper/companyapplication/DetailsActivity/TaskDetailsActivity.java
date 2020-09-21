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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToProjectActivity;
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTasks;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView textViewPId,textViewEId,textViewDescription,textViewDuo;
    EditText editTextDescription,editTextDuo;
    Spinner spinnerEmployee,spinnerProject;
    ArrayAdapter arrayAdapterSpinnerProject,arrayAdapterSpinnerEmployee;

    int position;
    TaskModel taskModel;
    RequestQueue requestQueue;
    String editUrl="https://benot.xyz/api/api/tasks/";

    String fetchProject="https://benot.xyz/api/api/projects";
    String fetchEmployee="https://benot.xyz/api/api/employees";

    ArrayList<String> projectIdList=new ArrayList<>();
    ArrayList<String> employeeIdList=new ArrayList<>();

    String description,duo,projectId,eId;
    Button buttonAddTask;


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
        editUrl=editUrl+taskModel.getTaks_id();
        projectIdList=AdminToTasks.projectIdList;
        employeeIdList=AdminToTasks.employeeIdList;

        loadData();
        loadSpinnerData();

    }
    private void loadSpinnerData() {
        projectIdList.clear();
        employeeIdList.clear();
        final StringRequest stringRequestProject=new StringRequest(Request.Method.GET,fetchProject, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        projectIdList.add(jsonObjectReceive.getString("project_id"));


                    }

//


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

        final StringRequest stringRequestEmployee=new StringRequest(Request.Method.GET,fetchEmployee, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        employeeIdList.add(jsonObjectReceive.getString("employee_id"));


                    }

//


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

        requestQueue.add(stringRequestProject);
        requestQueue.add(stringRequestEmployee);


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
                    .setMessage("Do you want to delete this Project?")
                    .setTitle("WARNING!!!")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //delete function called

                            deleteTask();
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
            editTask();
        }
        return true;
    }

    private void deleteTask() {

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

    private void editTask() {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_task, (LinearLayout) findViewById(R.id.bottomSheetTask));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();

        spinnerProject=bottomSheetDialog.findViewById(R.id.projectIdSpinner);
        spinnerEmployee=bottomSheetDialog.findViewById(R.id.employeeIdSpinner);

        editTextDescription=bottomSheetDialog.findViewById(R.id.editTextTaskDescription);
        editTextDuo=bottomSheetDialog.findViewById(R.id.editTextTaskDue);
        buttonAddTask=bottomSheetDialog.findViewById(R.id.addTaskButton);

        editTextDescription.setText(taskModel.getDescription());
        editTextDuo.setText(taskModel.getDue());

        arrayAdapterSpinnerProject=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,projectIdList);
        arrayAdapterSpinnerProject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProject.setAdapter(arrayAdapterSpinnerProject);

        arrayAdapterSpinnerEmployee=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,employeeIdList);
        arrayAdapterSpinnerEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployee.setAdapter(arrayAdapterSpinnerEmployee);


        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                projectId= parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                projectId= parent.getItemAtPosition(0).toString();




            }
        });

        spinnerEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                eId= parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId= parent.getItemAtPosition(0).toString();



            }
        });
        buttonAddTask.setText("Edit Task");

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                description=editTextDescription.getText().toString();
                duo=editTextDuo.getText().toString();

                if (!description.isEmpty()&&!duo.isEmpty())
                {

                    StringRequest stringRequestAdd=new StringRequest(Request.Method.PUT, editUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.isEmpty())
                                    {
                                        Log.d("error", "onResponse: null ");

                                    }

                                    Log.d("error", "onResponse: "+response.toString());

                                    JSONObject jsonObjectData = null;
                                    try {
                                        jsonObjectData = new JSONObject(response);
                                        String jsonString=jsonObjectData.getString("data");
                                        JSONObject jsonObject=new JSONObject(jsonString);
                                        textViewPId.setText("Project Id: "+jsonObject.getString("project_id"));
                                        textViewEId.setText("Employee Id: "+jsonObject.getString("employee_id"));
                                        textViewDescription.setText(" Description: "+jsonObject.getString("description"));
                                        if (jsonObject.getString("due").equals("1"))
                                        {
                                            textViewDuo.setText("True");
                                        }
                                        else
                                        {
                                            textViewDuo.setText("False");

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }





                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(),"Try Again"+error.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("Error", "onErrorResponse: "+error.toString());

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String,String> params=new HashMap<String, String>();
                            params.put("project_id",projectId);
                            params.put("employee_id",eId);
                            params.put("due",duo);
                            params.put("description",description);


                            return params;
                        }
                    };

                    requestQueue.add(stringRequestAdd);




                    bottomSheetDialog.dismiss();


                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Fillup All Field",Toast.LENGTH_SHORT).show();
                }











            }
        });
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