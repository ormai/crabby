package ui;

import java.awt.Rectangle;

public class PauseButton {
    protected final int y;
    protected final int height;
    protected int x;
    protected int width;
    protected Rectangle bounds;

    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds() {bounds = new Rectangle(x, y, width, height);}

    public Rectangle getBounds() {return bounds;}
}