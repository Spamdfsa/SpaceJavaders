package main.java.spaceinvaders;

import main.java.spaceinvaders.model.*;
import main.java.spaceinvaders.util.CollisionDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private PlayerShip player;
    private final List<Alien> aliens;
    private final List<Bullet> playerBullets;
    private final List<Bullet> enemyBullets;
    private final Timer timer;
    private final Random random = new Random();

    private boolean leftPressed, rightPressed, shootPressed;
    private int shootCooldown = 0;
    private int waveNumber = 1;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new PlayerShip(GameConstants.WINDOW_WIDTH / 2 - GameConstants.PLAYER_WIDTH / 2,
                GameConstants.WINDOW_HEIGHT - 60);

        aliens = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();

        spawnAliens(waveNumber);

        timer = new Timer(GameConstants.TIMER_DELAY, this);
        timer.start();
    }

    private void spawnAliens(int wave) {
        aliens.clear();
        int rows = 3 + wave;      // increases each wave
        int cols = 8;
        int hGap = 20;
        int vGap = 20;
        int startX = 50;
        int startY = 50;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (GameConstants.ALIEN_WIDTH + hGap);
                int y = startY + row * (GameConstants.ALIEN_HEIGHT + vGap);

                // Different enemy types based on row
                int speed;
                int hp;
                if (row % 3 == 0) {          // fast alien
                    speed = GameConstants.BASE_ALIEN_SPEED + wave;
                    hp = 1;
                } else if (row % 3 == 1) {   // armored alien
                    speed = GameConstants.BASE_ALIEN_SPEED + wave - 1;
                    hp = 2 + wave / 2;
                } else {                     // standard
                    speed = GameConstants.BASE_ALIEN_SPEED + wave - 1;
                    hp = 1 + wave / 3;
                }
                aliens.add(new Alien(x, y, speed, hp));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        g.setColor(Color.GREEN);
        g.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Draw player bullets
        g.setColor(Color.WHITE);
        for (Bullet b : playerBullets) {
            g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }

        // Draw aliens
        for (Alien alien : aliens) {
            // color by hit points
            if (alien.getHitPoints() > 2) g.setColor(Color.ORANGE);
            else g.setColor(Color.RED);
            g.fillRect(alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight());
        }

        // Draw enemy bullets
        g.setColor(Color.CYAN);
        for (Bullet b : enemyBullets) {
            g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }

        g.setColor(Color.WHITE);
        g.drawString("Wave: " + waveNumber, 10, 15);
        g.drawString("Aliens remaining: " + aliens.size(), 10, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        // Player movement
        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();

        // Shooting logic
        if (shootCooldown > 0) shootCooldown--;
        if (shootPressed && shootCooldown == 0) {
            firePlayerBullet();
            shootCooldown = GameConstants.PLAYER_SHOOT_COOLDOWN_FRAMES;
        }

        // Update aliens
        for (Alien alien : new ArrayList<>(aliens)) {
            alien.update();
            if (alien.getY() + alien.getHeight() >= player.getY()) {
                endGame("Game Over! Aliens reached you.");
                return;
            }
            // Alien shooting
            double chance = GameConstants.ALIEN_SHOOT_CHANCE * waveNumber;
            if (random.nextDouble() < chance) {
                fireEnemyBullet(alien);
            }
        }

        // Update bullets
        updateBullets(playerBullets, true);
        updateBullets(enemyBullets, false);

        // Check collisions: player bullets vs aliens
        Iterator<Bullet> pIter = playerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet b = pIter.next();
            Iterator<Alien> aIter = aliens.iterator();
            while (aIter.hasNext()) {
                Alien alien = aIter.next();
                if (CollisionDetector.areColliding(b, alien)) {
                    alien.damage();
                    pIter.remove();
                    if (alien.isDestroyed()) aIter.remove();
                    break;
                }
            }
        }

        // Check collisions: enemy bullets vs player
        for (Bullet b : enemyBullets) {
            if (CollisionDetector.areColliding(b, player)) {
                endGame("Game Over! You were hit.");
                return;
            }
        }

        // Next wave
        if (aliens.isEmpty()) {
            waveNumber++;
            spawnAliens(waveNumber);
        }
    }

    private void updateBullets(List<Bullet> bullets, boolean upward) {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet b = iterator.next();
            b.update();
            if (b.isOffScreen()) iterator.remove();
        }
    }

    private void firePlayerBullet() {
        int bulletX = player.getX() + player.getWidth() / 2 - GameConstants.BULLET_WIDTH / 2;
        int bulletY = player.getY() - GameConstants.BULLET_HEIGHT;
        playerBullets.add(new Bullet(bulletX, bulletY, -GameConstants.PLAYER_BULLET_SPEED));
    }

    private void fireEnemyBullet(Alien alien) {
        int bulletX = alien.getX() + alien.getWidth() / 2 - GameConstants.BULLET_WIDTH / 2;
        int bulletY = alien.getY() + alien.getHeight();
        enemyBullets.add(new Bullet(bulletX, bulletY, GameConstants.ENEMY_BULLET_SPEED));
    }

    private void endGame(String message) {
        timer.stop();
        JOptionPane.showMessageDialog(this, message);
        System.exit(0);
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
            case KeyEvent.VK_SPACE -> shootPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
            case KeyEvent.VK_SPACE -> shootPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
