package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Card.TargetType;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.IntentType;
import deckfall.Controller.Listener;
import deckfall.DataClasses.EntityAction;
import deckfall.Entity.Entity;
import deckfall.Game.MoveTypes;

import java.util.Scanner;

import java.util.List;

public class ConsoleLogger implements GameEventObserver {
    Scanner scanner = new Scanner(System.in);
    private final EntityAction emptyEntityAction = new EntityAction();
    private Listener displayFinishedListener;
    private Listener userInputListener;

    private List<String> userMoves = List.of(
            "Get card description",
            "Get enemy description",
            "Play a card",
            "Pass action",
            "End turn"
    );

    private Integer tryParsingInt(String userInput) {
        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String readNextLine() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

    private boolean confirmSelfTargetCard(Card card) {
        System.out.println("You selected " + card.getName() + ".");
        System.out.println("This card targets yourself. Use it? (y/n)");
        String input = readNextLine();
        return input != null && input.trim().equalsIgnoreCase("y");
    }

    @Override
    public void requestUserInput(RelevantGameData gameData) {
        System.out.println("Requesting user input");

        boolean successfullyMadeMove = false;

        while(!successfullyMadeMove) {
            System.out.println("\nIt's your turn! You have " + gameData.getSlayer().getHP() + " HP and " + gameData.getSlayer().getEnergy() + " energy.");
            System.out.println("Enemies: ");
            for (Entity entity : gameData.getEnemies()) {
                System.out.println("\t" + entity);
            }
            System.out.println("Moves: ");
            for (int i=0; i<userMoves.size(); i++) {
                System.out.println("\t" + (i+1) + ": " + userMoves.get(i));
            }
            System.out.println("Input the number of the move you'd like to make: ");

            String move = readNextLine();
            if (move == null) {
                successfullyMadeMove = true;
                System.out.println("No more input available. Ending turn.");
                userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.END_TURN));
                continue;
            }
            Integer moveNum = tryParsingInt(move);
            if(moveNum == null || moveNum > userMoves.size() || moveNum <= 0) {
                System.out.println("Input a number (1, 2, 3, 4, or 5).");
                continue;
            }
            switch(moveNum){
                case 1:
                    handleGetCardDescription(gameData.getCards());
                    break;
                case 2:
                    handleGetEnemyDescription(gameData.getEnemies(), gameData.getSlayer());
                    break;
                case 3:
                    successfullyMadeMove = handlePlayCard(gameData.getCards(), gameData.getEnemies(), gameData.getSlayer());
                    break;
                case 4:
                    System.out.println("You passed your action.");
                    userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.PASS));
                    break;
                case 5:
                    successfullyMadeMove = true;
                    System.out.println("You ended your turn.");
                    userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.END_TURN));
                    break;
            }
        }
    }

    private Integer userSelectCardNumber(List<Card> cards) {
        Integer userSelection = null;
        while(userSelection == null) {
            System.out.println("Select a card:");
            for (int i = 0; i < cards.size(); i++) {
                System.out.println("\t" + (i + 1) + ": " + cards.get(i).getSimpleString());
            }
            System.out.println("\t" + (cards.size()+1) + ": Back to move selection");
            String line = readNextLine();
            if (line == null) {
                return cards.size() + 1;
            }
            userSelection = tryParsingInt(line);
            if (userSelection == null || userSelection <= 0) {
                System.out.println("Please type a number, greater than 0");
            }
        }
        return userSelection;
    }

    private Integer userSelectTargetNumber(List<Entity> enemies) {
        Integer userSelection = null;
        while(userSelection == null) {
            System.out.println("Select a target:");
            for (int i = 0; i < enemies.size(); i++) {
                System.out.println("\t" + (i + 1) + ": " + enemies.get(i));
            }
            System.out.println("\t" + (enemies.size()+1) + ": Yourself");
            System.out.println("\t" + (enemies.size()+2) + ": Back to move selection");
            userSelection = tryParsingInt(scanner.nextLine());
            if (userSelection == null || userSelection <= 0) {
                System.out.println("Please type a number, greater than 0");
            }
        }
        return userSelection;
    }

    private boolean handlePlayCard(List<Card> cards, List<Entity> enemies, Entity slayer) {
        Card selectedCard = null;
        while(selectedCard == null) {
            int userSelection = userSelectCardNumber(cards) -1;
            if (userSelection < cards.size()) {
                selectedCard = cards.get(userSelection);
            } else {
                return false;
            }
        }

        // Stretch goal: implement a check here for whether the card's target type is AOE.
        // Stretch goal: implement AOE functionality in Card

        if (selectedCard.getTargetType() == TargetType.SELF_ONLY) {
            if (!confirmSelfTargetCard(selectedCard)) {
                System.out.println("Cancelled card use.");
                return false;
            }
            userInputListener.ActionPerformed(new EntityAction()
                    .setAction_enum(MoveTypes.USE_CARD)
                    .setSelectedCard(selectedCard)
                    .setTarget(slayer)
            );
            return true;
        }

        Entity selectedTarget = null;
        while(selectedTarget == null) {
            int userSelection = userSelectTargetNumber(enemies) -1;
            if(userSelection > enemies.size()) {
                return false;
            } else if (userSelection == enemies.size()) {
                selectedTarget = slayer;
            }
            else {
                selectedTarget = enemies.get(userSelection);
            }
        }
        userInputListener.ActionPerformed(new EntityAction()
                .setAction_enum(MoveTypes.USE_CARD)
                .setSelectedCard(selectedCard)
                .setTarget(selectedTarget)
        );
        return true;
    }

    private void handleGetCardDescription(List<Card> cards) {
        Card selectedCard = null;
        while(selectedCard == null) {
            int userSelection = userSelectCardNumber(cards);
            if (userSelection <= cards.size()) {
                selectedCard = cards.get(userSelection - 1);
            } else {
                return;
            }
        }
        System.out.println(selectedCard + "\n");
    }

    private void handleGetEnemyDescription(List<Entity> enemies, Entity slayer) {
        int userSelection = userSelectTargetNumber(enemies) - 1;
        if (userSelection > enemies.size()) {
            return;
        }

        if (userSelection == enemies.size()) {
            System.out.println(slayer.getName() + ":\n\t" + slayer.getDescription() + "\n");
            return;
        }

        Entity selectedEnemy = enemies.get(userSelection);
        System.out.println(selectedEnemy.getName() + ":\n\t" + selectedEnemy.getDescription() + "\n");
    }

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
    public void startGame() {
        System.out.println("Game started.");
        displayFinishedListener.ActionPerformed(emptyEntityAction);
    }

}
