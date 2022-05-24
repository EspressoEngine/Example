package io.github.pastthepixels.jygame_example;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import com.github.jygame.*;
import com.github.jygame.object.*;
import com.github.jygame.physics.PhysicsEngine;
import com.github.jygame.physics.RigidBody;
import com.github.jygame.sound.*;

import java.util.ArrayList;

public class Game {
    // CONSTANTS (i am yelling)
    Vector2 DIMENSIONS = new Vector2(800, 800);

    // Class instances otherwise ungrouped
    GeometryBank geometry = new GeometryBank();
    Scene root = new Scene(new Window(DIMENSIONS, "JyGame Example"));
    PhysicsEngine physics = new PhysicsEngine();

    // Meshes
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    ArrayList<RigidBody> entities = new ArrayList<RigidBody>();

    Mesh2D player;
    RigidBody player_body;

    // Vars
    float HUE = 0;

    // Vars you can mess with
    double player_rotation_speed = 3; // Degrees/refresh (usually 60FPS=1/60Hz)
    double player_acceleration = 0.2; // Pixels/refresh^2
    double player_drag = 0.06; // Pixels/refresh^2
    double player_max_speed = 3; // Pixels/refresh

    // Initialization
    public void init() {
        root.setBackground(Color.black);
        initPhysics();
        createPlayer();
        bindKeys();
        Sound music = new Sound("src/main/resources/Half-Life03.wav"); // <-- We have to have wav files for some reason
        music.play();

        createEnemy();
    }

    public void initPhysics() {
        physics.bounds.set(DIMENSIONS);
    }

    public void createPlayer() {
        player = new Mesh2D() {
            @Override
            public void updateGeometry() {
                this.geometry = (new GeometryBank()).createPolygon((new GeometryBank()).ship_geometry, this._enginePosition);
            }
        };
        player.dbg = true;
        player.position.set(root.window.getSize().getWidth()/2, root.window.getSize().getHeight()/2);
        player.rotation = Math.toRadians(180);
        root.add(player);

        // Physis (sic)
        player_body = new RigidBody(player);
        player_body.disabled = true;
        player_body.mass = 0;
        entities.add(player_body);
        physics.add(player_body);
    }

    public void createEnemy() {
        Enemy enemy = new Enemy(player, this);
        enemy.dbg = true;
        enemy.position.set(100, 100);
        root.add(enemy);
        enemies.add(enemy);

        // Physics
        RigidBody enemy_body = new RigidBody(enemy);
        enemy_body.disabled = true;
        enemy_body.mass = 0;
        entities.add(enemy_body);
    }

    // Input
    ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
    public void bindKeys() {
        root.window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
    
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
                checkLaserInput(e);
                if(pressedKeys.contains(e.getKeyCode()) == false) {
                    pressedKeys.add(e.getKeyCode());
                }
            }
    
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("Key released code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
                if(pressedKeys.contains(e.getKeyCode())) {
                    pressedKeys.remove(pressedKeys.indexOf(e.getKeyCode()));
                }
            }
        });
    }

    // LASERS!!!
    public void checkLaserInput(KeyEvent e) {
        if(e.getKeyCode() == 32 && isKeyPressed(32) == false) { // Not yet in the array but pressed == just pressed spacebar
            Laser laser = new Laser(player, player_body, this);
            laser.mesh.fillColor = player.fillColor;
            lasers.add(laser);
            root.add(laser.mesh);
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public void startLoop() {
        while(true) {
            updatePlayerMovement();
            updateLaserMovement();
            updateEnemies();
 
            // Color
            if(HUE == 1) {
                HUE = 0;
            }
            HUE += 0.01;
            player.fillColor = Color.getHSBColor(HUE, 1.0f, 1.0f);

            // DRAWING/PHYSICS
            root.update();
            physics.nextFrame();

            // Waiting
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
            }
        }
    }

    // UPDATES!!
    public void updatePlayerMovement() {
        // Player physics
        if(isKeyPressed(38) == false && isKeyPressed(40) == false){
            if(player_body.velocity.y > player_drag) {
                player_body.velocity.y -= player_drag;
            } else if(player_body.velocity.y < -player_drag) {
                player_body.velocity.y += player_drag;
            } else {
                player_body.velocity.y = 0;
            }

            if(player_body.velocity.x > player_drag) {
                player_body.velocity.x -= player_drag;
            } else if(player_body.velocity.x < -player_drag) {
                player_body.velocity.x += player_drag;
            } else {
                player_body.velocity.x = 0;
            }
        }
        
        // Keybinds
        if (isKeyPressed(37)) { // LEFT
            player.rotation -= Math.toRadians(player_rotation_speed);
        }
        
        if (isKeyPressed(39)) { // RIGHT
            player.rotation += Math.toRadians(player_rotation_speed);
        }

        if (isKeyPressed(38)) { // UP (forward)
            player_body.velocity.y -= player_acceleration * Math.cos(player.rotation);
            player_body.velocity.x += player_acceleration * Math.sin(player.rotation);
        }

        if (isKeyPressed(40)) { // DOWN (backward)
            player_body.velocity.y += player_acceleration * Math.cos(player.rotation);
            player_body.velocity.x -= player_acceleration * Math.sin(player.rotation);
        }

        if(player_body.velocity.y > player_max_speed) player_body.velocity.y = player_max_speed;
        if(player_body.velocity.y < -player_max_speed) player_body.velocity.y = -player_max_speed;
        if(player_body.velocity.x > player_max_speed) player_body.velocity.x = player_max_speed;
        if(player_body.velocity.x < -player_max_speed) player_body.velocity.x = -player_max_speed;

        // Simple bounds
        if(player.position.y > physics.bounds.y) { player.position.y = physics.bounds.y; player_body.velocity.y = 0; }
        if(player.position.y < 0)                { player.position.y = 0;                player_body.velocity.y = 0; }
        if(player.position.x > physics.bounds.x) { player.position.x = physics.bounds.x; player_body.velocity.x = 0; }
        if(player.position.x < 0)                { player.position.x = 0;                player_body.velocity.x = 0; }
    }

    public void updateLaserMovement() {
        for(int i = 0; i < lasers.size(); i ++) {
            lasers.get(i).update();
        }
        for(int i = 0; i < enemies.size(); i ++) {
            for(int j = 0; j < enemies.get(i).lasers.size(); j ++) {
                enemies.get(i).lasers.get(j).update();
            }
        }
    }

    public void updateEnemies() {
        for(int i = 0; i < enemies.size(); i ++) {
            enemies.get(i).update();
        }
    }
}
