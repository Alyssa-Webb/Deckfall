package deckfall.Observer;

import deckfall.Entity.IntentType;

public interface GameEventObserver {
    // Floor Events
    void onFloorEntry(int floor);
    void onFloorClear(int floor);

    // Battle Events
    void onBattleEntry();
    void onBattleWin();

    // Turn Events
    void onTurnStart(String entityName);
    void onTurnEnd(String entityName);
    void onTurnPass(String entityName);

    // Death Events
    void onSlayerDefeat();
    void onEnemyDefeat(String enemyName);

    // Combat Events
    void onEntityAttack(String attackerName, String targetName, int damageDealt);
    void onEntityDefense(String entityName, int damageBlocked);
    void onEntityHeal(String entityName, int damageHealed);

    // Enemy Intent
    void onDecideIntent(String enemyName, IntentType intent);

    // Demon King Events
    void onDemonKingFloor();

    // Win Condition Events
    void onVictory();
    void onDefeat();
}
