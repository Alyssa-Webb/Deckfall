package deckfall.Game;

import deckfall.DataClasses.*;
import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;
import deckfall.Observer.GameEventBus;
import deckfall.Tower.Battle;
import deckfall.Tower.Level;
import deckfall.Tower.Tower;

import java.util.List;

public class Game {
    private final Tower tower;
    private Level currentLevel;
    private Battle currentBattle;
    private final Slayer slayer;
    private Entity currentTurnHolder;
    private boolean towerDefeated;

    public Game(Slayer playerCharacter, Tower tower) {
        this.slayer = playerCharacter;
        this.tower = tower;
    }

    public void startGame() {
        currentLevel = tower.getNextLevel();
        currentBattle = currentLevel.getNextBattle();
        currentBattle.addPlayerCharacter(slayer, 0);
    }

    public String evalValidityOfMove(EntityAction action) {
        return (action.getAction_enum() == MoveTypes.USE_CARD) ? currentTurnHolder.evalMove(action.getSelectedCard(), action.getTarget()) : "";
    }

    public boolean makeMove(EntityAction action) {
        switch(action.getAction_enum()) {
            case PASS:
                currentTurnHolder.pass();
                return true;
            case USE_CARD:
                return ((Slayer) currentTurnHolder).playCard(action.getSelectedCard(), action.getTarget());
        };
        return false;
    }

    public GameState nextGameState(){
        if(isOver()){
            if(slayerWon()){
                return GameState.GAME_WIN;
            } else {
                return GameState.GAME_LOSS;
            }
        }

        if (currentBattle.battleOver()) {
            GameEventBus.getGameEventBus().notifyBattleWin();
            if (currentLevel.levelIsCleared()) {
                GameEventBus.getGameEventBus().notifyFloorClear(tower.getCurrentLevel());
                Level nextLevel = tower.getNextLevel();
                if (nextLevel == null) {
                    towerDefeated = true;
                    return GameState.GAME_WIN;
                }
                currentLevel = nextLevel;
                GameEventBus.getGameEventBus().notifyFloorEntry(tower.getCurrentLevel());
            }
            currentBattle = currentLevel.getNextBattle();
            GameEventBus.getGameEventBus().notifyBattleEntry();
            currentBattle.addPlayerCharacter(slayer);
        }

        currentTurnHolder = currentBattle.getNextTurn();
        if(currentTurnHolder.isSlayer()) {
            return GameState.PLAYER_TURN;
        } else {
            return GameState.ENEMY_TURN;
        }
    }

    private boolean slayerWon() {
        return slayer.isAlive();
    }

    public boolean isOver() {
        return !slayer.isAlive() || towerDefeated;
    }

    public List<String> getNotifications() {
        return GameEventBus.getGameEventBus().getEvents();
    }

    public RelevantGameData getRelevantGameData() {
        List<String> notifications = getNotifications();

        List<Entity> enemies = currentBattle.getActiveEnemies();

        return new RelevantGameData(
                slayer.getHand(),
                enemies,
                slayer,
                currentBattle,
                currentLevel.getTotalBattles(),
                currentLevel,
                tower.getTotalLevels(),
                notifications
        );
    }

    public void startSlayerTurn() {
        slayer.startTurn();
        currentBattle.peekNextEntity().decideIntent();
    }

    public void endSlayerTurn() {
        slayer.endTurn();
    }

    public void playEnemyTurn() {
        currentTurnHolder.executeIntent(slayer);
    }

    public boolean currentBattleOver() {
        return currentBattle.battleOver();
    }
}
