package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Entity.IntentType;

import java.util.ArrayList;
import java.util.List;

public class GameEventBus {
    private static GameEventBus GAME_EVENT_BUS = null;
    private final List<GameEventObserver> observers = new ArrayList<>();
    private static List<String> EVENTS = new ArrayList<>();


    private GameEventBus() {}

    public static GameEventBus getGameEventBus() {
        if(GAME_EVENT_BUS == null){
            GAME_EVENT_BUS = new GameEventBus();
        }
        return GAME_EVENT_BUS;
    }

    public List<String> getEvents(){
        List<String> retList = EVENTS;
        EVENTS = new ArrayList<>();
        return retList;
    }

    public void registerObserver(GameEventObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(GameEventObserver observer) {
        observers.remove(observer);
    }

    public void notifyDefaultNotification(String message) {
        EVENTS.add(message);
        //for (GameEventObserver observer : observers) observer.defaultNotification(message);
    }

    // TODO
    // IN MVC, OBSERVER WILL BE THE BRIDGE BETWEEN THE MODEL AND CONTROLLER.
    // THIS WILL BE NECESSARY IN DISPLAYING INFORMATION TO THE CONSOLE
    // OR TO WHATEVER GUI WE CHOOSE.

    // Floor Events
    public void notifyFloorEntry(int floor) {
        EVENTS.add("Entered floor " + floor);
        //for (GameEventObserver observer : observers) observer.onFloorEntry(floor);
    }

    public void notifyFloorClear(int floor) {
        EVENTS.add("Cleared floor " + floor);
        //for (GameEventObserver observer : observers) observer.onFloorClear(floor);
    }

    // Battle Events
    public void notifyBattleEntry() {
        EVENTS.add("You entered battle!");
        //for (GameEventObserver observer : observers) observer.onBattleEntry();
    }

    public void notifyBattleWin() {
        EVENTS.add("You won!");
        //for (GameEventObserver observer : observers) observer.onBattleWin();
    }

    // Turn Events
    public void notifyTurnStart(String entityName) {
        EVENTS.add("It's your turn!");
        //for (GameEventObserver observer : observers) observer.onTurnStart(entityName);
    }

    public void notifyTurnEnd(String entityName) {
        EVENTS.add("Your turn is over");
        //for (GameEventObserver observer : observers) observer.onTurnEnd(entityName);
    }

    // Combat Events
    public void notifyEntityDamaged(String attacker, int damage) {
        EVENTS.add(attacker + " lost " + damage + " HP!");
        //for (GameEventObserver observer : observers) observer.onEntityDamaged(attacker, damage);
    }

    public void notifyEntityAttack(String attackerName, String targetName, int damageDealt) {
        EVENTS.add(attackerName + " attacks " + targetName + " for " + damageDealt);
        //for (GameEventObserver observer : observers) observer.onEntityAttack(attackerName, targetName, damageDealt);
    }

    public void notifyEntityDefense(String entityName, int blocked) {
        EVENTS.add(entityName + " is blocking! Blocked for *" + blocked + "* damage!");
        //for (GameEventObserver observer : observers) observer.onEntityDefense(entityName, blocked);
    }

    public void notifyEntityHeal(String entityName, int healed) {
        EVENTS.add(entityName + " healed " + healed + " HP.");
        //for (GameEventObserver observer : observers) observer.onEntityHeal(entityName, blocked);
    }

    public void notifyNotEnoughEnergy(String entityName, Card cardName) {
        EVENTS.add("You don't have enough mana to play " + cardName);
        //for (GameEventObserver observer : observers) observer.onNotEnoughEnergy(entityName, cardName);
    }

    // Card Events
    public void notifyCardDrawn(Card card) {
        EVENTS.add("You drew " + card.getName());
        //for (GameEventObserver observer : observers) observer.onCardDrawn(card);
    }

    public void notifyCardPlayed(Card card) {
        for (GameEventObserver observer : observers) observer.onCardPlayed(card);
    }

    public void notifyDeckShuffled() {
        EVENTS.add("Deck shuffled.");
        //for (GameEventObserver observer : observers) observer.onDeckShuffled();
    }

    // Intent Events
    public void notifyDecideIntent(String enemyName, IntentType intent) {
        EVENTS.add(enemyName + " decided to " + intent);
        //for (GameEventObserver observer : observers) observer.onDecideIntent(enemyName, intent);
    }

    // Demon King Events
    public void notifyDemonKingFloor() {
        EVENTS.add("""
                The trek has been long, and arduous. But finally, you're here.\
                
                You step through the large, imposing archway, and come face to face with THE DEMON KING.\
                
                Standing at 12 feet tall, you are just short of leaning backwards as you look up at their face.\
                
                They leer at you when your eyes meet. You draw your sword. One last battle.\
                """);
        //for (GameEventObserver observer : observers) observer.onDemonKingFloor();
    }

    // Win/Loss Events
    public void notifyVictory() {
        for (GameEventObserver observer : observers) observer.onVictory();
    }

    public void notifyDefeat() {
        for (GameEventObserver observer : observers) observer.onDefeat();
    }
}
