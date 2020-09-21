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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.Adapter.EmployeeAdapterView;
import com.example.androidshaper.companyapplication.Adapter.ProjectAdapter;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.DetailsActivity.EmployeeDetailsActivity;
import com.example.androidshaper.companyapplication.DetailsActivity.ProjectDetailActivity;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminToProjectActivity extends AppCompatActivity implements ProjectAdapter.OnRecyclerItemClickInterface {


    RecyclerView recyclerViewProject;
    SearchView searchViewProject;
    ImageView imageViewAddProject;

    ProjectAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;


    EditText editTextTeamId,editTextProjectName,editTextStartDate,editTextEndDate,editTextDescription,editTextAmount,editTextType
            ,editTextClient,editTextPriority,editTextLeader;

    Button buttonProjectAdd;


   ProjectAdapter projectAdapter;


    RequestQueue requestQueue;

    public static List<ProjectModel> modelList;

    String fetchUrl="https://benot.xyz/api/api/projects";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_project);

        recyclerViewProject=findViewById(R.id.recyclerViewProject);
        searchViewProject=findViewById(R.id.searchViewProject);
        imageViewAddProject=findViewById(R.id.projectAddButton);

        onRecyclerItemClickInterface=AdminToProjectActivity.this;

        requestQueue= Volley.newRequestQueue(this);

        recyclerViewProject.setHasFixedSize(true);
        recyclerViewProject.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        modelList=new ArrayList<>();

        imageViewAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewAddProject();
            }
        });

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

        loadProjectData();
    }

    private void imageViewAddProject() {

        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_shet_project, (LinearLayout) findViewById(R.id.bottomSheetProject));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
        editTextTeamId=bottomSheetDialog.findViewById(R.id.editTextProjectTeamId);
        editTextProjectName=bottomSheetDialog.findViewById(R.id.editTextProjectName);
        editTextStartDate=bottomSheetDialog.findViewById(R.id.editTextProjectStartDate);
        editTextEndDate=bottomSheetDialog.findViewById(R.id.editTextProjectEndDate);
        editTextDescription=bottomSheetDialog.findViewById(R.id.editTextProjectDescription);
        editTextAmount=bottomSheetDialog.findViewById(R.id.editTextProjectAmount);
        editTextType=bottomSheetDialog.findViewById(R.id.editTextProjectType);
        editTextClient=bottomSheetDialog.findViewById(R.id.editTextProjectClient);
        editTextPriority=bottomSheetDialog.findViewById(R.id.editTextProjectPriority);
        editTextLeader=bottomSheetDialog.findViewById(R.id.editTextProjectLeader);
        buttonProjectAdd=bottomSheetDialog.findViewById(R.id.addProjectButton);

        buttonProjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String teamId,name,startDate,endDate,description,amount,type,client,priority,leader;

                teamId=editTextTeamId.getText().toString();
                name=editTextProjectName.getText().toString();
                startDate=editTextStartDate.getText().toString();
                endDate=editTextEndDate.getText().toString();
                description=editTextDescription.getText().toString();
                amount=editTextAmount.getText().toString();
                type=editTextType.getText().toString();
                client=editTextClient.getText().toString();
                priority=editTextPriority.getText().toString();
                leader=editTextLeader.getText().toString();

                if (!teamId.isEmpty()&&!name.isEmpty()&&!startDate.isEmpty()&&!endDate.isEmpty()&&!description.isEmpty())
                {
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, fetchUrl,
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
                            params.put("team_id",teamId);
                            params.put("name",name);
                            params.put("start_date",startDate);
                            params.put("end_date",endDate);
                            params.put("description",description);
                            params.put("amount",amount);
                            params.put("type",type);
                            params.put("client",client);
                            params.put("priority",priority);
                            params.put("leader",leader);

                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);





                    searchViewProject.clearFocus();
                    bottomSheetDialog.dismiss();
                    loadProjectData();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill all field",Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    private void loadProjectData() {

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
                    recyclerViewProject.setAdapter(projectAdapter);
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

        Intent intent=new Intent(AdminToProjectActivity.this, ProjectDetailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadProjectData();
    }
}