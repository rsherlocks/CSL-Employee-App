package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import com.example.androidshaper.companyapplication.Adapter.TaskAdapter;
import com.example.androidshaper.companyapplication.Adapter.TeamAdapter;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.DataModel.TeamModel;
import com.example.androidshaper.companyapplication.DetailsActivity.TaskDetailsActivity;
import com.example.androidshaper.companyapplication.DetailsActivity.TeamDetails;
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

public class AdminToTeam extends AppCompatActivity implements TeamAdapter.OnRecyclerItemClickInterface {
    RecyclerView  recyclerViewTeam;
    ImageView imageViewAddButton;
    Button addTeamButton;
    SearchView searchViewTeam;
    TeamAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;
    public static List<TeamModel> teamModelList;
    TeamAdapter teamAdapter;
    RequestQueue requestQueue;
    String fetchUrl="https://benot.xyz/api/api/teams";

    Spinner spinnerEmployee1,spinnerEmployee2,spinnerEmployee3,spinnerEmployee4,spinnerEmployee5;

    String fetchEmployee="https://benot.xyz/api/api/employees";
    ArrayAdapter arrayAdapterSpinnerEmployee;


    public static ArrayList<String> employeeIdList;
    public static ArrayList<String> employeeId;
    public static List<EmployeeModel> employeeIdName;

    String eId1,eId2,eId3,eId4,eId5;
    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_team);
        recyclerViewTeam=findViewById(R.id.recyclerViewAdminTeam);
        teamModelList=new ArrayList<>();
        employeeIdList=new ArrayList<>();
        employeeIdName=new ArrayList<>();
        employeeId=new ArrayList<>();
        onRecyclerItemClickInterface=AdminToTeam.this;
        requestQueue= Volley.newRequestQueue(this);

        recyclerViewTeam.setHasFixedSize(true);
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchViewTeam=findViewById(R.id.searchViewAdminTeam);
        imageViewAddButton=findViewById(R.id.adminTeamAddButton);
        imageViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeam();
            }
        });
        searchViewTeam.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                teamAdapter.getFilter().filter(newText);
                return false;
            }
        });
        check=getIntent().getExtras().getInt("check");
        if (check==1)
        {
            imageViewAddButton.setVisibility(View.INVISIBLE);
        }

        loadData();
        loadSpinner();

    }

    private void loadSpinner() {

        employeeIdList.clear();

        final StringRequest stringRequestEmployee=new StringRequest(Request.Method.GET,fetchEmployee, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        employeeIdList.add(jsonObjectReceive.getString("name")+"("+jsonObjectReceive.getString("employee_id")+")");
                        EmployeeModel employeeModel=new EmployeeModel(jsonObjectReceive.getString("employee_id"),jsonObjectReceive.getString("name"));
                        employeeIdName.add(employeeModel);
                        employeeId.add(jsonObjectReceive.getString("employee_id"));


                    }


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

        requestQueue.add(stringRequestEmployee);
    }

    private void loadData() {
        teamModelList.clear();

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        TeamModel teamModel=new TeamModel(jsonObjectReceive.getString("team_id"),jsonObjectReceive.getString("e_id_1")
                                ,jsonObjectReceive.getString("e_id_2"),jsonObjectReceive.getString("e_id_3"),jsonObjectReceive.getString("e_id_4"),jsonObjectReceive.getString("e_id_5"));


                        teamModelList.add(teamModel);

                    }
                    Log.d("array", "onResponse: "+employeeIdName.toString());

                    teamAdapter=new TeamAdapter(teamModelList,employeeIdName,onRecyclerItemClickInterface);
                    teamAdapter.notifyDataSetChanged();
                    recyclerViewTeam.setAdapter(teamAdapter);



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

    private void addTeam() {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_team, (LinearLayout) findViewById(R.id.bottomSheetTask));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();



        spinnerEmployee1=bottomSheetDialog.findViewById(R.id.employeeIdSpinner1);
        spinnerEmployee2=bottomSheetDialog.findViewById(R.id.employeeIdSpinner2);
        spinnerEmployee3=bottomSheetDialog.findViewById(R.id.employeeIdSpinner3);
        spinnerEmployee4=bottomSheetDialog.findViewById(R.id.employeeIdSpinner4);
        spinnerEmployee5=bottomSheetDialog.findViewById(R.id.employeeIdSpinner5);
        addTeamButton=bottomSheetDialog.findViewById(R.id.addTeamButton);







        arrayAdapterSpinnerEmployee=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,employeeIdList);
        arrayAdapterSpinnerEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployee1.setAdapter(arrayAdapterSpinnerEmployee);
        spinnerEmployee2.setAdapter(arrayAdapterSpinnerEmployee);
        spinnerEmployee3.setAdapter(arrayAdapterSpinnerEmployee);
        spinnerEmployee4.setAdapter(arrayAdapterSpinnerEmployee);
        spinnerEmployee5.setAdapter(arrayAdapterSpinnerEmployee);



        spinnerEmployee1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                eId1= employeeId.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId1= employeeId.get(0).toString();




            }
        });

        spinnerEmployee2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                eId2= employeeId.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId2= employeeId.get(0).toString();



            }
        });
        spinnerEmployee3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                eId3= employeeId.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId3= employeeId.get(0).toString();



            }
        });
        spinnerEmployee4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                eId4= employeeId.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId4= employeeId.get(0).toString();



            }
        });
        spinnerEmployee5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                eId5= employeeId.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId5= employeeId.get(0).toString();



            }
        });

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




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
                            params.put("e_id_1",eId1);
                            params.put("e_id_2",eId2);
                            params.put("e_id_3",eId3);
                            params.put("e_id_4",eId4);
                            params.put("e_id_5",eId5);


                            return params;
                        }
                    };

                    requestQueue.add(stringRequestAdd);

                    searchViewTeam.clearFocus();


                    bottomSheetDialog.dismiss();
                    loadData();













            }
        });
    }

    @Override
    public void OnItemClick(int position) {

        Intent intent=new Intent(AdminToTeam.this, TeamDetails.class);
        intent.putExtra("position",position);
        intent.putExtra("check",check);

        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}