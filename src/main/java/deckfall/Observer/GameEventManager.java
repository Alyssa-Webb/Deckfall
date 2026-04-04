package deckfall.Observer;

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



    // Notify Observers

    // Floor Events
    public static void notifyNewFloorEntry (int floor) {}
    public static void notifyNewFloorClear (int floor) {}

    // Death Events
    public static void notifySlayerDefeat () {}
    public static void notifyEnemyDefeat (String enemyName) {}

    // Combat Events
    public static void notifyEntityAttack (String attackerName, String targetName, int damageDealt) {}
    public static void notifyEntityDefense(String entityName, int damageBlocked) {}

    // Demon King Events
    public static void notifyOnDemonKingFloor() {}

    // Win Condition Events
    public void notifyVictory() {}
    public void notifyDefeat() {}

}
