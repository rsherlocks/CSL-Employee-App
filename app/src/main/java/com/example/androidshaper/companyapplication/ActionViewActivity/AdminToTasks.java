package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.DetailsActivity.ProjectDetailActivity;
import com.example.androidshaper.companyapplication.DetailsActivity.TaskDetailsActivity;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminToTasks extends AppCompatActivity implements TaskAdapter.OnRecyclerItemClickInterface {

    RecyclerView recyclerViewTask;
    ImageView imageViewAddTask,imageViewDatePicker;
    SearchView searchViewTask;

    Button buttonAddTask;

    long millisecond;


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
    public static String eName,pName;

    public static ArrayList<String> projectIdList;
    public static ArrayList<String> employeeIdList;
    public static ArrayList<String> projectIdName;
    public static List<EmployeeModel> employeeIdName;

    String description,dateText,projectId,eId;





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
        projectIdName=new ArrayList<>();
        employeeIdName=new ArrayList<>();


        loadSpinnerData();


        searchViewTask.clearFocus();

        imageViewAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });
        searchViewTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAdapter.getFilter().filter(newText);
                return false;
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
                       pName=jsonObjectReceive.getString("name")+"("+jsonObjectReceive.getString("project_id")+")";

                        projectIdList.add(pName);

                        pName="";



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
                        EmployeeModel employeeModel=new EmployeeModel(jsonObjectReceive.getString("employee_id"),jsonObjectReceive.getString("name"));
                        employeeIdName.add(employeeModel);


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

        loadTask();


    }

    private void createNewTask() {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_task, (LinearLayout) findViewById(R.id.bottomSheetTask));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();

        final long[] milliseconds = new long[1];

        spinnerProject=bottomSheetDialog.findViewById(R.id.projectIdSpinner);
        spinnerEmployee=bottomSheetDialog.findViewById(R.id.employeeIdSpinner);

        editTextDescription=bottomSheetDialog.findViewById(R.id.editTextTaskDescription);
        editTextDuo=bottomSheetDialog.findViewById(R.id.editTextTaskDue);
        buttonAddTask=bottomSheetDialog.findViewById(R.id.addTaskButton);

        imageViewDatePicker=bottomSheetDialog.findViewById(R.id.dateTaskImageView);

        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminToTasks.this, new DatePickerDialog.OnDateSetListener() {
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
                dateText=editTextDuo.getText().toString();

                SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    Date d = (Date) f.parse(dateText);
                    milliseconds[0] = d.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (!description.isEmpty()&&!dateText.isEmpty())
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
                                        loadSpinnerData();
                                        loadTask();
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

                    searchViewTask.clearFocus();

                    loadSpinnerData();

                    bottomSheetDialog.dismiss();


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
        employeeIdName.clear();

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        TaskModel taskModel=new TaskModel(jsonObjectReceive.getString("taks_id"),
                                jsonObjectReceive.getString("project_id")
                        ,jsonObjectReceive.getString("employee_id"),
                                jsonObjectReceive.getString("description"),
                                jsonObjectReceive.getString("due"));
                        taskModelsList.add(taskModel);









                    }
                   // Log.d("EmployeeName", "onResponse: "+employeeIdName.get(0).getName().toString());



                        taskAdapter=new TaskAdapter(onRecyclerItemClickInterface,taskModelsList,employeeIdName);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        loadSpinnerData();

    }
}