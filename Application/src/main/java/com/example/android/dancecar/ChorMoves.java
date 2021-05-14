package com.example.android.dancecar;

import java.util.ArrayList;

public class ChorMoves {
    ArrayList<DaneMoveObject> selectedDances;
    int chorMoveID;
    String chorName;

    public ChorMoves(ArrayList<DaneMoveObject> selectedDances, int chorMoveID, String chorName) {
        this.selectedDances = selectedDances;
        this.chorMoveID = chorMoveID;
        this.chorName = chorName;
    }

    public ArrayList<DaneMoveObject> getSelectedDances() {
        return selectedDances;
    }

    public void setSelectedDances(ArrayList<DaneMoveObject> selectedDances) {
        this.selectedDances = selectedDances;
    }

    public int getChorMoveID() {
        return chorMoveID;
    }

    public void setChorMoveID(int chorMoveID) {
        this.chorMoveID = chorMoveID;
    }

    public String getChorName() {
        return chorName;
    }

    public void setChorName(String chorName) {
        this.chorName = chorName;
    }
}
