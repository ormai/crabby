package effects;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.Dialogue.GetSpriteAmount;

public class DialogueEffect {
    private final int type;
    private int x;
    private int y;
    private int aniIndex, aniTick;
    private boolean active = true;

    public DialogueEffect(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(type)) {
                active = false;
                aniIndex = 0;
            }
        }
    }

    public void deactivate() {active = false;}

    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        active = true;
    }

    public int getAniIndex() {return aniIndex;}

    public int getX() {return x;}

    public int getY() {return y;}

    public int getType() {return type;}

    public boolean isActive() {return active;}
}