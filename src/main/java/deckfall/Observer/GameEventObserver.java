package deckfall.Observer;

import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.IntentType;
import deckfall.Card.*;
import deckfall.Controller.Listener;
import deckfall.Entity.Entity;

import java.util.List;

public interface GameEventObserver {
    //register event listeners:
    public void addDisplayFinishedListener(Listener listener);
    public void addUserInputListener(Listener listener);

    void defaultNotif(String message);

    // Floor Events
    void onFloorEntry(int floor);
    void onFloorClear(int floor);

    // Battle Events
    void onBattleEntry();
    void onBattleChange(List<Entity> currentLivingPlayers, List<Entity> currentLivingFoes);
    void onBattleWin();
    void onInvalidMoveSelected(String message);

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

    // Card Events
    void onCardDrawn(Card card);
    void onCardPlayed(Card card);
    void onDeckShuffled();

    // Enemy Intent
    void onDecideIntent(String enemyName, IntentType intent);
    void onEntityPass(String entityName);
    void onEntityDamaged(String entityName, int damageReceived);

    // Demon King Events
    void onDemonKingFloor();

    // Win Condition Events
    void onVictory();
    void onDefeat();

    void requestUserInput(RelevantGameData gameData);

    void startGame();

    void promptContinue(String message, Runnable onAcknowledge);
}
