package com.example.androidshaper.companyapplication.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToAttendance;
import com.example.androidshaper.companyapplication.DataModel.AttendancesModel;
import com.example.androidshaper.companyapplication.R;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    List<AttendancesModel> attendancesModelList;
    OnRecyclerItemClickInterface itemClickInterface;

    public AttendanceAdapter(List<AttendancesModel> attendancesModelList, OnRecyclerItemClickInterface itemClickInterface) {
        this.attendancesModelList = attendancesModelList;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendance_layout,parent,false);
        return new AttendanceViewHolder(view,itemClickInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {

        AttendancesModel attendancesModel=attendancesModelList.get(position);

        holder.textViewEmployeeId.setText("Employee Id: "+attendancesModel.getEmployee_id());
        holder.textViewCheckIn.setText("Check In: "+attendancesModel.getCheck_in());
        holder.textViewCheckOut.setText("Check In: "+attendancesModel.getCheck_out());

        holder.viewCardColor.setBackgroundColor(getRandomColor());





    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return attendancesModelList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewEmployeeId,textViewName,textViewCheckIn,textViewCheckOut;
        View viewCardColor;
       OnRecyclerItemClickInterface onRecyclerItemClickInterface;

        public AttendanceViewHolder(@NonNull View itemView,OnRecyclerItemClickInterface interFace) {
            super(itemView);
            textViewEmployeeId=itemView.findViewById(R.id.textViewAttendanceEmployeeId);
            textViewCheckIn=itemView.findViewById(R.id.textViewAttendanceCheckIn);
            textViewCheckOut=itemView.findViewById(R.id.textViewAttendanceCheckOut);
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
