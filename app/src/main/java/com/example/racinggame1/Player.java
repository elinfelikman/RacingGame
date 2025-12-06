package com.example.racinggame1;


public class Player {
    private int lane;
    private int lives;


    public Player(int initialLane, int initialLives) {
        this.lane = initialLane;
        this.lives = initialLives;
    }


    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }


}
