package deckfall.Observer;

public interface GameEventObserver {
    // Floor Events
    void onFloorEntry(int floor);
    void onFloorClear(int floor);

    // Death Events
    void onSlayerDefeat();
    void onEnemyDefeat(String enemyName);

    // Combat Events
    void onEntityAttack(String attackerName, String targetName, int damageDealt);
    void onEntityDefense(String entityName, int damageBlocked);

    // Demon King Events
    void onDemonKingFloor();

    // Win Condition Events
    void onVictory();
    void onDefeat();
}
