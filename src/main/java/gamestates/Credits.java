package gamestates;

import main.Game;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Credits extends State implements StateMethods {
    private final BufferedImage backgroundImg;
    private final BufferedImage creditsImg;
    private final int bgX;
    private final int bgY;
    private final int bgW;
    private final int bgH;
    private float bgYFloat;

    private ArrayList<ShowEntity> entitiesList;

    public Credits(Game game) {
        super(game);
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        creditsImg = LoadSave.GetSpriteAtlas(LoadSave.CREDITS);
        bgW = (int) (creditsImg.getWidth() * Game.SCALE);
        bgH = (int) (creditsImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = Game.GAME_HEIGHT;
        loadEntities();
    }

    private void loadEntities() {
        entitiesList = new ArrayList<>();
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS)
                , 5, 64, 40), (int) (Game.GAME_WIDTH * 0.05), (int) (Game.GAME_HEIGHT * 0.8)));
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 72, 32), (int) (Game.GAME_WIDTH * 0.15), (int) (Game.GAME_HEIGHT * 0.75)));
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8, 34, 30), (int) (Game.GAME_WIDTH * 0.7), (int) (Game.GAME_HEIGHT * 0.75)));
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS),
                8, 34, 30), (int) (Game.GAME_WIDTH * 0.8), (int) (Game.GAME_HEIGHT * 0.8)));
    }

    private BufferedImage[] getIdleAni(BufferedImage atlas, int spritesAmount, int width,
                                       int height) {
        BufferedImage[] arr = new BufferedImage[spritesAmount];
        for (int i = 0; i < spritesAmount; i++) {
            arr[i] = atlas.getSubimage(width * i, 0, width, height);
        }
        return arr;
    }

    @Override
    public void update() {
        bgYFloat -= 0.2f;
        for (ShowEntity se : entitiesList) {
            se.update();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(creditsImg, bgX, (int) (bgY + bgYFloat), bgW, bgH, null);

        for (ShowEntity se : entitiesList) {
            se.draw(g);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            bgYFloat = 0;
            setGameState(GameState.MENU);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    private static class ShowEntity {
        private final BufferedImage[] idleAnimation;
        private final int x;
        private final int y;
        private int aniIndex;
        private int aniTick;

        public ShowEntity(BufferedImage[] idleAnimation, int x, int y) {
            this.idleAnimation = idleAnimation;
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics g) {
            g.drawImage(idleAnimation[aniIndex], x, y,
                    idleAnimation[aniIndex].getWidth() * 4,
                    idleAnimation[aniIndex].getHeight() * 4, null);
        }

        public void update() {
            aniTick++;
            if (aniTick >= 25) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= idleAnimation.length) {
                    aniIndex = 0;
                }
            }
        }
    }
}
