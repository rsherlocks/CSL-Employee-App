package com.example.androidshaper.companyapplication.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.androidshaper.companyapplication.DataModel.EmployeeModel;
import com.example.androidshaper.companyapplication.DataModel.ProjectModel;
import com.example.androidshaper.companyapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> implements Filterable {

    OnRecyclerItemClickInterface itemClickInterface;
    List<ProjectModel> projectModelsList;
    List<ProjectModel> projectModelsListSearch;



    public ProjectAdapter(OnRecyclerItemClickInterface itemClickInterface, List<ProjectModel> projectModelsList) {
        this.itemClickInterface = itemClickInterface;
        this.projectModelsList = projectModelsList;
        projectModelsListSearch=new ArrayList<>();
        projectModelsListSearch.addAll(projectModelsList);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.project_row,parent,false);

        return new ProjectViewHolder(view,itemClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {

        ProjectModel projectModel=projectModelsList.get(position);

        String endDate=projectModel.getEnd_date().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;

        try {
            convertedDate = dateFormat.parse(endDate);
            String month = new SimpleDateFormat("MMM").format(convertedDate);
            String day = new SimpleDateFormat("dd").format(convertedDate);
            holder.textViewDate.setText(day);
            holder.textViewMonth.setText(month);
            Log.d("date", "onBindViewHolder: "+day+month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewName.setText(projectModel.getName());
        holder.textViewDescription.setText(projectModel.getDescription());

        holder.viewCardColor.setBackgroundColor(getRandomColor());


    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return projectModelsList.size();
    }

    @Override
    public Filter getFilter() {
        return filterProject;
    }
    private Filter filterProject=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ProjectModel> modelListFilter=new ArrayList<>();
            if (charSequence==null || charSequence.length()==0)
            {
                modelListFilter.addAll(projectModelsListSearch);
            }
            else{
                String searchFilter=charSequence.toString().toLowerCase().trim();
                for (ProjectModel projectModel:projectModelsListSearch)
                {
                    if (projectModel.getName().toLowerCase().contains(searchFilter))
                    {
                        modelListFilter.add(projectModel);

                    }
                }

            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=modelListFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            projectModelsList.clear();
            projectModelsList.addAll((Collection<? extends ProjectModel>) results.values);
            notifyDataSetChanged();

        }
    };

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewDate,textViewMonth,textViewName,textViewDescription;
        View viewCardColor;
        OnRecyclerItemClickInterface onRecyclerItemClickInterface;
        public ProjectViewHolder(@NonNull View itemView,OnRecyclerItemClickInterface interFace) {
            super(itemView);
            textViewDate=itemView.findViewById(R.id.textViewEndDate);
            textViewMonth=itemView.findViewById(R.id.textViewEndMonth);
            textViewName=itemView.findViewById(R.id.textViewProjectName);
            textViewDescription=itemView.findViewById(R.id.textViewProjectDescription);
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
