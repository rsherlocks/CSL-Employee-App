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
import com.example.androidshaper.companyapplication.Adapter.ProjectAdapter;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.DetailsActivity.ProjectDetailActivity;
import com.example.androidshaper.companyapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeToProject extends AppCompatActivity implements ProjectAdapter.OnRecyclerItemClickInterface {


    SearchView searchViewProject;

    RecyclerView recyclerViewEmployeeProject;

    RequestQueue requestQueue;
    ProjectAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;

    String fetchUrl="https://benot.xyz/api/api/projects";
    public static List<ProjectModel> modelList;

    ProjectAdapter projectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_to_project);

        searchViewProject=findViewById(R.id.searchViewEmployeeProject);
        recyclerViewEmployeeProject=findViewById(R.id.recyclerViewEmployeeProject);

        requestQueue= Volley.newRequestQueue(this);

        recyclerViewEmployeeProject.setHasFixedSize(true);
        onRecyclerItemClickInterface=EmployeeToProject.this;
        modelList=new ArrayList<>();

        recyclerViewEmployeeProject.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchViewProject.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                projectAdapter.getFilter().filter(newText);
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

                        ProjectModel projectModel=new ProjectModel(jsonObjectReceive.getString("project_id"),jsonObjectReceive.getString("team_id")
                                ,jsonObjectReceive.getString("name"),jsonObjectReceive.getString("start_date"),jsonObjectReceive.getString("end_date")
                                ,jsonObjectReceive.getString("description"),jsonObjectReceive.getString("amount"),jsonObjectReceive.getString("birth_date")
                                ,jsonObjectReceive.getString("type"),jsonObjectReceive.getString("client"),jsonObjectReceive.getString("priority")
                                ,jsonObjectReceive.getString("leader"));


                        modelList.add(projectModel);

                    }
                    projectAdapter=new ProjectAdapter(onRecyclerItemClickInterface,modelList);
                    projectAdapter.notifyDataSetChanged();
                    recyclerViewEmployeeProject.setAdapter(projectAdapter);
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

        requestQueue.add(stringRequest);


    }

    @Override
    public void OnItemClick(int position) {

        Intent intent=new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("check",1);
        intent.putExtra("position",position);
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}