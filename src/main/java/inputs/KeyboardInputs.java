package inputs;

import gamestates.GameState;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private final GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {this.gamePanel = gamePanel;}

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            case CREDITS -> gamePanel.getGame().getCredits().keyReleased(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}