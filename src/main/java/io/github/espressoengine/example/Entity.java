package io.github.espressoengine.example;

import io.github.espressoengine.object.Mesh2D;

public class Entity extends Mesh2D {
    // Game mechanics
    double health = 100;
    double max_health = 100;

    // Progress bar
    ProgressBar health_bar = new ProgressBar();

    public void hurt(double amount) {
        health -= amount;
        if(health <= 0) die();
        if(health_bar != null) health_bar.progress = health/max_health;
    }

    public void die() {
        
    }

    public String toString() {
        return String.format(super.toString() + " health=%.2f, max_health=%.2f", health, max_health);
    }

    public void setMaxHealth(double hp) {
        max_health  = hp;
        health      = hp;
    }
}
