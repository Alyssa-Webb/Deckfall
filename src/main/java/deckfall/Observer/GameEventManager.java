package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Entity.IntentType;

import java.util.ArrayList;
import java.util.List;

public class GameEventManager {
    private static final List<GameEventObserver> observers = new ArrayList<>();

    public static void registerObserver(GameEventObserver observer) {
        observers.add(observer);
    }

    public static void unregisterObserver(GameEventObserver observer) {
        observers.remove(observer);
    }

    // TODO
    // IN MVC, OBSERVER WILL BE THE BRIDGE BETWEEN THE MODEL AND CONTROLLER.
    // THIS WILL BE NECESSARY IN DISPLAYING INFORMATION TO THE CONSOLE
    // OR TO WHATEVER GUI WE CHOOSE.

    // Floor Events
    public void notifyFloorEntry(int floor) {
        for (GameEventObserver observer : observers) observer.onFloorEntry(floor);
    }

    public void notifyFloorClear(int floor) {
        for (GameEventObserver observer : observers) observer.onFloorClear(floor);
    }

    // Battle Events
    public void notifyBattleEntry() {
        for (GameEventObserver observer : observers) observer.onBattleEntry();
    }

    public void notifyBattleWin() {
        for (GameEventObserver observer : observers) observer.onBattleWin();
    }

    // Turn Events
    public void notifyTurnStart(String entityName) {
        for (GameEventObserver observer : observers) observer.onTurnStart(entityName);
    }

    public void notifyTurnEnd(String entityName) {
        for (GameEventObserver observer : observers) observer.onTurnEnd(entityName);
    }

    // Combat Events
    public void notifyEntityAttack(String attacker, String target, int damage) {
        for (GameEventObserver observer : observers) observer.onEntityAttack(attacker, target, damage);
    }

    public void notifyEntityDefense(String entityName, int blocked) {
        for (GameEventObserver observer : observers) observer.onEntityDefense(entityName, blocked);
    }

    // Card Events
    public void notifyCardDrawn(Card card) {
        for (GameEventObserver observer : observers) observer.onCardDrawn(card);
    }

    public void notifyCardPlayed(Card card) {
        for (GameEventObserver observer : observers) observer.onCardPlayed(card);
    }

    public void notifyDeckShuffled() {
        for (GameEventObserver observer : observers) observer.onDeckShuffled();
    }

    // Intent Events
    public void notifyDecideIntent(String enemyName, IntentType intent) {
        for (GameEventObserver observer : observers) observer.onDecideIntent(enemyName, intent);
    }

    // Demon King Events
    public void notifyDemonKingFloor() {
        for (GameEventObserver observer : observers) observer.onDemonKingFloor();
    }

    // Win/Loss Events
    public void notifyVictory() {
        for (GameEventObserver observer : observers) observer.onVictory();
    }

    public void notifyDefeat() {
        for (GameEventObserver observer : observers) observer.onDefeat();
    }
}
