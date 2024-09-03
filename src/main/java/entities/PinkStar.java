package entities;

import gamestates.Playing;

import static utilz.Constants.Dialogue.QUESTION;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;

public class PinkStar extends Enemy {
    private boolean preRoll = true;
    private int tickSinceLastDmgToPlayer;
    private int tickAfterRollInIdle;
    private int rollDurationTick;

    public PinkStar(float x, float y) {
        super(x, y, PINKSTAR_WIDTH, PINKSTAR_HEIGHT, PINKSTAR);
        initHitBox(17, 21);
    }

    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
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
                    preRoll = true;
                    if (tickAfterRollInIdle >= 120) {
                        if (IsFloor(hitBox, lvlData)) {
                            newState(RUNNING);
                        } else {
                            inAir = true;
                        }
                        tickAfterRollInIdle = 0;
                        tickSinceLastDmgToPlayer = 60;
                    } else {
                        tickAfterRollInIdle++;
                    }
                }
                case RUNNING -> {
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        newState(ATTACK);
                        setWalkDir(playing.getPlayer());
                    }
                    move(lvlData, playing);
                }
                case ATTACK -> {
                    if (preRoll) {
                        if (aniIndex >= 3) {
                            preRoll = false;
                        }
                    } else {
                        move(lvlData, playing);
                        checkDmgToPlayer(playing.getPlayer());
                        checkRollOver(playing);
                    }
                }
                case HIT -> {
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2) {
                        pushBack(pushBackDir, lvlData, 2f);
                    }
                    updatePushBackDrawOffset();
                    tickAfterRollInIdle = 120;
                }
            }
        }
    }

    private void checkDmgToPlayer(Player player) {
        if (hitBox.intersects(player.getHitBox())) {
            if (tickSinceLastDmgToPlayer >= 60) {
                tickSinceLastDmgToPlayer = 0;
                player.changeHealth(-GetEnemyDmg(enemyType), this);
            } else {
                tickSinceLastDmgToPlayer++;
            }
        }
    }

    private void setWalkDir(Player player) {
        if (player.getHitBox().x > hitBox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected void move(int[][] lvlData, Playing playing) {
        float xSpeed;

        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (state == ATTACK) {
            xSpeed *= 2;
        }

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            if (IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }
        }

        if (state == ATTACK) {
            rollOver(playing);
            rollDurationTick = 0;
        }

        changeWalkDir();
    }

    private void checkRollOver(Playing playing) {
        rollDurationTick++;
        int rollDuration = 300;
        if (rollDurationTick >= rollDuration) {
            rollOver(playing);
            rollDurationTick = 0;
        }
    }

    private void rollOver(Playing playing) {
        newState(IDLE);
        playing.addDialogue((int) hitBox.x, (int) hitBox.y, QUESTION);
    }
}