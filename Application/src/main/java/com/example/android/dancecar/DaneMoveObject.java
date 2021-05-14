package com.example.android.dancecar;

import java.util.ArrayList;

public class DaneMoveObject {
    private String name;
    private int id;
    private ArrayList<IndividualMove> individualMoves = new ArrayList();

    public DaneMoveObject(String danceName, int id) {
        this.name = danceName;
        this.id = id;
    }

    public DaneMoveObject(ArrayList<IndividualMove> individualMoves) {
        this.individualMoves = individualMoves;
    }


    public String getDanceName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setDanceName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
