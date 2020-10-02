package com.example.androidshaper.companyapplication.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.androidshaper.companyapplication.ActionViewActivity.AdminToAttendance;
import com.example.androidshaper.companyapplication.DataModel.AttendancesModel;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> implements Filterable {

    List<AttendancesModel> attendancesModelList;
    OnRecyclerItemClickInterface itemClickInterface;
    List<EmployeeModel> employeeModelList;
    List<AttendancesModel> attendancesModelListSearch;


//    public AttendanceAdapter(List<AttendancesModel> attendancesModelList, OnRecyclerItemClickInterface itemClickInterface) {
//        this.attendancesModelList = attendancesModelList;
//        this.itemClickInterface = itemClickInterface;
//    }

    public AttendanceAdapter(List<AttendancesModel> attendancesModelList, OnRecyclerItemClickInterface itemClickInterface, List<EmployeeModel> employeeModelList) {
        this.attendancesModelList = attendancesModelList;
        this.itemClickInterface = itemClickInterface;
        this.employeeModelList = employeeModelList;
       attendancesModelListSearch=new ArrayList<>();
        attendancesModelListSearch.addAll(attendancesModelList);
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

        for(EmployeeModel employeeModel:employeeModelList)
        {
            if (employeeModel.getEmployee_id()==attendancesModel.getEmployee_id())
            {
                holder.textViewAttendanceEmployeeId.setText("Name: "+employeeModel.getName()+"("+attendancesModel.getEmployee_id()+")");
            }
        }


        holder.textViewAttendanceId.setText("Attendance Id: "+attendancesModel.getAttendance_id());

        holder.textViewCheckIn.setText("Check In: "+attendancesModel.getCheck_in());
        holder.textViewCheckOut.setText("Check Out: "+attendancesModel.getCheck_out());

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

    @Override
    public Filter getFilter() {
        return filterAttendance;
    }
    private Filter filterAttendance=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
             List<AttendancesModel> modelListFilter=new ArrayList<>();
            if (charSequence==null || charSequence.length()==0)
            {
                modelListFilter.addAll(attendancesModelListSearch);
            }
            else{
                String searchFilter=charSequence.toString().toLowerCase().trim();
                for (AttendancesModel attendancesModel:attendancesModelListSearch)
                {
                    if (attendancesModel.getEmployee_id().toLowerCase().contains(searchFilter))
                    {
                        modelListFilter.add(attendancesModel);

                    }
                }

            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=modelListFilter;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            attendancesModelList.clear();
            attendancesModelList.addAll((Collection<? extends AttendancesModel>) results.values);
            notifyDataSetChanged();

        }
    };

    public class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewAttendanceId,textViewAttendanceEmployeeId,textViewCheckIn,textViewCheckOut;
        View viewCardColor;
       OnRecyclerItemClickInterface onRecyclerItemClickInterface;

        public AttendanceViewHolder(@NonNull View itemView,OnRecyclerItemClickInterface interFace) {
            super(itemView);
            textViewAttendanceEmployeeId=itemView.findViewById(R.id.textViewAttendanceEmployeeId);
            textViewAttendanceId=itemView.findViewById(R.id.textViewAttendanceId);
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
