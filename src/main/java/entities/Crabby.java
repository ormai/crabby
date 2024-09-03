package entities;

import gamestates.Playing;

import static utilz.Constants.Dialogue.EXCLAMATION;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(22, 19);
        initAttackBox(82, 19, 30);
    }

    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Playing playing) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }

        if (inAir) {
            inAirChecks(lvlData, playing);
        } else {
            switch (state) {
                case IDLE -> {
                    if (IsFloor(hitBox, lvlData)) {
                        newState(RUNNING);
                    } else {
                        inAir = true;
                    }
                }
                case RUNNING -> {
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        if (isPlayerCloseForAttack(playing.getPlayer())) {
                            newState(ATTACK);
                        }
                    }
                    move(lvlData);

                    if (inAir) {
                        playing.addDialogue((int) hitBox.x, (int) hitBox.y, EXCLAMATION);
                    }
                }
                case ATTACK -> {
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 3 && !attackChecked) {
                        checkPlayerHit(attackBox, playing.getPlayer());
                    }
                }
                case HIT -> {
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2) {
                        pushBack(pushBackDir, lvlData, 2f);
                    }
                    updatePushBackDrawOffset();
                }
            }
        }
    }
}