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
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToTeam;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.TeamModel;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamDetails extends AppCompatActivity {

    TextView textViewTeamId,textViewEId1,textViewEId2,textViewEId3,textViewEId4,textViewEId5;

    TeamModel teamModel;
    int position,check=0;

    Toolbar toolbar;

    ArrayList<String> employeeId;

    RequestQueue requestQueue;
    String fetchUrl="https://benot.xyz/api/api/teams/";

    Spinner spinnerEmployee1,spinnerEmployee2,spinnerEmployee3,spinnerEmployee4,spinnerEmployee5;

    String fetchEmployee="https://benot.xyz/api/api/employees";
    ArrayAdapter arrayAdapterSpinnerEmployee;



    String eId1,eId2,eId3,eId4,eId5;

    Button addTeamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        textViewTeamId=findViewById(R.id.textViewEmployeeDetailsTeamId);
        textViewEId1=findViewById(R.id.textViewDetailsEmployeeId1);
        textViewEId2=findViewById(R.id.textViewDetailsEmployeeId2);
        textViewEId3=findViewById(R.id.textViewDetailsEmployeeId3);
        textViewEId4=findViewById(R.id.textViewDetailsEmployeeId4);
        textViewEId5=findViewById(R.id.textViewDetailsEmployeeId5);
        toolbar=findViewById(R.id.toolbarTeam);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        position=getIntent().getExtras().getInt("position");
        check=getIntent().getExtras().getInt("check");
        teamModel= AdminToTeam.teamModelList.get(position);
        fetchUrl=fetchUrl+teamModel.getTeam_id().toString().trim();
        employeeId=AdminToTeam.employeeId;
        requestQueue= Volley.newRequestQueue(this);


        loadData();

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

                            deleteTeam();
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
            editTeam();
        }
        return true;
    }

    private void deleteTeam() {
        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, fetchUrl,
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

    private void editTeam() {
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

        addTeamButton.setText("Edit Team");







        arrayAdapterSpinnerEmployee=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AdminToTeam.employeeIdList);
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




                StringRequest stringRequestAdd=new StringRequest(Request.Method.PUT, fetchUrl,
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

                                    textViewTeamId.setText("Project Id: "+jsonObject.getString("team_id"));

                                    for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
                                    {
                                        if (employeeModel.getEmployee_id()==jsonObject.getString("e_id_1"))
                                        {

                                            textViewEId1.setText("e_id_1: " +employeeModel.getName()+"("+ jsonObject.getString("e_id_1")+")");
                                        }
                                    }

                                    for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
                                    {
                                        if (employeeModel.getEmployee_id()==jsonObject.getString("e_id_2"))
                                        {

                                            textViewEId2.setText("e_id_2: " +employeeModel.getName()+"("+ jsonObject.getString("e_id_2")+")");
                                        }
                                    }
                                    for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
                                    {
                                        if (employeeModel.getEmployee_id()==jsonObject.getString("e_id_3"))
                                        {

                                            textViewEId3.setText("e_id_3: " +employeeModel.getName()+"("+jsonObject.getString("e_id_3")+")");
                                        }
                                    }
                                    for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
                                    {
                                        if (employeeModel.getEmployee_id()==jsonObject.getString("e_id_4"))
                                        {

                                            textViewEId4.setText("e_id_4: " +employeeModel.getName()+"("+jsonObject.getString("e_id_4")+")");
                                        }
                                    }
                                    for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
                                    {
                                        if (employeeModel.getEmployee_id()==jsonObject.getString("e_id_5"))
                                        {

                                            textViewEId5.setText("e_id_5: " +employeeModel.getName()+"("+jsonObject.getString("e_id_5")+")");
                                        }
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
                        params.put("e_id_1",eId1);
                        params.put("e_id_2",eId2);
                        params.put("e_id_3",eId3);
                        params.put("e_id_4",eId4);
                        params.put("e_id_5",eId5);


                        return params;
                    }
                };

                requestQueue.add(stringRequestAdd);

                Toast.makeText(getApplicationContext(),"Update Data",Toast.LENGTH_SHORT).show();




                bottomSheetDialog.dismiss();
                finish();













            }
        });
    }

    private void loadData() {
//        for(EmployeeModel employeeModel:AdminToTeam.employeeIdName)
//        {
//            if (employeeModel.getEmployee_id()==teamModel.getE_id_1())
//            {
//
//                textViewEId1.setText("e_id_1: " +employeeModel.getName()+"("+ teamModel.getE_id_1()+")");
//            }
//            else if (employeeModel.getEmployee_id()==teamModel.getE_id_2())
//            {
//
//                textViewEId2.setText("e_id_2: " +employeeModel.getName()+"("+ teamModel.getE_id_2()+")");
//            }
//            else if (employeeModel.getEmployee_id()==teamModel.getE_id_3())
//            {
//
//                textViewEId3.setText("e_id_3: " +employeeModel.getName()+"("+ teamModel.getE_id_3()+")");
//            }
//            else if (employeeModel.getEmployee_id()==teamModel.getE_id_4())
//            {
//
//                textViewEId4.setText("e_id_4: " +employeeModel.getName()+"("+ teamModel.getE_id_4()+")");
//            }
//            else if (employeeModel.getEmployee_id()==teamModel.getE_id_5())
//            {
//
//                textViewEId5.setText("e_id_5: " +employeeModel.getName()+"("+ teamModel.getE_id_5()+")");
//            }
////            else{
////         textViewEId1.setText("Employee Id1: "+teamModel.getE_id_1());
////        textViewEId2.setText("Employee Id2: "+teamModel.getE_id_2());
////        textViewEId3.setText("Employee Id3: "+teamModel.getE_id_3());
////        textViewEId4.setText("Employee Id1: "+teamModel.getE_id_4());
////        textViewEId5.setText("Employee Id1: "+teamModel.getE_id_5());
////
////            }
//        }
        for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==teamModel.getE_id_1())
            {

                textViewEId1.setText("e_id_1: " +employeeModel.getName()+"("+ teamModel.getE_id_1()+")");
            }
        }
        for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==teamModel.getE_id_2())
            {

                textViewEId2.setText("e_id_2: " +employeeModel.getName()+"("+ teamModel.getE_id_2()+")");
            }
        }
        for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==teamModel.getE_id_3())
            {

                textViewEId3.setText("e_id_3: " +employeeModel.getName()+"("+ teamModel.getE_id_3()+")");
            }
        }
        for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==teamModel.getE_id_4())
            {

                textViewEId4.setText("e_id_4: " +employeeModel.getName()+"("+ teamModel.getE_id_4()+")");
            }
        }
        for (EmployeeModel employeeModel : AdminToTeam.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==teamModel.getE_id_5())
            {

                textViewEId5.setText("e_id_5: " +employeeModel.getName()+"("+ teamModel.getE_id_5()+")");
            }
        }
        textViewTeamId.setText("Team Id: "+teamModel.getTeam_id());
//        textViewEId1.setText("Employee Id1: "+teamModel.getE_id_1());
//        textViewEId2.setText("Employee Id2: "+teamModel.getE_id_2());
//        textViewEId3.setText("Employee Id3: "+teamModel.getE_id_3());
//        textViewEId4.setText("Employee Id1: "+teamModel.getE_id_4());
//        textViewEId5.setText("Employee Id1: "+teamModel.getE_id_5());

    }
}