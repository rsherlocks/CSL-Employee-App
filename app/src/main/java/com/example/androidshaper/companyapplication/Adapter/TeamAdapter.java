package com.example.androidshaper.companyapplication.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.androidshaper.companyapplication.DataModel.TaskModel;
import com.example.androidshaper.companyapplication.DataModel.TeamModel;
import com.example.androidshaper.companyapplication.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> implements Filterable {

    List<TeamModel> teamModelList;
    List<TeamModel> teamModelListSearch;
    OnRecyclerItemClickInterface itemClickInterface;

    public TeamAdapter(List<TeamModel> teamModelList, OnRecyclerItemClickInterface itemClickInterface) {
        this.teamModelList = teamModelList;
        this.itemClickInterface = itemClickInterface;
        teamModelListSearch=new ArrayList<>();
        teamModelListSearch.addAll(teamModelList);
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row_layout,parent,false);
        return new TeamViewHolder(view,itemClickInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        TeamModel teamModel=teamModelList.get(position);
        holder.textViewTeamId.setText("Team_Id"+teamModel.getTeam_id());
        holder.textViewEmployeeId1.setText("e_id_1: " + teamModel.getE_id_1());
        holder.textViewEmployeeId2.setText("e_id_2: "+teamModel.getE_id_2());
        holder.textViewEmployeeId3.setText("e_id_3: "+teamModel.getE_id_3());
        holder.textViewEmployeeId4.setText("e_id_4: "+teamModel.getE_id_4());
        holder.textViewEmployeeId5.setText("e_id_5: "+teamModel.getE_id_5());
        holder.viewCardColor.setBackgroundColor(getRandomColor());

    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return teamModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filterTeam;
    }
    public  Filter filterTeam=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<TeamModel> modelListFilter=new ArrayList<>();
            if (charSequence==null || charSequence.length()==0)
            {
                modelListFilter.addAll(teamModelListSearch);
            }
            else{
                String searchFilter=charSequence.toString().toLowerCase().trim();
                for (TeamModel teamModel:teamModelListSearch)
                {
                    if (teamModel.getTeam_id().toLowerCase().contains(searchFilter))
                    {
                        modelListFilter.add(teamModel);

                    }
                }

            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=modelListFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            teamModelList.clear();
            teamModelList.addAll((Collection<? extends TeamModel>) results.values);
            notifyDataSetChanged();


        }
    };

    public class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTeamId,textViewEmployeeId1,textViewEmployeeId2,textViewEmployeeId3,textViewEmployeeId4
        ,textViewEmployeeId5;
        View viewCardColor;

        OnRecyclerItemClickInterface onRecyclerItemClickInterface;
        public TeamViewHolder(@NonNull View itemView,OnRecyclerItemClickInterface interFace) {
            super(itemView);
            textViewTeamId=itemView.findViewById(R.id.textViewTeamTeamId);
            textViewEmployeeId1=itemView.findViewById(R.id.textViewTeamEmployeeId1);
            textViewEmployeeId2=itemView.findViewById(R.id.textViewTeamEmployeeId2);
            textViewEmployeeId3=itemView.findViewById(R.id.textViewTeamEmployeeId3);
            textViewEmployeeId4=itemView.findViewById(R.id.textViewTeamEmployeeId4);
            textViewEmployeeId5=itemView.findViewById(R.id.textViewTeamEmployeeId5);
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
