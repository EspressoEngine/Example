package io.github.espressoengine.example;

import java.awt.Color;

import io.github.espressoengine.*;
import io.github.espressoengine.input.KeyHandler;
import io.github.espressoengine.object.*;
import io.github.espressoengine.physics.PhysicsEngine;
import io.github.espressoengine.physics.RigidBody;
import io.github.espressoengine.sound.*;

import java.util.ArrayList;

public class Game {
    // CONSTANTS (i am yelling)
    Vector2 DIMENSIONS = new Vector2(800, 800);

    // INPUT VARS
    int frames_since_last_fire = 0;
    int laser_fire_interval = 16; // Measured in frames.
    
    // Class instances otherwise ungrouped
    Scene root = new Scene(new Window(DIMENSIONS, "Espresso Example")) {
        @Override
        public void process(double delta) {
            updateLaserMovement();
            if(player.health > 0) updatePlayer();
            updateEnemies();

            // PHYSICS
            physics.nextFrame();
        }
    };
    PhysicsEngine physics = new PhysicsEngine();
    KeyHandler keys = new KeyHandler(root.window);
    Label label;

    // Meshes
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<RigidBody> entities = new ArrayList<RigidBody>();

    Player player;

    // Vars you cannot mess with
    int score = 0;

    // Initialization
    public void init() {
        root.setBackground(Color.black);
        physics.bounds.set(DIMENSIONS);
        createPlayer();
        createLabel();
        initMusic();
        createEnemy();
    }

    public void initMusic() {
        MIDIPlayer midi_player = new MIDIPlayer("src/main/resources/soundtrack.mid");
        midi_player.setSoundFont("src/main/resources/gzdoom.sf2");
        midi_player.loopInfinitely();
        midi_player.play();
    }

    public void createPlayer() {
        player = new Player(this) {
            @Override
            public void onDie() {
                gameOver();
            }
        };
        player.position.set(root.window.getSize().getWidth()/2, root.window.getSize().getHeight()/2);
        player.rotation = Math.toRadians(180); // So the player is facing UP
        root.add(player);

        // Physis (sic)
        entities.add(player.body);
        physics.add(player.body);
    }

    public void createLabel() { // Creates a label that holds scores
        label = new Label();
        label.text = "SCORE: 0";
        label.position.set(10, 10);
        label.zIndex = 100;
        root.add(label);
    }

    public void gameOver() { // Creates a label that lets you know when you died
        Label label = new Label();
        label.color = Color.RED;
        label.text = "You died :(";
        label.position.set(10, 50);
        label.zIndex = 100;
        root.add(label);
    }


    public void createEnemy() {
        Enemy enemy = new Enemy(this) {
            @Override
            public void onDie() {
                score ++;
                label.text = String.format("SCORE: %d", score);
                createEnemy();
            }
        };
        // Enemies are either positioned on the sides OR the top. Not both -- because that means getting a random position ANYWHERE on the screen.
        if(Utils.flip_coin() == true) {
            enemy.position.x = physics.bounds.x * Utils.rand_range(0, 1);
            enemy.position.y = Utils.flip_coin() == true? -100 : physics.bounds.y + 100;
        } else {
            enemy.position.x = Utils.flip_coin() == true? -100 : physics.bounds.x + 100;
            enemy.position.y = physics.bounds.y * Utils.rand_range(0, 1);
        }
        root.add(enemy);
        enemies.add(enemy);

        // Physics (we're just using physics for collision detection)
        RigidBody enemy_body = new RigidBody(enemy);
        enemy_body.disabled = true;
        enemy_body.mass = 0;
        entities.add(enemy_body);
    }

    // LASERS!!!
    public void checkLaserInput() {
        if(keys.isKeyPressed(32) == true) {
            if(frames_since_last_fire == 0) { // Fires a laser when the counter is at 0 so that tapping the spacebar to fire lasers works.
                Laser laser = new Laser(player, player.body, this, player.laser_sound);
                laser.mesh.fillColor = player.fillColor;
                laser.mesh.strokeColor = player.strokeColor;
                laser.mesh.strokeWidth = player.strokeWidth;
                lasers.add(laser);
                root.add(laser.mesh);
            }
            frames_since_last_fire += 1; // but still counts up on the chance that the spacebar is held.
            if(frames_since_last_fire == laser_fire_interval) frames_since_last_fire = 0; // If the counter reaches the time set in the interval, reset the counter.
        } else { // It's that simple!
            frames_since_last_fire = 0;
        }
    }

    // UPDATES!!
    public void updatePlayer() {
        // Disco
        player.updateColor();

        // Lasers
        checkLaserInput();

        // Player physics
        if(keys.getAxis(38, 40) == 0){
            player.body.velocity.y = Utils.stepTo(player.body.velocity.y, 0, player.drag);
            player.body.velocity.x = Utils.stepTo(player.body.velocity.x, 0, player.drag);
        }

        // LEFT/RIGHT
        player.rotation += Math.toRadians(player.rotation_speed) * keys.getAxis(37, 39);

        // UP/DOWN (code golf edition)
        player.body.velocity.set(player.body.velocity.add(player.acceleration * keys.getAxis(38, 40), player.rotation).clamp(-player.max_speed, player.max_speed));

        // Simple bounds
        Vector2 clampedPosition = player.position.clamp(new Vector2(), physics.bounds);
        if(player.position.x != clampedPosition.x) player.body.velocity.x = 0;
        if(player.position.y != clampedPosition.y) player.body.velocity.y = 0;
        player.position.set(clampedPosition);
    }

    public void updateLaserMovement() {
        for(int i = 0; i < lasers.size(); i ++) {
            lasers.get(i).update();
        }
    }

    public void updateEnemies() {
        for(int i = 0; i < enemies.size(); i ++) {
            enemies.get(i).update();
        }
    }
}
