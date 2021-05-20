package com.example.android.dancecar;

import java.util.Random;
import java.util.UUID;
import java.util.Random.*;

import java.util.ArrayList;

public class DaneMoveObject {
    String danceName;
    int id;
    Random random = new Random();

    public DaneMoveObject(String danceName) {
        this.danceName = danceName;
        this.id = random.nextInt();
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
}
