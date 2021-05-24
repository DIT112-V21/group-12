package com.example.android.dancecar;

import androidx.annotation.NonNull;

public class IndividualMove {
    private String carInstruction;
    private int duration;

    public IndividualMove(String carInstruction, int duration) {
        this.carInstruction = carInstruction;
        this.duration = duration;

    }


    @NonNull
    @Override
    public String toString() {
        return carInstruction;
    }

    public String getCarInstruction() {
        return carInstruction;
    }

    public void setCarInstruction(String carInstruction) {
        this.carInstruction = carInstruction;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
