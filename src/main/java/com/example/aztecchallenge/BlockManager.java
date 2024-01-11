package com.example.aztecchallenge;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class BlockManager {
    private Game game;
    private ArrayList<Block> blocks;
    private Image blockImage;

    public BlockManager(Game game, String[] levelData, Image blockImage) {
        this.game = game;
        this.blocks = new ArrayList<>();
        this.blockImage = blockImage;
        initializeBlocks(levelData);
    }

    private void initializeBlocks(String[] levelData) {
        final int BLOCK_SIZE = 60;
        for (int row = 0; row < levelData.length; row++) {
            String rowString = levelData[row];
            for (int col = 0; col < rowString.length(); col++) {
                if (rowString.charAt(col) == '1') {
                    double x = col * BLOCK_SIZE;
                    double y = row * BLOCK_SIZE;
                    blocks.add(new Block(x, y, blockImage));
                }
            }
        }
    }

    public void simulate(double timeDelta) {
        for (Block block : blocks) {
            block.simulate(timeDelta, game.getCameraSpeed());
        }
    }

    public void draw(GraphicsContext gc) {
        for (Block block : blocks) {
            block.draw(gc);
        }
    }

    public double getLastBlockX() {
        if (blocks.isEmpty()) {
            return 0;
        }
        Block lastBlock = blocks.get(blocks.size() - 1);
        return lastBlock.getX();
    }

    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    public boolean isPlayerOnBlock(Player player) {
        for (Block block : blocks) {
            if (player.getBoundingBox().intersects(block.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }
}
