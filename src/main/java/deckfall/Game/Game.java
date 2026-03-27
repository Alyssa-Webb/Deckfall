package deckfall.Game;

import deckfall.DataClasses.*;
import deckfall.Entity.Entity;
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
    private final Entity player;
    private Entity currentTurnHolder;
    private int numTurns = 0;
    private final LinkedList<String> events = new LinkedList<>();

    public Game(Entity playerCharacter, Tower tower) {
        this.player = playerCharacter;
        this.tower = tower;
    }

    public void play() {
        //aaaaaaaaaaaaa?
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

    /*public Action makeActionableMove(EntityAction action){
        return switch(action.getAction_enum()){
            case PASS -> ""
        }
    }*/

    //public GameState evalNextGameState(EntityAction action, GameState gameState){
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

        /* Actually, I don't think I should be passing any arguments. ... Except, the user can do things that don't progress the game, ugh.
        OH WAIT
        That can just be left to the GameController. Maybe it doesn't call evalNextGameState() if the user chose to check a card/entity
        switch(gameState) {
            case GAME_OVER:
                return GameState.GAME_OVER;
            case LEVEL_END:
                return GameState.LEVEL_START;
            case LEVEL_START, BATTLE_END:
                return GameState.BATTLE_START;
            case BATTLE_START:
                return GameState.PLAYER_TURN;
        }

        if (currentBattle.battleOver()) {
            return GameState.BATTLE_END;
        }
        if(action.getAction_enum() == MoveTypes.USE_CARD){
            numTurns += 1;
        }*/
        events.add("Events will eventually be changed to be of type Side Effect. I think?");
        //TODO: make non-trivial
        return GameState.PLAYER_TURN;
    }

    private boolean slayerWon() {
        return player.isAlive();
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
        events.pop();
        return new SideEffect(SideEffectType.ENEMY_DEATH, "Enemy");
    }
}
