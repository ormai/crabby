package effects;

import main.Game;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Rain {
    private final Point2D.Float[] drops;
    private final Random rand;
    private final BufferedImage rainParticle;

    // Worth knowing, adding particles this way can cost a lot in
    // computer power. Disable it if the game lags.
    public Rain() {
        rand = new Random();
        drops = new Point2D.Float[1000];
        rainParticle = LoadSave.GetSpriteAtlas(LoadSave.RAIN_PARTICLE);
        initDrops();
    }

    private void initDrops() {
        for (int i = 0; i < drops.length; i++) {
            drops[i] = getRndPos();
        }
    }

    private Point2D.Float getRndPos() {
        return new Point2D.Float((int) getNewX(0), rand.nextInt(Game.GAME_HEIGHT));
    }

    public void update(int xLvlOffset) {
        for (Point2D.Float p : drops) {
            float rainSpeed = 1.25f;
            p.y += rainSpeed;
            if (p.y >= Game.GAME_HEIGHT) {
                p.y = -20;
                p.x = getNewX(xLvlOffset);
            }
        }
    }

    private float getNewX(int xLvlOffset) {
        return (float) ((-Game.GAME_WIDTH) + rand.nextInt((int) (Game.GAME_WIDTH * 3f)) + xLvlOffset);
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (Point2D.Float p : drops) {
            g.drawImage(rainParticle, (int) p.getX() - xLvlOffset, (int) p.getY(), 3, 12, null);
        }
    }
}
