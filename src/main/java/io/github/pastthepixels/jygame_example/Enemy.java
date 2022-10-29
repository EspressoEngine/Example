package io.github.pastthepixels.jygame_example;

import io.github.espressoengine.physics.RigidBody;

public class Enemy extends Entity {

    double velocity = 1; // Pixels/refresh


    RigidBody body;

    Game game;

    int frames_since_last_fire = 0; // Please don't mess with this
    
    int frames_to_fire = 100; // Frames until a laser is fired.

    @Override
    public void updateGeometry() {
        this.geometry = (new GeometryBank()).createPolygon((new GeometryBank()).ship_geometry, this._enginePosition);
    }

    public Enemy(Game game) {
        this.game = game;
        // physics
        body = new RigidBody(this);
        body.disabled = true;
        body.mass = 0;
        game.entities.add(body);
        game.physics.add(body);
        // health
        super.setMaxHealth(20);
        // healthbar
        health_bar.addToScene(game.root);
        health_bar.follow(this);
    }

    public void update() {
        if(health <= 0) return;
        frames_since_last_fire += 1;

        // Look, then move
        if(game.player.health > 0) {
            super.lookAt(game.player.position);
            rotation -= Math.toRadians(90);
        }

        move();

        if(frames_since_last_fire == frames_to_fire) {
            frames_since_last_fire = 0;
            fire();
        }
    }

    public void move() {
        position.y += velocity * Math.cos(rotation);
        position.x -= velocity * Math.sin(rotation);
    }

    public void fire() {
        Laser laser = new Laser(this, this.body, this.game);
        laser.mesh.fillColor = fillColor;
        game.lasers.add(laser);
        game.root.add(laser.mesh);
    }

    @Override
    public void die() {
        visible = false;
        // Creates an explosion
        Explosion explosion = new Explosion();
        explosion.origin.set(position);
        explosion.addToScene(game.root);
        explosion.sound.play();
        // Removes the ship
        game.physics.remove(body);
        game.entities.remove(body);
        game.root.remove(this);
        game.enemies.remove(this);
        health_bar.removeFromScene(game.root);
        onDie();
    }

    public void onDie() { // Override this function in custom instances

    }
}
