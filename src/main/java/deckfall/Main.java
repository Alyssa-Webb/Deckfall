package deckfall;

import deckfall.Controller.GameController;
import deckfall.Entity.Slayer;
import deckfall.Game.Game;
import deckfall.Observer.ConsoleLogger;
import deckfall.Observer.GameEventObserver;
import deckfall.Observer.SwingGameView;
import deckfall.Tower.Difficulty;
import deckfall.Tower.Tower;
import deckfall.Tower.TowerBuilder;

public class Main {
    public static void main(String[] args) {
        GameEventObserver view = selectView(args);
        // Difficulty is set to Easy for now.
        Tower tower = TowerBuilder.buildTower(Difficulty.EASY);
        Game game = new Game(new Slayer(), tower);
        GameController controller = new GameController(game, view);
        controller.gameStart();
    }

    private static GameEventObserver selectView(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            return new ConsoleLogger();
        }
        return new SwingGameView();
    }
}