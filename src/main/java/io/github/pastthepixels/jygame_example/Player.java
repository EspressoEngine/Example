package io.github.pastthepixels.jygame_example;

import io.github.espressoengine.physics.RigidBody;

import java.awt.Color;
import java.util.concurrent.Executors;

public class Player extends Entity {
    GeometryBank geomBank = new GeometryBank();
    RigidBody body = new RigidBody(this);
    Game game;

    // Vars
    float HUE = 0;
    boolean disco = true;

    // Movement vars
    double rotation_speed = 4; // Degrees/refresh (usually 60FPS=1/60Hz)
    double acceleration = 0.5; // Pixels/refresh^2
    double drag = 0.06; // Pixels/refresh^2
    double max_speed = 4; // Pixels/refresh

    public Player(Game game) {
        this.game = game;
        body.disabled = true;
        body.mass = 0;
        // healthbar
        health_bar.addToScene(game.root);
        health_bar.follow(this);
    }

    @Override
    public void updateGeometry() {
        this.geometry = geomBank.createPolygon(geomBank.ship_geometry, this._enginePosition);
    }

    @Override
    public void die() {
        visible = false;
        // Creates an explosion
        Explosion explosion = new Explosion();
        explosion.origin.set(position);
        explosion.addToScene(game.root);
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                for(int i = 0; i < 3; i++){
                    explosion.sound.play();
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                System.out.println("Something went wrong while playing your vine booms.");
            }
        });
        // Removes the ship
        game.physics.remove(body);
        game.entities.remove(body);
        game.root.remove(this);
        health_bar.removeFromScene(game.root);
        onDie(); 
    }

    public void updateColor() {
        if(disco == true) {
            if(HUE == 1) {
                HUE = 0;
            }
            HUE += 0.01;
            strokeColor = Color.getHSBColor(HUE, 1.0f, 1.0f);
            strokeWidth = 6;
            fillColor = Color.DARK_GRAY;
            health_bar.setColor(strokeColor, Color.DARK_GRAY, Color.getHSBColor(HUE, 1.0f, 0.5f));
        } else {
            fillColor = Color.decode("#3584e4");
            health_bar.setColor(fillColor);
        }
    }

    public void onDie() { // Override this function in custom instances

    }

}
