package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

    AttendanceAdapter attendanceAdapter;
    public static List<AttendancesModel> attendancesModelList;
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

   RequestQueue requestQueue;
   AttendanceAdapter.OnRecyclerItemClickInterface onRecyclerItemClickInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_attendance);
        imageViewAttendanceAdd=findViewById(R.id.attendanceAddButton);
        searchViewAttendance=findViewById(R.id.searchViewAttendance);
        recyclerViewAttendance=findViewById(R.id.recyclerViewAttendance);
        attendancesModelList=new ArrayList<>();
        employeeIdList=new ArrayList<>();
        employeeIdListName=new ArrayList<>();
        employeeIdName=new ArrayList<>();

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

        loadSpinner();

        loadData();


    }

    private void loadSpinner() {

        employeeIdListName.clear();


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
        buttonCheckInAdd=bottomSheetDialog.findViewById(R.id.addCheckInButton);
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
                   attendanceAdapter=new AttendanceAdapter(attendancesModelList,onRecyclerItemClickInterface);
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

    }
}