package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminToAttendance extends AppCompatActivity implements AttendanceAdapter.OnRecyclerItemClickInterface {

    AttendanceAdapter attendanceAdapter;
    public static List<AttendancesModel> attendancesModelList;
   ImageView imageViewAttendanceAdd;
   RecyclerView recyclerViewAttendance;
   SearchView searchViewAttendance;
   String fetchUrl="https://benot.xyz/api/api/attendances";

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
        recyclerViewAttendance.setHasFixedSize(true);
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        requestQueue= Volley.newRequestQueue(this);
        onRecyclerItemClickInterface=AdminToAttendance.this;

        loadData();


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
                                jsonObjectReceive.getString("check_in"));

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