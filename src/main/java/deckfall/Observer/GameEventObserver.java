package deckfall.Observer;

public interface GameEventObserver {
    // Floor Events
    void onFloorEntry(int floor);
    void onFloorClear(int floor);

    //Battle Events
    void onBattleEntry();
    void onBattleWin();

    // Death Events
    void onSlayerDefeat();
    void onEnemyDefeat(String enemyName);

    // Combat Events
    void onEntityAttack(String attackerName, String targetName, int damageDealt);
    void onEntityDefense(String entityName, int damageBlocked);
    void onEntityHeal(String entityName, int damageHealed);
    void onEntityPass(String entityName);

    // Demon King Events
    void onDemonKingFloor();

    // Win Condition Events
    void onVictory();
    void onDefeat();
}
