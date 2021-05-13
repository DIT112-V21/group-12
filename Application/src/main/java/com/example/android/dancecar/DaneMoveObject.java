package com.example.android.dancecar;

public class DaneMoveObject {
    String name;
    int id;

    public DaneMoveObject(String danceName, int id) {
        this.name = danceName;
        this.id = id;
    }

    /* TODO: new constructor for user's created moves
    public DanceMove(String danceName, int id, ArrayList) {
        this.name = danceName;
        this.id = id;
    }
     */

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
