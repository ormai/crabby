package entities;

import main.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.CanMoveHere;

public abstract class Entity {
    protected final int width;
    protected final int height;
    protected float x, y;
    protected Rectangle2D.Float hitBox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit) {
                pushBackOffsetDir = DOWN;
            }
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0) {
                pushDrawOffset = 0;
            }
        }
    }

    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed;
        if (pushBackDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (CanMoveHere(hitBox.x + xSpeed * speedMulti, hitBox.y, hitBox.width, hitBox.height,
                lvlData)) {
            hitBox.x += xSpeed * speedMulti;
        }
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width,
                (int) attackBox.height);
    }

    protected void drawHitBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLvlOffset, (int) hitBox.y, (int) hitBox.width,
                (int) hitBox.height);
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE),
                (int) (height * Game.SCALE));
    }

    public Rectangle2D.Float getHitBox() {return hitBox;}

    public int getState() {return state;}

    public int getAniIndex() {return aniIndex;}

    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }
}