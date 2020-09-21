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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.Adapter.EmployeeAdapterView;
import com.example.androidshaper.companyapplication.Adapter.ProjectAdapter;
import com.example.androidshaper.companyapplication.Adapter.TaskAdapter;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.DetailsActivity.ProjectDetailActivity;
import com.example.androidshaper.companyapplication.DetailsActivity.TaskDetailsActivity;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminToTasks extends AppCompatActivity implements TaskAdapter.OnRecyclerItemClickInterface {

    RecyclerView recyclerViewTask;
    ImageView imageViewAddTask;
    SearchView searchViewTask;

    Button buttonAddTask;


    ArrayAdapter arrayAdapterSpinnerProject,arrayAdapterSpinnerEmployee;

    public static List<TaskModel> taskModelsList;

    TaskAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;

    RequestQueue requestQueue;
    TaskAdapter taskAdapter;

    EditText editTextDescription,editTextDuo;
    Spinner spinnerEmployee,spinnerProject;


    String fetchUrl="https://benot.xyz/api/api/tasks";
    String fetchProject="https://benot.xyz/api/api/projects";
    String fetchEmployee="https://benot.xyz/api/api/employees";

    ArrayList<String> projectIdList;
    ArrayList<String> employeeIdList;

    String description,duo,projectId,eId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_tasks);
        recyclerViewTask=findViewById(R.id.recyclerViewTask);
        imageViewAddTask=findViewById(R.id.taskAddButton);
        searchViewTask=findViewById(R.id.searchViewTask);

        recyclerViewTask.setHasFixedSize(true);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        requestQueue= Volley.newRequestQueue(this);
        onRecyclerItemClickInterface=AdminToTasks.this;

        taskModelsList=new ArrayList<>();
        projectIdList=new ArrayList<>();
       employeeIdList=new ArrayList<>();

        loadSpinnerData();

        loadTask();

        imageViewAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });




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

    private void createNewTask() {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_task, (LinearLayout) findViewById(R.id.bottomSheetTask));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();

        spinnerProject=bottomSheetDialog.findViewById(R.id.projectIdSpinner);
        spinnerEmployee=bottomSheetDialog.findViewById(R.id.employeeIdSpinner);

        editTextDescription=bottomSheetDialog.findViewById(R.id.editTextTaskDescription);
        editTextDuo=bottomSheetDialog.findViewById(R.id.editTextTaskDue);
        buttonAddTask=bottomSheetDialog.findViewById(R.id.addTaskButton);

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

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                description=editTextDescription.getText().toString();
                duo=editTextDuo.getText().toString();

                if (!description.isEmpty()&&!duo.isEmpty())
                {

                    StringRequest stringRequestAdd=new StringRequest(Request.Method.POST, fetchUrl,
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

                                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
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

                    searchViewTask.clearFocus();


                    bottomSheetDialog.dismiss();
                    loadTask();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Fillup All Field",Toast.LENGTH_SHORT).show();
                }











            }
        });




    }

    private void loadTask() {
        taskModelsList.clear();

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        TaskModel taskModel=new TaskModel(jsonObjectReceive.getString("taks_id"),jsonObjectReceive.getString("project_id")
                        ,jsonObjectReceive.getString("employee_id"),jsonObjectReceive.getString("description"),jsonObjectReceive.getString("due"));


                        taskModelsList.add(taskModel);

                    }

                    taskAdapter=new TaskAdapter(onRecyclerItemClickInterface,taskModelsList);
                    taskAdapter.notifyDataSetChanged();
                    recyclerViewTask.setAdapter(taskAdapter);



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

    @Override
    public void OnItemClick(int position) {

        Intent intent=new Intent(AdminToTasks.this, TaskDetailsActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}