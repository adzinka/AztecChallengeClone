package com.example.aztecchallenge;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Block implements Collisionable {
    private Rectangle rect;
    public static final int BLOCK_SIZE = 60;
    private Image image;
    private double x;
    private double y;

    public Block(double x, double y, Image image) {
        this.rect = new Rectangle(x, y, BLOCK_SIZE, BLOCK_SIZE);
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y, BLOCK_SIZE, BLOCK_SIZE);
    }

    public void simulate(double timeDelta, double cameraX) {
        this.x -= timeDelta * cameraX;
    }

    public double getX() {
        return this.x;
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(x, y, BLOCK_SIZE, BLOCK_SIZE);
    }

    @Override
    public void hitBy(Collisionable another) {

    }
}

