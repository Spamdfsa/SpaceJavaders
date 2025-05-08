package main.java.spaceinvaders.model;

import main.java.spaceinvaders.GameConstants;

public class Alien extends GameObject {
    private int direction = 1;
    private int speed;
    private int hitPoints;

    public Alien(int x, int y, int speed, int hitPoints) {
        super(x, y, GameConstants.ALIEN_WIDTH, GameConstants.ALIEN_HEIGHT);
        this.speed = speed;
        this.hitPoints = hitPoints;
    }

    public void update() {
        move(direction * speed, 0);
        if (x <= 0 || x + width >= GameConstants.WINDOW_WIDTH) {
            direction *= -1;
            move(0, GameConstants.ALIEN_HEIGHT); // descend a row
        }
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void damage() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    public int getSpeed() {
        return speed;
    }
}
