package deckfall.Controller;

import deckfall.Game.Game;
import deckfall.Observer.GameEventManager;
import deckfall.Observer.GameEventObserver;

import java.util.Observer;

public class GameController {
    private Game game;
    private GameEventManager manager;

    public GameController(Game game, GameEventManager manager) {
        this.game = game;
        this.manager = manager;
    }
}
