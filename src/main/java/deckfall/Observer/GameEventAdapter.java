package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Entity.IntentType;

// Middleman
public class GameEventAdapter implements GameEventObserver {
    // Floor Events
    @Override
    public void onFloorEntry(int floor) {}

    @Override
    public void onFloorClear(int floor) {}

    // Battle Events
    @Override
    public void onBattleEntry() {}

    @Override
    public void onBattleWin() {}

    // Turn Events
    @Override
    public void onTurnStart(String entityName) {}

    @Override
    public void onTurnEnd(String entityName) {}

    @Override
    public void onTurnPass(String entityName) {}

    // Death Events
    @Override
    public void onSlayerDefeat() {}

    @Override
    public void onEnemyDefeat(String enemyName) {}

    // Combat Events
    @Override
    public void onEntityAttack(String attackerName, String targetName, int damageDealt) {}

    @Override
    public void onEntityDefense(String entityName, int damageBlocked) {}

    @Override
    public void onEntityHeal(String entityName, int damageHealed) {}

    // Card Events
    @Override
    public void onCardDrawn(Card card) {}

    @Override
    public void onCardPlayed(Card card) {}

    @Override
    public void onDeckShuffled() {}

    // Enemy Intent
    @Override
    public void onDecideIntent(String enemyName, IntentType intent) {}

    // Demon King Events
    @Override
    public void onDemonKingFloor() {}

    // Win Condition Events
    @Override
    public void onVictory() {}

    @Override
    public void onDefeat() {}
}
