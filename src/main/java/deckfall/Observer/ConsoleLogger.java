package deckfall.Observer;

import deckfall.Entity.IntentType;

public class ConsoleLogger implements GameEventObserver {
    // Floor Events
    public void onFloorEntry(int floor) {}
    public void onFloorClear(int floor) {}

    // Battle Events
    public void onBattleEntry() {}
    public void onBattleWin() {}

    // Turn Events
    public void onTurnStart(String entityName) {}
    public void onTurnEnd(String entityName) {}
    public void onTurnPass(String entityName) {}

    // Death Events
    public void onSlayerDefeat() {}
    public void onEnemyDefeat(String enemyName) {}

    // Combat Events
    public void onEntityAttack(String attackerName, String targetName, int damageDealt){}
    public void onEntityDefense(String entityName, int damageBlocked) {}
    public void onEntityHeal(String entityName, int damageHealed) {}
    public void onDecideIntent(String enemyName, IntentType intent) {}

    // Demon King Events
    public void onDemonKingFloor() {}

    // Win Condition Events
    public void onVictory() {}
    public void onDefeat() {}

}
