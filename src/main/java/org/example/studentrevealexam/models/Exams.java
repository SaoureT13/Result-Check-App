package org.example.studentrevealexam.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exams implements Serializable {
    List<String> examList = new ArrayList<>();

    public <E> Exams() {
    }

    public void setExamList(String name) {
        examList.add(name);
    }

    public List<String> getExamList() {
        return examList;
    }
}
