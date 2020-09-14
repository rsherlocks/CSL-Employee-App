package com.example.androidshaper.companyapplication.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapterView extends RecyclerView.Adapter<EmployeeAdapterView.EmployeeViewHolder> {

    OnRecyclerItemClickInterface itemClickInterface;
    List<EmployeeModel> modelList;

    public EmployeeAdapterView(OnRecyclerItemClickInterface itemClickInterface, List<EmployeeModel> modelList) {
        this.itemClickInterface = itemClickInterface;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row,parent,false);

        return new EmployeeViewHolder(view,itemClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        EmployeeModel employeeModel=modelList.get(position);

        String joinDate=employeeModel.getJoining_date().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;

        try {
            convertedDate = dateFormat.parse(joinDate);
            String month = new SimpleDateFormat("MMM").format(convertedDate);
            String day = new SimpleDateFormat("dd").format(convertedDate);
            holder.textViewDate.setText(day);
            holder.textViewMonth.setText(month);
            Log.d("date", "onBindViewHolder: "+day+month);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.textViewName.setText(employeeModel.getName());
        holder.textViewManager.setText(employeeModel.getPhone());
        holder.viewCardColor.setBackgroundColor(getRandomColor());


    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDate,textViewMonth,textViewName,textViewManager;
        View viewCardColor;
        OnRecyclerItemClickInterface onRecyclerItemClickInterface;

        public EmployeeViewHolder(@NonNull View itemView,OnRecyclerItemClickInterface interFace) {
            super(itemView);
            textViewDate=itemView.findViewById(R.id.textViewJoinDate);
            textViewMonth=itemView.findViewById(R.id.textViewJoinMonth);
            textViewName=itemView.findViewById(R.id.textViewEmployeeName);
            textViewManager=itemView.findViewById(R.id.textViewEmployeeManager);
            viewCardColor=itemView.findViewById(R.id.viewCardColor);
            itemView.setOnClickListener(this);
            this.onRecyclerItemClickInterface=interFace;
        }

        @Override
        public void onClick(View v) {

            onRecyclerItemClickInterface.OnItemClick(getAdapterPosition());

        }
    }

    public interface OnRecyclerItemClickInterface {
        void OnItemClick(int position);


    }
}