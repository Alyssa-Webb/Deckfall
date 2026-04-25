package deckfall.Game;

import deckfall.DataClasses.*;
import deckfall.Entity.Enemy;
import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;
import deckfall.Tower.Battle;
import deckfall.Tower.Level;
import deckfall.Tower.Tower;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
    private final Tower tower;
    private Level currentLevel;
    private Battle currentBattle;
    private List<Entity> entities;
    private final Slayer slayer;
    private Entity currentTurnHolder;
    private int numTurns = 0;
    //private final LinkedList<String> events = new LinkedList<>();

    public Game(Slayer playerCharacter, Tower tower) {
        this.slayer = playerCharacter;
        this.tower = tower;
    }

    //a little risky in the event there IS no next battle or level, but that's. fine.
    public void startGame() {
        currentLevel = tower.getNextLevel();
        currentBattle = currentLevel.getNextBattle();
        currentBattle.addPlayerCharacter(slayer, 0);
        currentTurnHolder = currentBattle.getNextTurn();
    }

    // if it's valid, then it returns an empty string. Otherwise, it returns the reason why the move is invalid.
    public String evalValidityOfMove(EntityAction action) {
        return (action.getAction_enum() == MoveTypes.USE_CARD) ? currentTurnHolder.evalMove(action.getSelectedCard(), action.getTarget()) : "";
    }

    public void makeMove(EntityAction action) {
        currentTurnHolder.pass();
        /*return switch(action.getAction_enum()) {
            case PASS -> currentTurnHolder.pass();
            case USE_CARD -> currentTurnHolder.useCard(action.getSelectedCard(), action.getTarget());
            case GET_CARD_INFO -> currentTurnHolder.getCardInfo(action.getSelectedCard());
            case GET_CARD_DESCRIPTION -> currentTurnHolder.getCardDescription(action.getSelectedCard());
            case GET_ENTITY_INFO -> action.getTarget().getEntityInfo();
            case GET_ENTITY_DESCRIPTION -> action.getTarget().getEntityDescription();
        };*/
    }

    public GameState nextGameState(){
        /*//this will be in charge of notifying when a battle and a floor is beaten, alongside damage and death
        if(!events.isEmpty()) {
            return GameState.NOTIFYING_OF_SIDE_EFFECTS;
        }*/

        if(isOver()){
            if(slayerWon()){
                return GameState.GAME_WIN;
            } else {
                return GameState.GAME_LOSS;
            }
        }

        if(currentBattle.battleOver()) {
            //if the tower is cleared, that should be caught by the previous Game Over check
            if(currentLevel.levelIsCleared()) {
                currentLevel = tower.getNextLevel();
            }
            currentBattle = currentLevel.getNextBattle();
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
        return !slayer.isAlive() || tower.isCleared();
    }

    public List<String> getNotifications() {
        List<String> notifications = new ArrayList<>();
        for(Entity entity : currentBattle) {
            notifications.addAll(entity.getNotifications());
        }
        return notifications;
    }

    public RelevantGameData getRelevantGameData() {
        //to make this more like the Observer pattern, we could switch to Events. But since we're using the Iterator pattern
        // as well now I'm not sure it's necessary to have Observer specifically
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
    }
    public void endSlayerTurn() {
        slayer.endTurn();
    }

    public void playEnemyTurn() {
        ((Enemy) currentTurnHolder).decideIntent();
        ((Enemy) currentTurnHolder).executeIntent(slayer);
    }
}
