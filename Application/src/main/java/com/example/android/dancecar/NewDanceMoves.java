package com.example.android.dancecar;

import java.util.ArrayList;

public class NewDanceMoves {
    public ArrayList<IndividualMove> individualMoves = new ArrayList();
    String newDanceName;

    public NewDanceMoves(ArrayList<IndividualMove> individualMoves, String newDanceName) {
        this.individualMoves = individualMoves;
        this.newDanceName = newDanceName;
    }

    public ArrayList<IndividualMove> getIndividualMoves() {
        return individualMoves;
    }

    public void setIndividualMoves(ArrayList<IndividualMove> individualMoves) {
        this.individualMoves = individualMoves;
    }

    public String getNewDanceName() {
        return newDanceName;
    }

    public void setNewDanceName(String newDanceName) {
        this.newDanceName = newDanceName;
    }
}
