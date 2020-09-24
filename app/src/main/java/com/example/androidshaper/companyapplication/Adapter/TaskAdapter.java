package com.example.androidshaper.companyapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

    OnRecyclerItemClickInterface itemClickInterface;
    List<TaskModel> taskModelList;
    List<TaskModel> taskModelListSearch;
    List<EmployeeModel> employeeName;



    public TaskAdapter(OnRecyclerItemClickInterface itemClickInterface, List<TaskModel> taskModelList, List<EmployeeModel> employeeName) {
        this.itemClickInterface = itemClickInterface;
        this.taskModelList = taskModelList;
        this.employeeName = employeeName;
        taskModelListSearch=new ArrayList<>();
        taskModelListSearch.addAll(taskModelList);
    }

    public TaskAdapter(OnRecyclerItemClickInterface itemClickInterface, List<TaskModel> taskModelList) {
        this.itemClickInterface = itemClickInterface;
        this.taskModelList = taskModelList;
        taskModelListSearch=new ArrayList<>();
        taskModelListSearch.addAll(taskModelList);


    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row,parent,false);
        return new TaskViewHolder(view,itemClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel taskModel=taskModelList.get(position);
        for(EmployeeModel employeeModel:employeeName)
        {
            if (employeeModel.getEmployee_id()==taskModel.getEmployee_id())
            {
                holder.textViewTaskId.setText("Employee Name: "+employeeModel.getName());
            }
        }

//        eNamePNameSet(taskModel.getEmployee_id(),taskModel.getProject_id());
//        String eName=employeeName.get(position);


        holder.textViewDescription.setText(taskModel.getDescription());
        holder.textViewProjectId.setText("P_id: "+taskModel.getProject_id());
        holder.textViewDue.setText("Date: "+taskModel.getDue());
        holder.viewCardColor.setBackgroundColor(getRandomColor());


    }

//    private void eNamePNameSet(String eId,String pId) {
//
////        final StringRequest stringRequestProject=new StringRequest(Request.Method.GET,fetchProject, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////
////                try {
////                    JSONObject jsonObject=new JSONObject(response);
////                    JSONArray jsonArray=jsonObject.getJSONArray("data");
////                    for(int i=0;i<jsonArray.length();i++)
////                    {
////                        JSONObject jsonObjectReceive=jsonArray.getJSONObject(i);
////
////                        projectIdList.add(jsonObjectReceive.getString("project_id"));
////
////
////                    }
////
//////
////
////
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////
////                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
////
////            }
////        });
//
//
//
//    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filterTask;
    }
    private Filter filterTask=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TaskModel> modelListFilter=new ArrayList<>();
            if (charSequence==null || charSequence.length()==0)
            {
                modelListFilter.addAll(taskModelListSearch);
            }
            else{
                String searchFilter=charSequence.toString().toLowerCase().trim();
                for (TaskModel taskModel:taskModelListSearch)
                {
                    if (taskModel.getProject_id().toLowerCase().contains(searchFilter))
                    {
                        modelListFilter.add(taskModel);

                    }
                }

            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=modelListFilter;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taskModelList.clear();
            taskModelList.addAll((Collection<? extends TaskModel>) results.values);
            notifyDataSetChanged();

        }
    };

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewProjectId,textViewDue,textViewTaskId,textViewDescription;
        View viewCardColor;
        OnRecyclerItemClickInterface onRecyclerItemClickInterface;

        public TaskViewHolder(@NonNull View itemView,OnRecyclerItemClickInterface interFace) {
            super(itemView);
            textViewProjectId=itemView.findViewById(R.id.textViewTaskProjectId);
            textViewDue=itemView.findViewById(R.id.textViewTaskDue);
            textViewTaskId=itemView.findViewById(R.id.textViewTaskId);
            textViewDescription=itemView.findViewById(R.id.textViewTaskDescription);
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
