package com.example.androidshaper.companyapplication.ActionViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidshaper.companyapplication.Adapter.EmployeeAdapterView;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DetailsActivity.EmployeeDetailsActivity;
import com.example.androidshaper.companyapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class AdminToEmployee extends AppCompatActivity implements EmployeeAdapterView.OnRecyclerItemClickInterface{

    SearchView searchView;
    RecyclerView recyclerViewEmployee;
    ImageView imageView;
    EmployeeAdapterView employeeAdapterView;
    EmployeeAdapterView.OnRecyclerItemClickInterface onRecyclerItemClickInterface;
    public static List<EmployeeModel> modelList;
    EditText editTextName,editTextEmail,editTextPhone,editTextGender,editTextAddress,editTextJoiningDate,editTextBirthDate,editTextManger;
    Button buttonAddEmployee;


    String name,email,phone,gender,address,joiningDate,birthDate,manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_to_employee);
        searchView=findViewById(R.id.searchViewEmployee);
        imageView=findViewById(R.id.employeeAddButton);
        recyclerViewEmployee=findViewById(R.id.recyclerViewEmployee);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.employeeAddButton)
                {
                    bottomSheetPlugin();
                }
            }
        });

        onRecyclerItemClickInterface=AdminToEmployee.this;
        recyclerViewEmployee.setHasFixedSize(true);
        recyclerViewEmployee.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelList=new ArrayList<>();
        modelList.add(new EmployeeModel("Md.Jubayer","csl.jubayer@gmail.com","01827095630","male","Mohakhali","22-08-2019","31-12-1995","hasan"));
        modelList.add(new EmployeeModel("Md.Jubayer Hasan","csl.jubayer@gmail.com","01827095630","male","Mohakhali","12-03-2019","31-12-1995","hasan"));



        loadData();


    }

    private void loadData() {

        employeeAdapterView=new EmployeeAdapterView(onRecyclerItemClickInterface,modelList);
        recyclerViewEmployee.setAdapter(employeeAdapterView);
    }

    private void bottomSheetPlugin() {

        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheet);
        View bottomSheet= LayoutInflater.from(this).inflate(R.layout.bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheetEmployee));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
        editTextName=bottomSheetDialog.findViewById(R.id.editTextEmployeeName);
        editTextEmail=bottomSheetDialog.findViewById(R.id.editTextEmployeeEmail);
        editTextPhone=bottomSheetDialog.findViewById(R.id.editTextEmployeePhone);
        editTextGender=bottomSheetDialog.findViewById(R.id.editTextEmployeeGender);
        editTextAddress=bottomSheetDialog.findViewById(R.id.editTextEmployeeAddress);
        editTextJoiningDate=bottomSheetDialog.findViewById(R.id.editTextEmployeeJoiningDate);
        editTextBirthDate=bottomSheetDialog.findViewById(R.id.editTextEmployeeBirthDate);
        editTextManger=bottomSheetDialog.findViewById(R.id.editTextEmployeeManager);
        buttonAddEmployee=bottomSheetDialog.findViewById(R.id.addEmployeeButton);
        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=editTextName.getText().toString().trim();
                email=editTextEmail.getText().toString().trim();
                phone=editTextPhone.getText().toString().trim();
                gender=editTextGender.getText().toString().trim();
                address=editTextAddress.getText().toString().trim();
                joiningDate=editTextJoiningDate.getText().toString().trim();
                birthDate=editTextBirthDate.getText().toString().trim();
                manager=editTextManger.getText().toString().trim();

                if (!name.isEmpty()&&!email.isEmpty()&&!phone.isEmpty())
                {
                   //modelList.add(new EmployeeModel("Md.Jubayer Hasan","csl.jubayer@gmail.com","01827095630","male","Mohakhali","22-08-2019","31-12-1995","hasan"));
                    modelList.add(new EmployeeModel(name,email,phone,gender,address,joiningDate,birthDate,manager));

                    employeeAdapterView=new EmployeeAdapterView(onRecyclerItemClickInterface,modelList);
                    recyclerViewEmployee.setAdapter(employeeAdapterView);


                    searchView.clearFocus();
                    bottomSheetDialog.dismiss();
                }

                else{
                    Toast.makeText(getApplicationContext(),"Fill up all field",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public void OnItemClick(int position) {

        Intent intent=new Intent(AdminToEmployee.this, EmployeeDetailsActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}