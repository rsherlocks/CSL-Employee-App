package com.example.androidshaper.companyapplication.DataModel;

public class ProjectModel {
    String team_id;
    String name;
    String start_date;
    String end_date;
    String description;
    String amount;
    String type;
    String client;
    String priority;
    String leader;

    public ProjectModel() {
    }

    public ProjectModel(String team_id, String name, String start_date, String end_date, String description, String amount, String type, String client, String priority, String leader) {
        this.team_id = team_id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.client = client;
        this.priority = priority;
        this.leader = leader;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
