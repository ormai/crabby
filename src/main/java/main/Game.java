package main;

import audio.AudioPlayer;
import gamestates.Credits;
import gamestates.GameOptions;
import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;

import java.awt.Graphics;

public class Game implements Runnable {
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    private static final boolean SHOW_FPS_UPS = true;
    private static boolean running = true;
    private final GamePanel gamePanel;
    private Playing playing;
    private Menu menu;
    private Credits credits;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    public Game() {
        System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
        initClasses();
        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }

    public static void setRunning(boolean running) {Game.running = running;}

    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        credits = new Credits(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case OPTIONS -> gameOptions.update();
            case CREDITS -> credits.update();
            case QUIT -> {
                Game.setRunning(false);
                System.exit(0);
            }
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            case CREDITS -> credits.draw(g);
        }
    }

    @Override
    public void run() {
        int FPS_SET = 120;
        double timePerFrame = 1000000000.0 / FPS_SET;
        int UPS_SET = 200;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (running) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (SHOW_FPS_UPS) {
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.printf("\rFPS: %3d, UPS: %3d", frames, updates);
                    frames = 0;
                    updates = 0;
                }
            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu() {return menu;}

    public Playing getPlaying() {return playing;}

    public Credits getCredits() {return credits;}

    public GameOptions getGameOptions() {return gameOptions;}

    public AudioOptions getAudioOptions() {return audioOptions;}

    public AudioPlayer getAudioPlayer() {return audioPlayer;}

    public static void main(String[] args) {
        new Game();
    }
}