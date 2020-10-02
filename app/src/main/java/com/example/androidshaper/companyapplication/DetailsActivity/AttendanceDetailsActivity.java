package com.example.androidshaper.companyapplication.DetailsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToAttendance;
import com.example.androidshaper.companyapplication.Adapter.AttendanceAdapter;
import com.example.androidshaper.companyapplication.DataModel.AttendancesModel;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AttendanceDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textViewAttendanceId,textViewEmployeeId,textViewCheckIn,textViewCheckOut;



    ImageView imageViewDatePicker,imageViewTimePicker;
    EditText editTextAttendanceDate,editTextAttendanceTime;
    Button buttonCheckInAdd;

    String dateText,timeText,eId;
    String checkIn;
    String fetchUrl="https://benot.xyz/api/api/attendances";

    int position;
    AttendancesModel attendancesModel;

   RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);
        toolbar=findViewById(R.id.toolbarAttendance);
        textViewAttendanceId=findViewById(R.id.textViewAttendanceId);
        textViewEmployeeId=findViewById(R.id.textViewAttendanceEmployeeIdName);
        textViewCheckIn=findViewById(R.id.textViewAttendanceCheckIn);
        textViewCheckOut=findViewById(R.id.textViewAttendanceCheckOut);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        position=getIntent().getExtras().getInt("position");

        attendancesModel= AdminToAttendance.attendancesModelList.get(position);

        requestQueue= Volley.newRequestQueue(this);

        fetchUrl=fetchUrl+"/"+attendancesModel.getAttendance_id();
        loadData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolar,menu);
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
                    .setMessage("Do you want to delete this Employee?")
                    .setTitle("WARNING!!!")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //delete function called

                            deleteAttendance();
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
            editAttendance();
        }
        return true;

    }

    private void editAttendance() {

        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_check_out_attendance, (LinearLayout) findViewById(R.id.bottomSheetTask));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                boolean is24HourFormat = DateFormat.is24HourFormat(AttendanceDetailsActivity.this);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AttendanceDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        buttonCheckInAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!(dateText ==null) && !(timeText ==null))
                {
                    checkIn=dateText+":"+timeText;


                    StringRequest stringRequestAddEmployee=new StringRequest(Request.Method.PUT, fetchUrl,
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
                                        String jsonString=jsonObject.getString("data");
                                        JSONObject jsonObjectData=new JSONObject(jsonString);
                                        textViewCheckOut.setText("Check Out: "+jsonObjectData.getString("check_out"));



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
                            params.put("check_out",dateText+":"+timeText);


                            return params;
                        }
                    };

                    requestQueue.add(stringRequestAddEmployee);





                    bottomSheetDialog.dismiss();

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

    private void deleteAttendance() {

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

    private void loadData() {
        textViewAttendanceId.setText("Attendance Id: " + attendancesModel.getAttendance_id());
        textViewCheckIn.setText("Check In: " + attendancesModel.getCheck_in());
        textViewCheckOut.setText("Check Out: " + attendancesModel.getCheck_out());
        for(EmployeeModel employeeModel:AdminToAttendance.employeeIdName)
        {
            if (employeeModel.getEmployee_id()==attendancesModel.getEmployee_id())
            {
                textViewEmployeeId.setText("Name: "+employeeModel.getName()+"("+attendancesModel.getEmployee_id()+")");;
            }
        }
    }

}