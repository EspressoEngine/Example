package io.github.pastthepixels.jygame_example;

import java.util.ArrayList;

import com.github.jygame.object.*;
import com.github.jygame.physics.RigidBody;

public class Enemy extends Mesh2D {

    double velocity = 20; // Pixels/refresh

    Mesh2D player;

    RigidBody body;

    Game game;

    int frames_since_last_fire = 0;
    
    int frames_to_fire = 100;

    ArrayList<Laser> lasers = new ArrayList<Laser>();

    @Override
    public void updateGeometry() {
        this.geometry = (new GeometryBank()).createPolygon((new GeometryBank()).ship_geometry, this._enginePosition);
    }

    public Enemy(Mesh2D player, Game game) {
        this.player = player;
        this.game = game;
        // physics
        body = new RigidBody(this);
        body.disabled = true;
        body.mass = 0;
        game.entities.add(body);
        game.physics.add(body);
    }

    public void update() {
        frames_since_last_fire += 1;

        super.lookAt(player.position);
        rotation -= Math.toRadians(90);

        if(frames_since_last_fire == frames_to_fire) {
            frames_since_last_fire = 0;
            fire();
        }
    }

    public void fire() {
        Laser laser = new Laser(this, this.body, this.game);
        lasers.add(laser);
        game.root.add(laser.mesh);
    }
}
