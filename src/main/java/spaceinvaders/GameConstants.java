package main.java.spaceinvaders;

public interface GameConstants {
    int WINDOW_WIDTH = 800;
    int WINDOW_HEIGHT = 600;

    int PLAYER_WIDTH = 50;
    int PLAYER_HEIGHT = 30;

    int ALIEN_WIDTH = 40;
    int ALIEN_HEIGHT = 30;

    int BULLET_WIDTH = 5;
    int BULLET_HEIGHT = 10;

    int PLAYER_SPEED = 5;
    int PLAYER_BULLET_SPEED = 8;

    int ENEMY_BULLET_SPEED = 4;

    int BASE_ALIEN_SPEED = 2;

    double ALIEN_SHOOT_CHANCE = 0.002; // probability per frame per alien

    int TIMER_DELAY = 16; // ~60 FPS

    int PLAYER_SHOOT_COOLDOWN_FRAMES = 10;
}
