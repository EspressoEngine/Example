package com.github.jygame;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.*;

import com.github.jygame.object.*;
import com.github.jygame.physics.PhysicsEngine;
import com.github.jygame.physics.RigidBody;
import com.github.jygame.sound.*;

public class Example {
    public static void main(String[] args) {
        float ROTATION = 0;
        float HUE = 0;

        Scene root = new Scene();
        root.setBackground(Color.black);

        //
        // BACKGROUND
        //
        Mesh2D customMesh = new Mesh2D() {
            @Override
            public void updateGeometry() {
                this.geometry = new RoundRectangle2D.Double(this._enginePosition.x, this._enginePosition.y, 256, 256, 32, 32);
            }
        }; // <-- Showcases meshes with custom geometry from java.awt.geom.*
        customMesh.dbg = true;
        customMesh.position.set(212, 328);
        customMesh.rotation = Math.toRadians(10);

        root.add(customMesh);

        
        Mesh2D customMesh2 = new Mesh2D() {
            @Override
            public void updateGeometry() {
                Polygon polygon = new Polygon();
                polygon.addPoint(0+(int)this._enginePosition.x, 0+(int)this._enginePosition.y);
                polygon.addPoint(50+(int)this._enginePosition.x, 20+(int)this._enginePosition.y);
                polygon.addPoint(100+(int)this._enginePosition.x, 0+(int)this._enginePosition.y);
                polygon.addPoint(50+(int)this._enginePosition.x, 100+(int)this._enginePosition.y);
                this.geometry = polygon;
            }
        }; // <-- Showcases meshes with ACTUAL custom geometry (a.k.a a funky polygon) from java.awt.Polygon
        customMesh2.dbg = true;
        customMesh2.position.set(100, 100);
        root.add(customMesh2);

        Circle circle = new Circle(); // <-- Showcases built-in support for circles/ovals
        circle.dbg = true;
        circle.position.set(150, 0);
        circle.setRadius(64);
        root.add(circle);

        Circle circle2 = new Circle(); // <-- Showcases built-in support for circles/ovals
        circle2.position.set(450, 500);
        circle2.setRadius(64);
        circle2.fillColor = Color.GREEN;
        root.add(circle2);

        //
        // RECTANGLE
        //
        Rect rectangle = new Rect(); // <-- Showcases built-in support for rectangles/squares
        rectangle.position.set(500, 300);
        rectangle.strokeWidth = 16;
        rectangle.zIndex = 2;
        rectangle.fillColor = null;
        root.add(rectangle);

        //
        // SPRITE
        //
        Sprite2D sprite = new Sprite2D("src/main/resources/player.png"); // <-- Showcases support for images as sprites
        sprite.dbg = true;
        sprite.position.set(400, 700);
        root.add(sprite);

        //
        // LABEL
        //
        Label label = new Label(); // <-- Showcases support for text
        label.text = "infiniteshooter";
        label.position.set(300, 670);
        label.zIndex = 0;
        root.add(label);

        //
        // MUSIC
        //
        Sound music = new Sound("src/main/resources/audio.wav"); // <-- Showcases support for images as sprites
        music.play();
        

        // Z-INDEX
        root.sortZ();

        // Physics
        PhysicsEngine engine = new PhysicsEngine();
        engine.bounds.set(600, 800);

        RigidBody circleBody = new RigidBody(circle);
        engine.add(circleBody);

        RigidBody customBody = new RigidBody(customMesh);
        customBody.mass = 2;
        engine.add(customBody);

        RigidBody spriteBody = new RigidBody(sprite) {
            public void onBodyEntered() {
                circle2.fillColor = Color.RED;
            }
            public void onBodyExited() {
                circle2.fillColor = Color.GREEN;
            }
        };
        spriteBody.mass = 0;
        spriteBody.dbg = true;
        spriteBody.disabled = true;
        engine.add(spriteBody);

        double positionXSpeed = 0.4;
        while(true) {
            sprite.position.x += positionXSpeed;
            if(sprite.position.x <= 380) positionXSpeed = 0.4;
            if(sprite.position.x >= 500) positionXSpeed = -0.4;

            // Color
            if(HUE == 1) {
                HUE = 0;
            }
            HUE += 0.01;
            rectangle.strokeColor = Color.getHSBColor(HUE, 1.0f, 1.0f);
            Color bgColor = Color.getHSBColor(HUE, 0.2f, 0.2f);
            circle.fillColor = bgColor;
            customMesh.fillColor = bgColor;
            customMesh2.fillColor = bgColor;
            root.window.canvas.drawAll();

            // PHYSICS
            engine.nextFrame();

            ROTATION += 1;
            customMesh2.rotation = Math.toRadians(ROTATION);
            sprite.rotation = Math.toRadians(ROTATION);

            // Waiting
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
            }
        }
    }
}
