package deckfall.Game;

import deckfall.DataClasses.*;
import deckfall.Entity.Enemy;
import deckfall.Entity.Entity;
import deckfall.Entity.Goblin;
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
    //storing the 'player' allows me to add them to the Battle when it starts, rather than having to *build* each Battle with the player
    private final Slayer slayer;
    private Entity currentTurnHolder;
    private int numTurns = 0;
    private final LinkedList<String> events = new LinkedList<>();

    public Game(Slayer playerCharacter, Tower tower) {
        this.slayer = playerCharacter;
        this.tower = tower;
        //remove this later
        currentTurnHolder = slayer;
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
        //this will be in charge of notifying when a battle and a floor is beaten, alongside damage and death
        if(!events.isEmpty()) {
            return GameState.NOTIFYING_OF_SIDE_EFFECTS;
        }

        if(isOver()){
            if(slayerWon()){
                return GameState.GAME_WIN;
            } else {
                return GameState.GAME_LOSS;
            }
        }


        events.add("Events will eventually be changed to be of type SideEffect. I think?");
        //TODO: make non-trivial
        return GameState.PLAYER_TURN;
    }

    private boolean slayerWon() {
        return slayer.isAlive();
    }

    public boolean isOver() {
        numTurns += 1;
        return numTurns == 2;
        //return player.isAlive() || tower.isCleared();
    }

    public Entity getCurrentTurnHolder() {
        return currentTurnHolder;
    }

    public SideEffect getSideEffect() {
        //TODO: make this non-trivial
        events.pop();
        return new SideEffect(SideEffectType.ENEMY_DEATH, "Enemy");
    }

    public RelevantGameData getRelevantGameData() {
        List<Entity> enemies = List.of(new Goblin(), new Goblin());
        // Pass a string of notifications i.e. "pass" "use card" etc.
        List<String> notifications = new ArrayList<>(events);

        currentLevel = tower.getCurrentLevel();
        currentBattle = currentLevel.getCurrentBattle();

        events.clear();

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
}
