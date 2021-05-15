package com.example.android.dancecar;

public class IndividualMove {
    private String carInstruction;
    private long duration;


    public IndividualMove(String carInstruction, long duration) {
        this.carInstruction = carInstruction;
        this.duration = duration;

    }

    public String getCarInstruction() {
        return carInstruction;
    }

    public void setCarInstruction(String carInstruction) {
        this.carInstruction = carInstruction;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
