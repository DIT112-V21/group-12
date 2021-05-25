package com.example.android.dancecar;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class Choreography {
    ArrayList<DanceMove> selectedDances;
    int chorMoveID;
    String chorName;
    Random random = new Random();

    public Choreography(ArrayList<DanceMove> selectedDances, String chorName) {
        this.selectedDances = selectedDances;
        this.chorMoveID = random.nextInt();
        this.chorName = chorName;
    }



    public ArrayList<DanceMove> getSelectedDances() {
        return selectedDances;
    }

    public void setSelectedDances(ArrayList<DanceMove> selectedDances) {
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
