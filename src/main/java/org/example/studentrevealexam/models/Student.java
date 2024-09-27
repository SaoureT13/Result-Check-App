package org.example.studentrevealexam.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 3789909326487155148L;
    private int lg_mat_etud;
    private String str_nom_etud;
    private String str_prenoms_etud;
    private String dt_date_naissance_etud;
    private String str_ecole_etud;
    private HashMap<String, Integer> notes = new HashMap<String, Integer>();

    public int getLg_mat_etud() {
        return lg_mat_etud;
    }

    public String getStr_nom_etud() {
        return str_nom_etud;
    }

    public String getDt_date_naissance_etud() {
        return dt_date_naissance_etud;
    }

    public String getStr_ecole_etud() {
        return str_ecole_etud;
    }

    public String getStr_prenoms_etud() {
        return str_prenoms_etud;
    }

    public HashMap<String, Integer> getNotes() {
        return notes;
    }

    public void setLg_mat_etud(int lg_mat_etud) {
        this.lg_mat_etud = lg_mat_etud;
    }

    public void setStr_nom_etud(String str_nom_etud) {
        this.str_nom_etud = str_nom_etud;
    }

    public void setStr_prenoms_etud(String str_prenoms_etud) {
        this.str_prenoms_etud = str_prenoms_etud;
    }

    public void setDt_date_naissance_etud(String dt_date_naissance_etud) {
        this.dt_date_naissance_etud = dt_date_naissance_etud;
    }

    public void setStr_ecole_etud(String str_ecole_etud) {
        this.str_ecole_etud = str_ecole_etud;
    }

    public void setNotes(String notes) {
        String[] examsNotes = notes.split(",");

        for (String examNote : examsNotes) {
            String[] parts = examNote.split(":");
            String exam = parts[0];
            Integer note = Integer.parseInt(parts[1]);
            this.notes.put(exam, note);
        }

    }
}

