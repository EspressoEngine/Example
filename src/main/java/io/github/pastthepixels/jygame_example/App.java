package io.github.pastthepixels.jygame_example;

public class App {
    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        game.startLoop();
/*
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
        Sprite2D sprite = new Sprite2D("src/main/resources/Scientist_01.jpeg"); // <-- Showcases support for images as sprites
        sprite.dbg = true;
        sprite.position.set(400, 700);
        root.add(sprite);

        //
        // LABEL
        //
        Label label = new Label(); // <-- Showcases support for text
        label.text = "do you know who ate all the doughnuts?";
        label.position.set(300, 670);
        label.zIndex = 0;
        root.add(label);


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

        double positionXSpeed = 0.4;*/

    }
}
