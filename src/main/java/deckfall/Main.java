package deckfall;

import deckfall.Controller.GameController;
import deckfall.Entity.Slayer;
import deckfall.Game.Game;
import deckfall.Observer.ConsoleLogger;
import deckfall.Observer.SwingGameView;
import deckfall.Tower.Tower;

public class Main {
    public static void main(String[] args) {
        //ConsoleLogger view = new ConsoleLogger();
        SwingGameView view = new SwingGameView();
        Game game = new Game(new Slayer(), new Tower());
        GameController controller = new GameController(game, view);
        controller.gameStart();
    }
}