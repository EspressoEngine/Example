package io.github.pastthepixels.jygame_example;

import io.github.espressoengine.Scene;
import io.github.espressoengine.Vector2;
import io.github.espressoengine.object.Rect;
import io.github.espressoengine.object.Object;

import java.awt.Color;

public class ProgressBar {
    public double progress = 1;

    private Vector2 SIZE = new Vector2(75, 10);

    protected Object followedObject;

    protected Rect background = new Rect(SIZE) {
        @Override
        public void onBeforeDraw() {
            if(followedObject != null) position = followedObject.position.add(new Vector2(0, -50));
        }
    };
    
    protected Rect foreground = new Rect(SIZE) {
        @Override
        public void updateGeometry() {
            this._geometry.setRect(_enginePosition.x - (size.x - size.x*progress)/2, _enginePosition.y, size.x*progress, size.y);
            this.geometry = this._geometry;
        }

        @Override
        public void onBeforeDraw() {
            if(followedObject != null) position = followedObject.position.add(new Vector2(0, -50));
        }
    };
    
    public ProgressBar() {
        background.fillColor = Color.LIGHT_GRAY;
        foreground.fillColor = Color.RED;
        background.zIndex = 3;
        foreground.zIndex = 4;
        background.strokeColor = Color.WHITE;
        background.strokeWidth = 10;
    }

    public void setColor(Color color) {
        foreground.fillColor = color;
    }

    public void setColor(Color color, Color backgroundColor, Color strokeColor) {
        foreground.fillColor = color;
        background.fillColor = backgroundColor;
        background.strokeColor = strokeColor;
    }

    public void follow(Object object) {
        followedObject = object;
    }

    public void addToScene(Scene scene) {
        scene.add(background);
        scene.add(foreground);
    }

    public void removeFromScene(Scene scene) {
        scene.remove(background);
        scene.remove(foreground);
    }

}
