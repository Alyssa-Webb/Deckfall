package deckfall.Game;

import deckfall.Action.Action;
import deckfall.Entity.Entity;
import deckfall.Tower.Level;
import deckfall.Tower.Tower;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameState gameState;
    private final Tower tower;
    private Level currentLevel;
    private List<Entity> entities;
    private Entity player;
    private Entity currentTurnHolder;

    public Game(Entity playerCharacter, Tower tower) {
        this.tower = tower;
        gameState = GameState.GAME_START;
    }

    public Action play(Action action) {
        action.execute();

        evalNextGameState();

        //TODO: yeah change that
        return action;
    }

    public void evalNextGameState(){
        //
    }

    public GameState getGameState(){
        return gameState;
    }

    public Entity getCurrentTurnHolder() {
        return currentTurnHolder;
    }
}
