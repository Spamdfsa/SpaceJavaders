package main.java.spaceinvaders.util;

import main.java.spaceinvaders.model.GameObject;

public class CollisionDetector {

    public static boolean areColliding(GameObject a, GameObject b) {
        return a.getBounds().intersects(b.getBounds());
    }
}
