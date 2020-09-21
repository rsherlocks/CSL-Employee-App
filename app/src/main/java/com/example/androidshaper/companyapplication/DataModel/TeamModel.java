package com.example.androidshaper.companyapplication.DataModel;

public class TeamModel {

    String team_id;
    String e_id_1;
    String e_id_2;
    String e_id_3;
    String e_id_4;
    String e_id_5;

    public TeamModel() {
    }

    public TeamModel(String team_id, String e_id_1, String e_id_2, String e_id_3, String e_id_4, String e_id_5) {
        this.team_id = team_id;
        this.e_id_1 = e_id_1;
        this.e_id_2 = e_id_2;
        this.e_id_3 = e_id_3;
        this.e_id_4 = e_id_4;
        this.e_id_5 = e_id_5;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getE_id_1() {
        return e_id_1;
    }

    public void setE_id_1(String e_id_1) {
        this.e_id_1 = e_id_1;
    }

    public String getE_id_2() {
        return e_id_2;
    }

    public void setE_id_2(String e_id_2) {
        this.e_id_2 = e_id_2;
    }

    public String getE_id_3() {
        return e_id_3;
    }

    public void setE_id_3(String e_id_3) {
        this.e_id_3 = e_id_3;
    }

    public String getE_id_4() {
        return e_id_4;
    }

    public void setE_id_4(String e_id_4) {
        this.e_id_4 = e_id_4;
    }

    public String getE_id_5() {
        return e_id_5;
    }

    public void setE_id_5(String e_id_5) {
        this.e_id_5 = e_id_5;
    }
}
