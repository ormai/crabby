package ui;

import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {
    private final int minX;
    private final int maxX;
    private BufferedImage[] images;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX;
    private float floatValue = 0f;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH,
                    VOLUME_DEFAULT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH,
                VOLUME_DEFAULT_HEIGHT);
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

    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(images[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        if (x < minX) {
            buttonX = minX;
        } else {
            buttonX = Math.min(x, maxX);
        }
        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public void setMouseOver(boolean mouseOver) {this.mouseOver = mouseOver;}

    public boolean isMousePressed() {return mousePressed;}

    public void setMousePressed(boolean mousePressed) {this.mousePressed = mousePressed;}

    public float getFloatValue() {return floatValue;}
}