package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.IntentType;
import deckfall.Controller.Listener;
import deckfall.DataClasses.EntityAction;
import deckfall.Entity.Entity;
import deckfall.Game.MoveTypes;

import java.util.Scanner;

import java.util.List;

public class ConsoleLogger implements GameEventObserver {
    EntityAction emptyEntityAction = new EntityAction();
    Listener displayFinishedListener;
    Listener userInputListener;

    @Override
    public void addDisplayFinishedListener(Listener listener) {
        displayFinishedListener = listener;
    }

    @Override
    public void addUserInputListener(Listener listener) {
        userInputListener = listener;
    }

    @Override
    public void defaultNotif(String message) {

    }

    // Floor Events
    public void onFloorEntry(int floor) {
    }
    public void onFloorClear(int floor) {

    }

    @Override
    public void onBattleEntry() {

    }

    @Override
    public void onBattleChange(List<Entity> currentLivingPlayers, List<Entity> currentLivingFoes) {

    }

    @Override
    public void onBattleWin() {

    }

    @Override
    public void onInvalidMoveSelected(String message) {

    }

    @Override
    public void onTurnStart(String entityName) {

    }

    @Override
    public void onTurnEnd(String entityName) {

    }

    @Override
    public void onTurnPass(String entityName) {

    }

    // Death Events
    public void onSlayerDefeat() {
        System.out.println("Slayer just died!");
        displayFinishedListener.ActionPerformed(emptyEntityAction);
    }

    public void onEnemyDefeat(String enemyName) {
        System.out.println(enemyName + " just died!");
        displayFinishedListener.ActionPerformed(emptyEntityAction);
    }

    // Combat Events
    public void onEntityAttack(String attackerName, String targetName, int damageDealt) {

    }

    public void onEntityDefense(String entityName, int damageBlocked) {

    }

    @Override
    public void onEntityHeal(String entityName, int damageHealed) {

    }

    @Override
    public void onCardDrawn(Card card) {

    }

    @Override
    public void onCardPlayed(Card card) {

    }

    @Override
    public void onDeckShuffled() {

    }

    @Override
    public void onDecideIntent(String enemyName, IntentType intent) {

    }

    @Override
    public void onEntityPass(String entityName) {

    }

    @Override
    public void onEntityDamaged(String entityName, int damageReceived) {

    }

    // Demon King Events
    public void onDemonKingFloor() {

    }

    // Win Condition Events
    public void onVictory() {
        System.out.println("You win!");
    }

    public void onDefeat() {

    }

    @Override
    public void requestUserInput(RelevantGameData gameData) {
        System.out.println("Requesting user input");
        Scanner scanner = new Scanner(System.in);

        System.out.println("It's your turn! You have " + gameData.getSlayer().getHP() + " HP and " + gameData.getSlayer().getEnergy() + " energy.");
        System.out.println("You can either pass your turn, get more information about a card or enemy, or play a card");
        System.out.println("Available cards:");
        for(Card card : gameData.getCards()) {
            System.out.println("(" + card.getEnergyCost() + ") " + card.getName());
        }
        System.out.println("Currently, you can only pass your turn. type \"pass\" to pass your turn");
        String move = scanner.nextLine();
        if(move.equals("pass")){
            System.out.println("You passed your turn. You gained 2 extra energy for next round.");
            System.out.println("(I dunno if STS actually does anything like that, I feel like it's typical for games like this tho)");
            displayFinishedListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.PASS));
        } else {
            System.out.println("You can only pass. Type 'pass' to pass");
            displayFinishedListener.ActionPerformed(emptyEntityAction);
        }
    }

    @Override
    public void startGame() {
        System.out.println("Game started.");
        displayFinishedListener.ActionPerformed(emptyEntityAction);
    }

}
