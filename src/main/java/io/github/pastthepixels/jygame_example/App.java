package io.github.pastthepixels.jygame_example;

public class App {
    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        game.root.startLoop();
    }
}
