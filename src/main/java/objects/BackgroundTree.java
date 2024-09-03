package objects;

import java.util.Random;

public class BackgroundTree {
    private final int x;
    private final int y;
    private final int type;
    private int aniIndex;
    private int aniTick;

    public BackgroundTree(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Sets the aniIndex to a random value, to get some variations for the trees so
        // they all don't move in sync.
        Random r = new Random();
        aniIndex = r.nextInt(4);
    }

    public void update() {
        aniTick++;
        if (aniTick >= 35) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4) {
                aniIndex = 0;
            }
        }
    }

    public int getAniIndex() {return aniIndex;}

    public int getX() {return x;}

    public int getY() {return y;}

    public int getType() {return type;}
}