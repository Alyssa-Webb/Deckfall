package deckfall.MVC;

import deckfall.Controller.GameController;
import deckfall.Entity.Slayer;
import deckfall.Factory.TowerFactory;
import deckfall.Game.Game;
import deckfall.Observer.ConsoleLogger;
import deckfall.Tower.Difficulty;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MVCTest {
    @Test
    public void TestMVC(){
        System.setIn(new ByteArrayInputStream("\n6".getBytes()));
        ConsoleLogger view = new ConsoleLogger();
        Game game = new Game(new Slayer(), TowerFactory.createSmallTower(Difficulty.EASY));
        GameController controller = new GameController(game, view);
    }
}
