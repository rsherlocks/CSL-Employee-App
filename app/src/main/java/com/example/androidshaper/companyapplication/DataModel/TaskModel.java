package com.example.androidshaper.companyapplication.DataModel;

public class TaskModel {
    String taks_id;
    String project_id;
    String employee_id;
    String description;
    String due;

    public TaskModel() {
    }

    public TaskModel(String taks_id, String project_id, String employee_id, String description, String due) {
        this.taks_id = taks_id;
        this.project_id = project_id;
        this.employee_id = employee_id;
        this.description = description;
        this.due = due;
    }

    public String getTaks_id() {
        return taks_id;
    }

    public void setTaks_id(String taks_id) {
        this.taks_id = taks_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }
}
