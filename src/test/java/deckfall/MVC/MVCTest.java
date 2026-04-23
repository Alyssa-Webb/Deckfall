package deckfall.MVC;

import deckfall.Controller.GameController;
import deckfall.Entity.Slayer;
import deckfall.Game.Game;
import deckfall.Observer.ConsoleLogger;
import deckfall.Tower.Tower;
import org.junit.jupiter.api.Test;

public class MVCTest {
    @Test
    void MVCTest() {
        // Simulate user choices:
        String simulatedInput = "4\n3\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleLogger view = new ConsoleLogger();
        Game game = new Game(new Slayer(), new Tower());
        GameController controller = new GameController(game, view);

        controller.gameStart();
    }
}
