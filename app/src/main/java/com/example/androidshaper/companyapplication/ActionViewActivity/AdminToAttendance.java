package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.Adapter.AttendanceAdapter;
import com.example.androidshaper.companyapplication.Adapter.EmployeeAdapterView;
import com.example.androidshaper.companyapplication.DataModel.AttendancesModel;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DetailsActivity.AttendanceDetailsActivity;
import com.example.androidshaper.companyapplication.DetailsActivity.TeamDetails;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminToAttendance extends AppCompatActivity implements AttendanceAdapter.OnRecyclerItemClickInterface {


    public static List<AttendancesModel> attendancesModelList;
    AttendanceAdapter attendanceAdapter;
    ArrayAdapter arrayAdapterSpinnerEmployee;
    ImageView imageViewAttendanceAdd,imageViewDatePicker,imageViewTimePicker;
    EditText editTextAttendanceDate,editTextAttendanceTime;
    Button buttonCheckInAdd;
   Spinner spinnerEmployeeId;
   RecyclerView recyclerViewAttendance;
   SearchView searchViewAttendance;
    String dateText,timeText,eId;
    String edateText,etimeText,checkIn;
    String fetchUrl="https://benot.xyz/api/api/attendances";
    String fetchEmployee="https://benot.xyz/api/api/employees";
    public static String eName,pName;
    public static ArrayList<String> employeeIdList;
    public static ArrayList<String> employeeIdListName;
    public static List<EmployeeModel> employeeIdName;
    TextView textView;
    LinearLayout linearLayout;

   RequestQueue requestQueue;
   AttendanceAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;

   String eIdPanel,check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_attendance);
        imageViewAttendanceAdd=findViewById(R.id.attendanceAddButton);
        searchViewAttendance=findViewById(R.id.searchViewAttendance);
        searchViewAttendance.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                attendanceAdapter.getFilter().filter(newText);

                return false;
            }
        });
        recyclerViewAttendance=findViewById(R.id.recyclerViewAttendance);
        attendancesModelList=new ArrayList<>();
        employeeIdList=new ArrayList<>();
        employeeIdListName=new ArrayList<>();
        employeeIdName=new ArrayList<>();

        check=getIntent().getExtras().getString("check");



        recyclerViewAttendance.setHasFixedSize(true);
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        requestQueue= Volley.newRequestQueue(this);
        onRecyclerItemClickInterface=AdminToAttendance.this;

        imageViewAttendanceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAttendance();
            }
        });



        loadDataPanel();








    }

    private void loadDataPanel() {
        loadSpinner();


        if (check.equals("user"))
        {
            SharedPreferences sharedPreferences=getSharedPreferences("userLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();

            eIdPanel=sharedPreferences.getString("eid","").toString().trim();



            loadDataEmployee(eIdPanel);
        }
        else {
            loadData();



        }


    }

    private void loadDataEmployee(final String eIdF) {
     searchViewAttendance.setVisibility(View.INVISIBLE);

        attendancesModelList.clear();

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);
                        if (  jsonObjectReceive.getString("employee_id").equals(eIdF))
                        {
                            AttendancesModel attendancesModel=new AttendancesModel(
                                    jsonObjectReceive.getString("attendance_id"),
                                    jsonObjectReceive.getString("employee_id"),
                                    jsonObjectReceive.getString("check_in"),
                                    jsonObjectReceive.getString("check_out"));

                            attendancesModelList.add(attendancesModel);
                        }



                    }
                    attendanceAdapter=new AttendanceAdapter(attendancesModelList,onRecyclerItemClickInterface,employeeIdName);
                    attendanceAdapter.notifyDataSetChanged();
                    recyclerViewAttendance.setAdapter(attendanceAdapter);


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

    private void loadSpinner() {

        employeeIdListName.clear();
        employeeIdList.clear();
        employeeIdName.clear();


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
                        employeeIdListName.add(jsonObjectReceive.getString("name")+"("+jsonObjectReceive.getString("employee_id")+")");



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

    private void createAttendance() {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottomsheet_attendance, (LinearLayout) findViewById(R.id.bottomSheetTask));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();


        spinnerEmployeeId=bottomSheetDialog.findViewById(R.id.attendanceEmployeeIdSpinner);
        editTextAttendanceTime=bottomSheetDialog.findViewById(R.id.editTextAttendanceTime);
        editTextAttendanceDate=bottomSheetDialog.findViewById(R.id.editTextAttendanceDate);
        imageViewDatePicker=bottomSheetDialog.findViewById(R.id.attendanceDateImageView);
        imageViewTimePicker=bottomSheetDialog.findViewById(R.id.attendanceTimeImageView);
        textView=bottomSheetDialog.findViewById(R.id.textAttendance);
        linearLayout=bottomSheetDialog.findViewById(R.id.layoutAttendance);

        buttonCheckInAdd=bottomSheetDialog.findViewById(R.id.addCheckInButton);

        if (check.equals("user"))
        {
            linearLayout.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            eId=eIdPanel;
        }


        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminToAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, date);
                         dateText= DateFormat.format("dd-MM-yyyy", calendar1).toString();

                        editTextAttendanceDate.setText(dateText);
                    }
                }, YEAR, MONTH, DATE);

                datePickerDialog.show();



            }
        });
        imageViewTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int HOUR = calendar.get(Calendar.HOUR);
                int MINUTE = calendar.get(Calendar.MINUTE);
                boolean is24HourFormat = DateFormat.is24HourFormat(AdminToAttendance.this);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AdminToAttendance.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        Log.i("Time", "onTimeSet: " + hour + minute);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.HOUR, hour);
                        calendar1.set(Calendar.MINUTE, minute);
                        timeText= DateFormat.format("HH-mm-ss", calendar1).toString();
                        editTextAttendanceTime.setText(timeText);
                    }
                }, HOUR, MINUTE, is24HourFormat);

                timePickerDialog.show();

            }
        });
        arrayAdapterSpinnerEmployee=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,employeeIdListName);
        arrayAdapterSpinnerEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployeeId.setAdapter(arrayAdapterSpinnerEmployee);
        spinnerEmployeeId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                eId= employeeIdList.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                eId= employeeIdList.get(0).toString();



            }
        });
        buttonCheckInAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!(dateText ==null) && !(timeText ==null))
                {
                    checkIn=dateText+":"+timeText;

//                    Toast.makeText(getApplicationContext(),eId+dateText+timeText,Toast.LENGTH_SHORT).show();


                    StringRequest stringRequestAddEmployee=new StringRequest(Request.Method.POST, fetchUrl,
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
                            error.printStackTrace();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String,String> params=new HashMap<String, String>();
                            params.put("employee_id",eId);
                            params.put("check_in",dateText+":"+timeText);
                            params.put("check_out","not submitted...");


                            return params;
                        }
                    };

                    requestQueue.add(stringRequestAddEmployee);


                   searchViewAttendance.clearFocus();


                    bottomSheetDialog.dismiss();
                    loadSpinner();
                    loadData();
                    dateText=null;
                    timeText=null;



                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Fillup All Field",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void loadData() {
        attendancesModelList.clear();

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);

                        AttendancesModel attendancesModel=new AttendancesModel(
                                jsonObjectReceive.getString("attendance_id"),
                                jsonObjectReceive.getString("employee_id"),
                                jsonObjectReceive.getString("check_in"),
                                jsonObjectReceive.getString("check_out"));

                        attendancesModelList.add(attendancesModel);

                    }
                   attendanceAdapter=new AttendanceAdapter(attendancesModelList,onRecyclerItemClickInterface,employeeIdName);
                    attendanceAdapter.notifyDataSetChanged();
                    recyclerViewAttendance.setAdapter(attendanceAdapter);


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

        Intent intent=new Intent(AdminToAttendance.this, AttendanceDetailsActivity.class);
        intent.putExtra("position",position);

        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadDataPanel();
    }
}