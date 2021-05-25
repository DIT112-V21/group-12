package com.example.android.dancecar;

import java.util.ArrayList;

public class CreatedDanceMove {
    public ArrayList<IndividualMove> individualMoves = new ArrayList();
    String newDanceName;

    public CreatedDanceMove(ArrayList<IndividualMove> individualMoves, String newDanceName) {
        this.individualMoves = individualMoves;
        this.newDanceName = newDanceName;
    }

    public ArrayList<IndividualMove> getIndividualMoves() {
        return individualMoves;
    }

    public void setIndividualMoves(ArrayList<IndividualMove> individualMoves) {
        this.individualMoves = individualMoves;
    }

    public String getName() {
        return newDanceName;
    }

    public void setNewDanceName(String newDanceName) {
        this.newDanceName = newDanceName;
    }

    @Override
    public String toString() {
        return "CreatedDanceMove{" +
                "individualMoves=" + individualMoves +
                ", newDanceName='" + newDanceName + '\'' +
                '}';
    }
}
