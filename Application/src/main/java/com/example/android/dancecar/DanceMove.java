package com.example.android.dancecar;

import java.util.Random;
import java.util.UUID;
import java.util.Random.*;

import java.util.ArrayList;

public class DanceMove {
    private String danceName;
    private int id;
    private Random random = new Random();
    private boolean isCreated;

    public DanceMove(String danceName) {
        this.danceName = danceName;
        this.id = random.nextInt();
        this.isCreated = isCreated();
    }

    public String getDanceName() {
        return danceName;
    }

    public void setDanceName(String danceName) {
        this.danceName = danceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public boolean isCreated() { return isCreated; }

    public void setCreated(boolean created) { isCreated = created; }
}
