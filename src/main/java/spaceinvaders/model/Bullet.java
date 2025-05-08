package main.java.spaceinvaders.model;

import main.java.spaceinvaders.GameConstants;

public class Bullet extends GameObject {

    private final int dy;

    public Bullet(int x, int y, int dy) {
        super(x, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
        this.dy = dy;
    }

    public void update() {
        move(0, dy);
    }

    public boolean isOffScreen() {
        return y + height < 0 || y > GameConstants.WINDOW_HEIGHT;
    }
}
