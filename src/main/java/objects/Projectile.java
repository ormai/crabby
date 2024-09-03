package objects;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Projectiles.*;

public class Projectile {
    private final Rectangle2D.Float hitBox;
    private final int dir;
    private boolean active = true;

    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (5 * Game.SCALE);

        if (dir == 1) {
            xOffset = (int) (29 * Game.SCALE);
        }

        hitBox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH,
                CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {hitBox.x += dir * SPEED;}

    public Rectangle2D.Float getHitBox() {return hitBox;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}
}