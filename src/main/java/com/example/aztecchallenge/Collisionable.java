package com.example.aztecchallenge;

import javafx.geometry.Rectangle2D;

public interface Collisionable {
    Rectangle2D getBoundingBox();
    default void checkCollision(Object obj){
        if(obj instanceof Collisionable ca2 && this.getBoundingBox().intersects(ca2.getBoundingBox())) {
            ca2.hitBy(this);
        }
    }
    void hitBy(Collisionable another);
}
