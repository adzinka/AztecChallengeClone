package com.example.aztecchallenge;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player implements Collisionable {

    private Point2D position;
    private String name;
    private Game game;
    private Image image;

    private Point2D velocity;
    protected double size;
    private boolean canJump = true;
    private int lives;
    private int score = 0;
    private boolean onBlock = false;

    public Player(Game game, Image image, Point2D start, Point2D velocity, double size, int lives) {
        this.game = game;
        this.image = image;
        this.velocity = velocity;
        this.size = size;
        this.position = start;
        this.lives = lives;
    }

    public void setName(String name) {
        this.name = name;
    }

    void drawPlayer(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY(), size, size);
    }

    public void jump() {
        if (canJump) {
            velocity = new Point2D(velocity.getX(), -25);
            canJump = false;
        }
    }

    public void update() {
        velocity = velocity.add(0, 1);

        position = position.add(velocity);

        if (position.getY() >= game.getHeight() - size) {
            position = new Point2D(position.getX(), game.getHeight() - size);
            canJump = true;
            velocity = new Point2D(velocity.getX(), 0);
            onBlock = false;
        } else {
            checkOnBlock();
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size, size);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Block) {
            Block block = (Block) another;
            Rectangle2D blockBounds = block.getBoundingBox();

            if (isCollidingVertically(blockBounds) && isMovingDownward()) {
                handleLanding(blockBounds);
            } else if (isCollidingHorizontally(blockBounds) ) {
                deductLife();
            }
        }
    }

    private void deductLife() {
        lives--;

        if (lives <= 0) {
            game.gameOver();
        } else {
            //System.out.println(score + " score");
            game.resetGame(lives, score);
        }
    }

    private void checkOnBlock() {
        if (game.getBlockManager().isPlayerOnBlock(this)) {
            if (!onBlock) {
                onBlock = true;
            }
        } else {
            if (onBlock) {
                increaseScore(50);
                onBlock = false;
                game.updateScore();
            }
        }
    }

    private boolean isMovingDownward() {
        return velocity.getY() > 0;
    }

    private boolean isCollidingVertically(Rectangle2D blockBounds) {
        double playerBottom = position.getY() + size;
        return playerBottom >= blockBounds.getMinY() && playerBottom <= blockBounds.getMaxY();
    }

    private void handleLanding(Rectangle2D blockBounds) {
        position = new Point2D(position.getX(), blockBounds.getMinY() - size);
        velocity = new Point2D(velocity.getX(), 0);
        canJump = true;
        onBlock = true;
    }

    private boolean isCollidingHorizontally(Rectangle2D blockBounds) {
        double playerFrontSide = position.getX() + size;
        boolean horizontalOverlap = playerFrontSide >= blockBounds.getMinX() && playerFrontSide <= blockBounds.getMaxX();
        boolean verticalOverlap = position.getY() + size > blockBounds.getMinY() && position.getY() < blockBounds.getMaxY();

        return horizontalOverlap && verticalOverlap;
    }

    public void increaseScore(int points) {
        score += points;
    }

    public String getName() {
        return this.name;
    }

    public Point2D getPosition() {
        return this.position;
    }

    public int getLives() {
        return this.lives;
    }

    public int getScore() {
        return this.score;
    }

}
