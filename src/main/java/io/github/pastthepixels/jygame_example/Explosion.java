package io.github.pastthepixels.jygame_example;

import java.awt.Color;

import com.github.jygame.Scene;
import com.github.jygame.Vector2;
import com.github.jygame.object.Mesh2D;
import com.github.jygame.sound.Sound;

public class Explosion {
    Vector2 origin = new Vector2();
    Scene scene;

    double rate = 0.001;
    double opacity = 255;

    public Sound sound = new Sound("src/main/resources/vineboom.wav");

    Mesh2D background = new Mesh2D() {
        double scale = 2;

        @Override
        public void onBeforeDraw() {
            this.position.set(origin);
            if(opacity > 0) this.fillColor = new Color(255, 255, 255, (int)opacity);
        }

        @Override
        public void updateGeometry() {
            this.geometry = (new GeometryBank()).createPolygon((new GeometryBank()).hexagon_geometry, this._enginePosition, scale);
        }
    };
    Mesh2D foreground = new Mesh2D() {
        double scale = 0.1;

        @Override
        public void onBeforeDraw() {
            rate += 0.01;
            scale += rate;
            opacity -= rate * 100;
            this.position.set(origin);
            if(opacity > 0) this.fillColor = new Color(255, 255, 255, (int)opacity);
        }

        @Override
        public void updateGeometry() {
            this.geometry = (new GeometryBank()).createPolygon((new GeometryBank()).hexagon_geometry, this._enginePosition, scale);
            if(scale >= 2) removeFromScene();
        }
    };

    public void addToScene(Scene scene) {
        background.zIndex = 1;
        foreground.zIndex = 2;
        scene.add(background);
        scene.add(foreground);
        this.scene = scene;
    }
    public void removeFromScene() {
        this.scene.remove(background);
        this.scene.remove(foreground);
    }
}
