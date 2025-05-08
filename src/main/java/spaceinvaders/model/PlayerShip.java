package main.java.spaceinvaders.model;

import main.java.spaceinvaders.GameConstants;

public class PlayerShip extends GameObject {
    public PlayerShip(int x, int y) {
        super(x, y, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
    }

    public void moveLeft() {
        if (x - GameConstants.PLAYER_SPEED >= 0) {
            move(-GameConstants.PLAYER_SPEED, 0);
        }
    }

    public void moveRight() {
        if (x + GameConstants.PLAYER_SPEED + width <= GameConstants.WINDOW_WIDTH) {
            move(GameConstants.PLAYER_SPEED, 0);
        }
    }
}
