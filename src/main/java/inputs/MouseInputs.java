package inputs;

import gamestates.GameState;
import main.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Objects.requireNonNull(GameState.state) == GameState.PLAYING) {
            gamePanel.getGame().getPlaying().mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}