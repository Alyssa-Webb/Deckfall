package deckfall;

import deckfall.Controller.GameController;
import deckfall.Entity.Slayer;
import deckfall.Game.Game;
import deckfall.Observer.ConsoleLogger;
import deckfall.Factory.TowerFactory;
import deckfall.Tower.Difficulty;
import deckfall.Tower.Tower;

public class Main {
    public static void main(String[] args) {
        ConsoleLogger view = new ConsoleLogger();
        //SwingGameView view = new SwingGameView();
        // Tower tower = TowerFactory.createStandardTower(Difficulty.EASY);
        Tower tower = TowerFactory.createSmallTower(Difficulty.EASY);

        Game game = new Game(new Slayer(), tower);
        GameController controller = new GameController(game, view);
        controller.gameStart();
    }
}