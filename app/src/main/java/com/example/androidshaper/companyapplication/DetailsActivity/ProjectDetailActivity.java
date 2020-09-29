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
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToProjectActivity;
import com.example.androidshaper.companyapplication.ActionViewActivity.EmployeeToProject;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectDetailActivity extends AppCompatActivity {

    TextView textViewTeamId,textViewName,textViewStartDate,textViewEndDate,textViewDescription,textViewAmount,textViewType
            ,textViewClient,textViewPriority,textViewLeader;

    EditText editTextTeamId,editTextProjectName,editTextStartDate,editTextEndDate,editTextDescription,editTextAmount,editTextType
            ,editTextClient,editTextPriority,editTextLeader;





    ProjectModel projectModel;
    int position;

    Toolbar toolbarProject;

    RequestQueue requestQueue;
    int check;

    String editUrl="https://benot.xyz/api/api/projects/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        textViewTeamId=findViewById(R.id.textViewProjectTeamId);
        textViewName=findViewById(R.id.textViewProjectName);
        textViewStartDate=findViewById(R.id.textViewProjectStartDate);
        textViewEndDate=findViewById(R.id.textViewProjectEndDate);
        textViewDescription=findViewById(R.id.textViewProjectDescription);
        textViewAmount=findViewById(R.id.textViewProjectAmount);
        textViewClient=findViewById(R.id.textViewProjectClient);
        textViewPriority=findViewById(R.id.textViewProjectPriority);
        textViewLeader=findViewById(R.id.textViewProjectLeader);
        textViewType=findViewById(R.id.textViewProjectType);
        toolbarProject=findViewById(R.id.toolbarProject);
        setSupportActionBar(toolbarProject);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        position=getIntent().getExtras().getInt("position");
        check=getIntent().getExtras().getInt("check");
        requestQueue= Volley.newRequestQueue(this);
        if (check==1)
        {
            projectModel= EmployeeToProject.modelList.get(position);
        }
        else {
            projectModel= AdminToProjectActivity.modelList.get(position);

        }


        editUrl=editUrl+projectModel.getProject_id();
        setProjectData(position);
    }

    private void setProjectData(int position) {


        textViewTeamId.setText("Team Id: "+projectModel.getTeam_id());
        textViewName.setText("Project Name: "+projectModel.getName());
        textViewStartDate.setText("Start Date: "+projectModel.getStart_date());
        textViewEndDate.setText("End Date: "+projectModel.getEnd_date());
        textViewDescription.setText("Description: "+projectModel.getDescription());
        textViewAmount.setText("Amount: "+projectModel.getAmount());
        textViewClient.setText("Client: "+projectModel.getClient());
        textViewPriority.setText("Priority: "+projectModel.getPriority());
        textViewType.setText("Type: "+projectModel.getType());
        textViewLeader.setText("Leader: "+projectModel.getLeader());
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

                            deleteProject();
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
            editProject();
        }
        return true;
    }

    private void deleteProject() {

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


    private void editProject() {


        Button buttonProjectAdd;


        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_shet_project, (LinearLayout) findViewById(R.id.bottomSheetEmployee));
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

        buttonProjectAdd.setText("Edit Project");

        loadEditPage();


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
                    StringRequest stringRequest=new StringRequest(Request.Method.PUT, editUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.isEmpty())
                                    {
                                        Log.d("error", "onResponse: null ");

                                    }

                                    Log.d("error", "onResponse: "+response.toString());

                                    JSONObject jsonObjectData= null;
                                    try {
                                        jsonObjectData = new JSONObject(response);
                                        String jsonString=jsonObjectData.getString("data");
                                        JSONObject jsonObject=new JSONObject(jsonString);

                                        textViewTeamId.setText("Team Id: "+jsonObject.getString("team_id"));
                                        textViewName.setText("Project Name: "+jsonObject.getString("name"));
                                        textViewStartDate.setText("Start Date: "+jsonObject.getString("start_date"));
                                        textViewEndDate.setText("End Date: "+jsonObject.getString("end_date"));
                                        textViewDescription.setText("Description: "+jsonObject.getString("description"));
                                        textViewAmount.setText("Amount: "+jsonObject.getString("amount"));
                                        textViewClient.setText("Client"+jsonObject.getString("client"));
                                        textViewPriority.setText("Priority: "+jsonObject.getString("priority"));
                                        textViewType.setText("Type: "+jsonObject.getString("type"));
                                        textViewLeader.setText("Leader: "+jsonObject.getString("leader"));


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






                    bottomSheetDialog.dismiss();


                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill all field",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void loadEditPage() {

        editTextTeamId.setText(projectModel.getTeam_id());
        editTextProjectName.setText(projectModel.getName());
        editTextStartDate.setText(projectModel.getStart_date());
        editTextEndDate.setText(projectModel.getEnd_date());
        editTextDescription.setText(projectModel.getDescription());
        editTextAmount.setText(projectModel.getAmount());
        editTextClient.setText(projectModel.getClient());
        editTextPriority.setText(projectModel.getPriority());
        editTextType.setText(projectModel.getType());
        editTextLeader.setText(projectModel.getLeader());


    }
}