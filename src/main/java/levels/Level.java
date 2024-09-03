package levels;

import entities.Crabby;
import entities.PinkStar;
import entities.Shark;
import main.Game;
import objects.BackgroundTree;
import objects.Cannon;
import objects.GameContainer;
import objects.Grass;
import objects.Potion;
import objects.Spike;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;

public class Level {
    private final BufferedImage image;
    private final int[][] levelData;

    private final ArrayList<Crabby> crabs = new ArrayList<>();
    private final ArrayList<PinkStar> pinkStars = new ArrayList<>();
    private final ArrayList<Shark> sharks = new ArrayList<>();
    private final ArrayList<Potion> potions = new ArrayList<>();
    private final ArrayList<Spike> spikes = new ArrayList<>();
    private final ArrayList<GameContainer> containers = new ArrayList<>();
    private final ArrayList<Cannon> cannons = new ArrayList<>();
    private final ArrayList<BackgroundTree> trees = new ArrayList<>();
    private final ArrayList<Grass> grass = new ArrayList<>();

    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {
        this.image = image;
        levelData = new int[image.getHeight()][image.getWidth()];
        loadLevel();
        calcLvlOffsets();
    }

    private void loadLevel() {
        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc...
        // Removed many methods in HelpMethods class.

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color c = new Color(image.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
        }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50) {
            levelData[y][x] = 0;
        } else {
            levelData[y][x] = redValue;
        }
        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                    grass.add(new Grass(x * Game.TILES_SIZE,
                            (y * Game.TILES_SIZE) - Game.TILES_SIZE, getRndGrassType(x)));
        }
    }

    private int getRndGrassType(int xPos) {return xPos % 2;}

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case CRABBY -> crabs.add(new Crabby(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case PINKSTAR -> pinkStars.add(new PinkStar(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case SHARK -> sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, BLUE_POTION ->
                    potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL ->
                    containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE,
                            blueValue));
            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT ->
                    cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case TREE_ONE, TREE_TWO, TREE_THREE ->
                    trees.add(new BackgroundTree(x * Game.TILES_SIZE, y * Game.TILES_SIZE,
                            blueValue));
        }
    }

    private void calcLvlOffsets() {
        int lvlTilesWide = image.getWidth();
        int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y) {return levelData[y][x];}

    public int[][] getLevelData() {return levelData;}

    public int getLvlOffset() {return maxLvlOffsetX;}

    public Point getPlayerSpawn() {return playerSpawn;}

    public ArrayList<Crabby> getCrabs() {return crabs;}

    public ArrayList<Shark> getSharks() {return sharks;}

    public ArrayList<Potion> getPotions() {return potions;}

    public ArrayList<GameContainer> getContainers() {return containers;}

    public ArrayList<Spike> getSpikes() {return spikes;}

    public ArrayList<Cannon> getCannons() {return cannons;}

    public ArrayList<PinkStar> getPinkStars() {return pinkStars;}

    public ArrayList<BackgroundTree> getTrees() {return trees;}

    public ArrayList<Grass> getGrass() {return grass;}
}