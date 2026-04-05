package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Controller.Listener;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.Entity;
import deckfall.Entity.IntentType;

import java.util.List;

// Middleman
public class GameEventAdapter implements GameEventObserver {
    @Override
    public void addDisplayFinishedListener(Listener listener) {

    }

    @Override
    public void addUserInputListener(Listener listener) {

    }

    @Override
    public void defaultNotif(String message) {

    }

    // Floor Events
    @Override
    public void onFloorEntry(int floor) {}

    @Override
    public void onFloorClear(int floor) {}

    // Battle Events
    @Override
    public void onBattleEntry() {}

    @Override
    public void onBattleChange(List<Entity> currentLivingPlayers, List<Entity> currentLivingFoes) {

    }

    @Override
    public void onBattleWin() {}

    @Override
    public void onInvalidMoveSelected(String message) {

    }

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

    @Override
    public void onEntityPass(String entityName) {

    }

    @Override
    public void onEntityDamaged(String entityName, int damageReceived) {

    }

    // Demon King Events
    @Override
    public void onDemonKingFloor() {}

    // Win Condition Events
    @Override
    public void onVictory() {}

    @Override
    public void onDefeat() {}

    @Override
    public void requestUserInput(RelevantGameData gameData) {
    }

    @Override
    public void startGame() {

    }
}
