package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.Adapter.TaskAdapter;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.DetailsActivity.TaskDetailsActivity;
import com.example.androidshaper.companyapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeToTask extends AppCompatActivity implements TaskAdapter.OnRecyclerItemClickInterface {

    RecyclerView recyclerViewEmployeeTask;
    SearchView searchViewEmployeeTask;

    public static List<TaskModel> taskModelsList;
    RequestQueue requestQueue;
    TaskAdapter taskAdapter;

    String fetchUrl="https://benot.xyz/api/api/tasks";
    String fetchProject="https://benot.xyz/api/api/projects";
    String fetchEmployee="https://benot.xyz/api/api/employees";

    public static ArrayList<String> projectIdList;
    public static ArrayList<String> employeeIdList;

    TaskAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_to_task);
        recyclerViewEmployeeTask=findViewById(R.id.recyclerViewEmployeeTask);
        searchViewEmployeeTask=findViewById(R.id.searchViewEmployeeTask);

        onRecyclerItemClickInterface=EmployeeToTask.this;
        recyclerViewEmployeeTask.setHasFixedSize(true);

        taskModelsList=new ArrayList<>();
        projectIdList=new ArrayList<>();
        employeeIdList=new ArrayList<>();
        recyclerViewEmployeeTask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        requestQueue= Volley.newRequestQueue(this);


        loadData();
        loadSpinnerData();

        searchViewEmployeeTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void loadData() {

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
                    recyclerViewEmployeeTask.setAdapter(taskAdapter);



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

        Intent intent=new Intent(EmployeeToTask.this, TaskDetailsActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("check",1);
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}