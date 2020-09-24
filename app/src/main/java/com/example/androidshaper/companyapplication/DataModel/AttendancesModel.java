package com.example.androidshaper.companyapplication.DataModel;

public class AttendancesModel {

    String attendance_id;
    String employee_id;
    String check_in;
    String check_out;

    public AttendancesModel() {
    }

    public AttendancesModel(String attendance_id, String employee_id, String check_in, String check_out) {
        this.attendance_id = attendance_id;
        this.employee_id = employee_id;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }
}
