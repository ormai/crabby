package ui;

import gamestates.GameState;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.Buttons.*;

public class MenuButton {
    private final int xPos;
    private final int yPos;
    private final int rowIndex;
    private final int xOffsetCenter = B_WIDTH / 2;
    private final GameState state;
    private int index;
    private BufferedImage[] images;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImages() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT,
                    B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(images[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void setMouseOver(boolean mouseOver) {this.mouseOver = mouseOver;}

    public boolean isMousePressed() {return mousePressed;}

    public void setMousePressed(boolean mousePressed) {this.mousePressed = mousePressed;}

    public Rectangle getBounds() {return bounds;}

    public void applyGameState() {GameState.state = state;}

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public GameState getState() {return state;}
}