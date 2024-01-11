package com.example.aztecchallenge;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Game {
    private final double width;
    private final double height;

    private static final double CAMERA_SPEED = 200.0;

    public Player player;
    private Score currentScore;
    private final Image backgroundImage;
    private final Image blockImage;
    private final Image playerImage;
    private BlockManager blockManager;

    public Game(double width, double height) {
        this.width = width;
        this.height = height;

        this.backgroundImage = new Image(getClass().getResourceAsStream("background.jpg"), width, height, true, true);
        this.blockImage = new Image(getClass().getResourceAsStream("brick.jpg"), 60, 60, true, true);
        this.playerImage = new Image(getClass().getResourceAsStream("pla.gif"), 40, 40, true, true);

        this.player = new Player(this, playerImage, new Point2D(300, height - 40), new Point2D(0, 0), 50, 6);
        this.blockManager = new BlockManager(this, LevelData.LEVEL1, blockImage);
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        gc.drawImage(backgroundImage, 0, 0, width, height);
        blockManager.draw(gc);
        player.drawPlayer(gc);
    }

    public void simulate(double timeDelta) {
        player.update();

        blockManager.simulate(timeDelta);
        double lastBlockX = blockManager.getLastBlockX();
        // lastBlockX <= width + player.getPosition().getX() - bylo
        if (player.getPosition().getX() >= lastBlockX) {
            gameOver();
        }

        for (Block block : blockManager.getBlocks()) {
            block.checkCollision(player);
        }
    }

    private GameEventListener gameEventListener;

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }

    public void resetGame(int lives, int score) {
        currentScore.setScore(score);
        //System.out.println(currentScore.getScore() + "current");
        this.player = new Player(this, playerImage, new Point2D(300, height - 40), new Point2D(0, 0), 50, lives);
        this.blockManager = new BlockManager(this, LevelData.LEVEL1, blockImage);
        if (gameEventListener != null) {
            gameEventListener.onGameRestart();
        }
    }

    public void gameOver() {
        System.out.println(player.getScore());
        currentScore.setScore(player.getScore());
        if (gameEventListener != null) {
            gameEventListener.onGameEnd();
        }
    }

    public void setPlayerName(String playerName) {
        player.setName(playerName);
        currentScore = new Score(playerName, 0);
    }

    public void updateScore() {
        if (player != null && player.getLives() == 1) currentScore.setScore(player.getScore());
        if (gameEventListener != null) {
            gameEventListener.updateScoreDisplay();
        }
    }

    public double getHeight() {
        return height;
    }
    public double getWidth() {
        return width;
    }
    public double getCameraSpeed() {
        return CAMERA_SPEED;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public Score getCurrentScore() {
        return this.currentScore;
    }

    public interface GameEventListener {
        void onGameEnd();
        void onGameRestart();
        void updateScoreDisplay();
    }


}
