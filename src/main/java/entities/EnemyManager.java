package entities;

import gamestates.Playing;
import levels.Level;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] crabbyArr, pinkStarArr, sharkArr;
    private Level currentLevel;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    public void loadEnemies(Level level) {this.currentLevel = level;}

    public void update(int[][] lvlData) {
        boolean isAnyActive = false;
        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive()) {
                c.update(lvlData, playing);
                isAnyActive = true;
            }
        }

        for (PinkStar p : currentLevel.getPinkStars()) {
            if (p.isActive()) {
                p.update(lvlData, playing);
                isAnyActive = true;
            }
        }

        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive()) {
                s.update(lvlData, playing);
                isAnyActive = true;
            }
        }

        if (!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawPinkStars(g, xLvlOffset);
        drawSharks(g, xLvlOffset);
    }

    private void drawSharks(Graphics g, int xLvlOffset) {
        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive()) {
                g.drawImage(sharkArr[s.getState()][s.getAniIndex()],
                        (int) s.getHitBox().x - xLvlOffset - SHARK_DRAWOFFSET_X + s.flipX(),
                        (int) s.getHitBox().y - SHARK_DRAWOFFSET_Y + (int) s.getPushDrawOffset(),
                        SHARK_WIDTH * s.flipW(), SHARK_HEIGHT, null);
                if (Constants.SHOW_HITBOXES) {
                    s.drawHitBox(g, xLvlOffset);
                    s.drawAttackBox(g, xLvlOffset);
                }
            }
        }
    }

    private void drawPinkStars(Graphics g, int xLvlOffset) {
        for (PinkStar p : currentLevel.getPinkStars()) {
            if (p.isActive()) {
                g.drawImage(pinkStarArr[p.getState()][p.getAniIndex()],
                        (int) p.getHitBox().x - xLvlOffset - PINKSTAR_DRAWOFFSET_X + p.flipX(),
                        (int) p.getHitBox().y - PINKSTAR_DRAWOFFSET_Y + (int) p.getPushDrawOffset(), PINKSTAR_WIDTH * p.flipW(), PINKSTAR_HEIGHT, null);
                if (Constants.SHOW_HITBOXES) {
                    p.drawHitBox(g, xLvlOffset);
                }
            }
        }
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
                        (int) c.getHitBox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
                        (int) c.getHitBox().y - CRABBY_DRAWOFFSET_Y + (int) c.getPushDrawOffset()
                        , CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);

                if (Constants.SHOW_HITBOXES) {
                    c.drawHitBox(g, xLvlOffset);
                    c.drawAttackBox(g, xLvlOffset);
                }
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive()) {
                if (c.getState() != DEAD && c.getState() != HIT) {
                    if (attackBox.intersects(c.getHitBox())) {
                        c.hurt(20);
                        return;
                    }
                }
            }
        }

        for (PinkStar p : currentLevel.getPinkStars()) {
            if (p.isActive()) {
                if (p.getState() == ATTACK && p.getAniIndex() >= 3) {
                    return;
                } else {
                    if (p.getState() != DEAD && p.getState() != HIT) {
                        if (attackBox.intersects(p.getHitBox())) {
                            p.hurt(20);
                            return;
                        }
                    }
                }
            }
        }

        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive()) {
                if (s.getState() != DEAD && s.getState() != HIT) {
                    if (attackBox.intersects(s.getHitBox())) {
                        s.hurt(20);
                        return;
                    }
                }
            }
        }
    }

    private void loadEnemyImages() {
        crabbyArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9,
                CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
        pinkStarArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8,
                PINKSTAR_WIDTH_DEFAULT, PINKSTAR_HEIGHT_DEFAULT);
        sharkArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8,
                SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT);
    }

    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int spriteW,
                                        int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[5][xSize];
        for (int j = 0; j < tempArr.length; j++) {
            for (int i = 0; i < tempArr[j].length; i++) {
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
            }
        }
        return tempArr;
    }

    public void resetAllEnemies() {
        for (Crabby c : currentLevel.getCrabs()) {
            c.resetEnemy();
        }
        for (PinkStar p : currentLevel.getPinkStars()) {
            p.resetEnemy();
        }
        for (Shark s : currentLevel.getSharks()) {
            s.resetEnemy();
        }
    }
}