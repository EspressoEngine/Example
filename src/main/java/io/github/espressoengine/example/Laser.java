package io.github.espressoengine.example;

import io.github.espressoengine.sound.Sound;
import io.github.espressoengine.object.*;
import io.github.espressoengine.physics.RigidBody;

import java.awt.Color;

public class Laser {
    Rect mesh = new Rect();

    RigidBody body;

    Game game;

    double velocity = 15; // Pixels/refresh

    double damage = 5;


    public Laser(Mesh2D sender, RigidBody senderBody, Game game, Sound preloaded_laser_sound) {
        this.game = game;
        mesh.zIndex = -1;
        mesh.fillColor = Color.RED;
        mesh.size.set(6, 32);
        mesh.position.set(sender.position);
        mesh.rotation = sender.rotation;
        // Physics
        body = new RigidBody(mesh) {
            @Override
            public void onBodyEntered(RigidBody body) { // <-- importan (sic)
                if(game.entities.indexOf(body) > -1 && body != senderBody) {
                    ((Entity)body.object).hurt(damage);
                    remove();
                }
            }
        };
        body.disabled = true;
        body.mass = 0;
        game.physics.add(body);
        preloaded_laser_sound.play();
    }

    public void remove() {
        game.physics.remove(body);
        game.root.remove(mesh);
        game.lasers.remove(this);
    }

    public void update() { // Uses the power of TRIG to move forward in the direction it is rotating
        mesh.position.set(mesh.position.add(velocity, mesh.rotation));
        //bounds
        if(mesh.position.y > game.physics.bounds.y || mesh.position.x > game.physics.bounds.x ) remove();
        if(mesh.position.y < 0                     || mesh.position.x < 0                     ) remove();
    }
}
