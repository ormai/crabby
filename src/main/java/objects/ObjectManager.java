package objects;

import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.CANNON_BALL_HEIGHT;
import static utilz.Constants.Projectiles.CANNON_BALL_WIDTH;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private final Playing playing;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private BufferedImage[][] potionImages, containerImages;
    private BufferedImage[] cannonImages, grassImages;
    private BufferedImage[][] treeImages;
    private BufferedImage spikeImg, cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private Level currentLevel;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImages();
    }

    public void checkSpikesTouched(Player p) {
        for (Spike s : currentLevel.getSpikes()) {
            if (s.getHitBox().intersects(p.getHitBox())) {
                p.kill();
            }
        }
    }

    public void checkSpikesTouched(Enemy e) {
        for (Spike s : currentLevel.getSpikes()) {
            if (s.getHitBox().intersects(e.getHitBox())) {
                e.hurt(200);
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitBox) {
        for (Potion p : potions) {
            if (p.isActive()) {
                if (hitBox.intersects(p.getHitBox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION) {
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        for (GameContainer gc : containers) {
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitBox().intersects(attackBox)) {
                    gc.setAnimation(true);
                    int type = 0;
                    if (gc.getObjType() == BARREL) {
                        type = 1;
                    }
                    potions.add(new Potion((int) (gc.getHitBox().x + gc.getHitBox().width / 2),
                            (int) (gc.getHitBox().y - gc.getHitBox().height / 2), type));
                    return;
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        currentLevel = newLevel;
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        projectiles.clear();
    }

    private void loadImages() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImages = new BufferedImage[2][7];

        for (int j = 0; j < potionImages.length; j++) {
            for (int i = 0; i < potionImages[j].length; i++) {
                potionImages[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];

        for (int j = 0; j < containerImages.length; j++) {
            for (int i = 0; i < containerImages[j].length; i++) {
                containerImages[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImages = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImages.length; i++) {
            cannonImages[i] = temp.getSubimage(i * 40, 0, 40, 26);
        }

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
        treeImages = new BufferedImage[2][4];
        BufferedImage treeOneImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_ONE_ATLAS);
        for (int i = 0; i < 4; i++) {
            treeImages[0][i] = treeOneImg.getSubimage(i * 39, 0, 39, 92);
        }

        BufferedImage treeTwoImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_TWO_ATLAS);
        for (int i = 0; i < 4; i++) {
            treeImages[1][i] = treeTwoImg.getSubimage(i * 62, 0, 62, 54);
        }

        BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.GRASS_ATLAS);
        grassImages = new BufferedImage[2];
        for (int i = 0; i < grassImages.length; i++) {
            grassImages[i] = grassTemp.getSubimage(32 * i, 0, 32, 32);
        }
    }

    public void update(int[][] lvlData, Player player) {
        updateBackgroundTrees();
        for (Potion p : potions) {
            if (p.isActive()) {
                p.update();
            }
        }

        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                gc.update();
            }
        }

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateBackgroundTrees() {
        for (BackgroundTree bt : currentLevel.getTrees()) {
            bt.update();
        }
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitBox().intersects(player.getHitBox())) {
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData)) {
                    p.setActive(false);
                }
            }
        }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitBox().x - c.getHitBox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            return c.getHitBox().x > player.getHitBox().x;
        } else {
            return c.getHitBox().x < player.getHitBox().x;
        }
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : currentLevel.getCannons()) {
            if (!c.doAnimation) {
                if (c.getTileY() == player.getTileY()) {
                    if (isPlayerInRange(c, player)) {
                        if (isPlayerInFrontOfCannon(c, player)) {
                            if (CanCannonSeePlayer(lvlData, player.getHitBox(), c.getHitBox(),
                                    c.getTileY())) {
                                c.setAnimation(true);
                            }
                        }
                    }
                }
            }

            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 0) {
                shootCannon(c);
            }
        }
    }

    private void shootCannon(Cannon c) {
        int dir = 1;
        if (c.getObjType() == CANNON_LEFT) {
            dir = -1;
        }

        projectiles.add(new Projectile((int) c.getHitBox().x, (int) c.getHitBox().y, dir));
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawGrass(g, xLvlOffset);
    }

    private void drawGrass(Graphics g, int xLvlOffset) {
        for (Grass grass : currentLevel.getGrass()) {
            g.drawImage(grassImages[grass.type()], grass.x() - xLvlOffset, grass.y(),
                    (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
        }
    }

    public void drawBackgroundTrees(Graphics g, int xLvlOffset) {
        for (BackgroundTree bt : currentLevel.getTrees()) {
            int type = bt.getType();
            if (type == 9) {
                type = 8;
            }
            g.drawImage(treeImages[type - 7][bt.getAniIndex()],
                    bt.getX() - xLvlOffset + GetTreeOffsetX(bt.getType()),
                    bt.getY() + GetTreeOffsetY(bt.getType()), GetTreeWidth(bt.getType()),
                    GetTreeHeight(bt.getType()), null);
        }
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                g.drawImage(cannonBallImg, (int) (p.getHitBox().x - xLvlOffset),
                        (int) (p.getHitBox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : currentLevel.getCannons()) {
            int x = (int) (c.getHitBox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImages[c.getAniIndex()], x, (int) (c.getHitBox().y), width,
                    CANNON_HEIGHT, null);
            if (Constants.SHOW_HITBOXES) {
                c.drawHitBox(g, xLvlOffset);
            }
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes()) {
            g.drawImage(spikeImg, (int) (s.getHitBox().x - xLvlOffset),
                    (int) (s.getHitBox().y - s.getYDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
            if (Constants.SHOW_HITBOXES) {
                s.drawHitBox(g, xLvlOffset);
            }
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BARREL) {
                    type = 1;
                }
                g.drawImage(containerImages[type][gc.getAniIndex()],
                        (int) (gc.getHitBox().x - gc.getXDrawOffset() - xLvlOffset),
                        (int) (gc.getHitBox().y - gc.getYDrawOffset()), CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null);
                if (Constants.SHOW_HITBOXES) {
                    gc.drawHitBox(g, xLvlOffset);
                }
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION) {
                    type = 1;
                }
                g.drawImage(potionImages[type][p.getAniIndex()],
                        (int) (p.getHitBox().x - p.getXDrawOffset() - xLvlOffset),
                        (int) (p.getHitBox().y - p.getYDrawOffset()), POTION_WIDTH, POTION_HEIGHT,
                        null);
                if (Constants.SHOW_HITBOXES) {
                    p.drawHitBox(g, xLvlOffset);
                }
            }
        }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p : potions) {
            p.reset();
        }
        for (GameContainer gc : containers) {
            gc.reset();
        }
        for (Cannon c : currentLevel.getCannons()) {
            c.reset();
        }
    }
}