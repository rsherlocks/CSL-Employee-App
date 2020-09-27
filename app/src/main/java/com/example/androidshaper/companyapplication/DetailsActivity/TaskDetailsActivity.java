package com.example.androidshaper.companyapplication.DetailsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToProject;
import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToTask;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView textViewPId,textViewEId,textViewDescription,textViewDuo;
    EditText editTextDescription,editTextDuo;
    Spinner spinnerEmployee,spinnerProject;
    ArrayAdapter arrayAdapterSpinnerProject,arrayAdapterSpinnerEmployee;

    int position,check;
    TaskModel taskModel;
    RequestQueue requestQueue;
    String editUrl="https://benot.xyz/api/api/tasks/";

    String fetchProject="https://benot.xyz/api/api/projects";
    String fetchEmployee="https://benot.xyz/api/api/employees";

    ArrayList<String> projectIdList=new ArrayList<>();
    ArrayList<String> employeeIdList=new ArrayList<>();

    String description,dateText,projectId,eId;
    Button buttonAddTask;
    ImageView imageViewDate;


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
        check=getIntent().getExtras().getInt("check");

        if (check==1)
        {
            taskModel= EmployeeToTask.taskModelsList.get(position);
            projectIdList=EmployeeToTask.projectIdList;
            employeeIdList=EmployeeToTask.employeeIdList;
        }
        else {
            taskModel= AdminToTasks.taskModelsList.get(position);
            projectIdList=AdminToTasks.projectIdList;
            employeeIdList=AdminToTasks.employeeIdList;

        }

        requestQueue= Volley.newRequestQueue(this);

        editUrl=editUrl+taskModel.getTaks_id();


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

                        projectIdList.add(jsonObjectReceive.getString("name")+"("+jsonObjectReceive.getString("project_id")+")");


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
        if (check==1)
        {
            MenuItem menuItemEdit=menu.findItem(R.id.editId);
            MenuItem menuItemDelete=menu.findItem(R.id.deleteId);
            menuItemEdit.setVisible(false);
            menuItemDelete.setVisible(false);
        }
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
        imageViewDate=bottomSheetDialog.findViewById(R.id.dateTaskImageView);
        imageViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, date);
                        dateText = DateFormat.format("dd-MM-yyyy", calendar1).toString();



                        editTextDuo.setText(dateText);

                    }
                }, YEAR, MONTH, DATE);

                datePickerDialog.show();

            }
        });

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
                dateText=editTextDuo.getText().toString();

                if (!description.isEmpty()&&!dateText.isEmpty())
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
                                        textViewDuo.setText("Date: "+jsonObject.getString("due"));


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
                            params.put("due",dateText);
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

        for(EmployeeModel employeeModel:AdminToTasks.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==taskModel.getEmployee_id())
            {
                textViewEId.setText("Name: "+employeeModel.getName()+"("+taskModel.getEmployee_id()+")");
            }
        }

        textViewPId.setText("Project Id: "+taskModel.getProject_id());
        //textViewEId.setText("Employee Id: "+taskModel.getEmployee_id());
        textViewDescription.setText(" Description: "+taskModel.getDescription());
        textViewDuo.setText("Date: "+taskModel.getDue());
    }
}