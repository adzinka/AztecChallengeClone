package com.example.aztecchallenge;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameController implements Game.GameEventListener  {

    private Game game;

    @FXML
    private Canvas canvas;

    @FXML
    private Label livesText;

    @FXML
    private Label scoreLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button startGameButton;

    @FXML
    private Label highScoreLabel;

    private AnimationTimer animationTimer;
    private String playerName;
    private ObservableList<Score> allScores;

    public GameController() {
        allScores = FXCollections.observableList(new ArrayList<Score>());
    }

    public void start() {
        this.game = new Game(canvas.getWidth(), canvas.getHeight());

        animationTimer = new DrawingThread(canvas, game);
        animationTimer.start();
        game.setGameEventListener(this);
        drawLives(game.player.getLives());
        updateScoreDisplay();
        initializeKeyHandling();
        drawHighScore();
        game.setPlayerName(playerName);
    }

    public void drawLives(int lives) {
        StringBuilder livesDisplay = new StringBuilder();
        if (lives != 0) {
            for (int i = 0; i < lives; i++) {
                livesDisplay.append("\uD83D\uDC9C");
            }
        } else {
            //System.out.println("NO LIVES");
            livesDisplay = new StringBuilder("NO LIVES");
        }
        livesText.setText(livesDisplay.toString());
    }

    private void drawHighScore() {
        if (!allScores.isEmpty()) {
            highScoreLabel.setText("HIGH SCORE: " + allScores.get(0).getScore());
        }
    }

    private void initializeKeyHandling() {
        Scene scene = canvas.getScene();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    game.player.jump();
                    break;
            }
        });
    }

    @Override
    public void onGameEnd() {
        drawLives(game.player.getLives());
        stopGame();
        System.out.println(game.player.getScore());
    }

    @Override
    public void onGameRestart() {
        drawLives(game.player.getLives());
        saveHighScore(game.getCurrentScore().getScore());
        System.out.println(game.player.getScore());
    }

    private void saveHighScore(int score) {

        boolean scoreUpdated = false;
        for (Score s : allScores) {
            if (s.getName().equals(playerName)) {
                if (score > s.getScore()) {
                    s.setScore(score);
                }
                scoreUpdated = true;
                break;
            }
        }

        if (!scoreUpdated) {
            allScores.add(new Score(playerName, score));
        }

        allScores.sort((x, y) -> y.getScore() - x.getScore());
        saveScores();
        drawHighScore();
    }

    @Override
    public void updateScoreDisplay() {
        scoreLabel.setText(String.valueOf(game.player.getScore()));
    }

    public void onStartGameButtonPressed(ActionEvent actionEvent) {
        playerName = nameTextField.getText();

        if (!playerName.isEmpty()) {
            start();
            startGameButton.setVisible(false);
            nameTextField.setVisible(false);
            loadScores();
            drawHighScore();
        }
    }

    public void stopGame() {
        saveCurrentScore();
        saveScores();
        animationTimer.stop();
    }

    public void saveCurrentScore() {
        allScores.add(game.getCurrentScore());

        allScores.sort((x, y) -> y.getScore() - x.getScore());
    }

    public void saveScores() {
        Set<Score> bestPlayers =  new HashSet<Score>();

        bestPlayers.addAll(allScores);

        allScores.clear();

        for (Score score : bestPlayers) {
            allScores.add(score);
        }

        allScores.sort((x, y) -> y.getScore() - x.getScore());

        try (FileWriter fw = new FileWriter("data.csv")) {
            for(Score score : allScores) {
                fw.write(score.getName() + ";" + score.getScore() + "\n");
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public void loadScores() {
        try (Scanner sn = new Scanner(new File("data.csv"))) {
            sn.useDelimiter("[;\\n]");
            while (sn.hasNext()) {
                String name = sn.next();
                int scoreVal = sn.nextInt();
                allScores.add(new Score(name, scoreVal));
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}

