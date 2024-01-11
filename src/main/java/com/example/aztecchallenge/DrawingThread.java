package com.example.aztecchallenge;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawingThread extends AnimationTimer {

    private final GraphicsContext gc;

    private long lastTime;
    private final Game game;
    private final Canvas canvas;

    public DrawingThread(Canvas canvas, Game game) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.game = game;
    }

    @Override
    public void handle(long now) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        game.draw(gc);
        game.player.update();
        if (lastTime > 0) {
            game.simulate((now - lastTime) / 1e9);
        }
        lastTime = now;

    }

}
