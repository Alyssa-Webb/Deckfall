package deckfall.Observer;

public class ConsoleLogger implements GameEventObserver {
    // Floor Events
    public void onFloorEntry(int floor) {

    }

    public void onFloorClear(int floor) {

    }

    // Death Events
    public void onSlayerDefeat() {

    }

    public void onEnemyDefeat(String enemyName) {

    }

    // Combat Events
    public void onEntityAttack(String attackerName, String targetName, int damageDealt) {

    }

    public void onEntityDefense(String entityName, int damageBlocked) {

    }

    // Demon King Events
    public void onDemonKingFloor() {

    }

    // Win Condition Events
    public void onVictory() {

    }

    public void onDefeat() {

    }

}
