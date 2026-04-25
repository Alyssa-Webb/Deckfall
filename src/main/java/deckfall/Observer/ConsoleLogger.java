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
    Scanner scanner = new Scanner(System.in);
    private final EntityAction emptyEntityAction = new EntityAction();
    private Listener displayFinishedListener;
    private Listener userInputListener;

    private List<String> userMoves = List.of(
            "get card description",
            "play card",
            "pass",
            "do a lil jig"
    );

    private Integer tryParsingInt(String userInput) {
        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void requestUserInput(RelevantGameData gameData) {
        for(String notif : gameData.getNotifications()) {
            System.out.println(notif);
        }

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

            String move = scanner.nextLine();
            Integer moveNum = tryParsingInt(move);
            if(moveNum == null || moveNum > userMoves.size() || moveNum <= 0) {
                System.out.println("Input a number between 1 and four");
                continue;
            }
            switch(moveNum){
                case 1:
                    handleGetCardDescription(gameData.getCards());
                    break;
                case 2:
                    successfullyMadeMove = handlePlayCard(gameData.getCards(), gameData.getEnemies(), gameData.getSlayer());
                    break;
                case 3:
                    successfullyMadeMove = true;
                    System.out.println("You passed your turn.");
                    userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.PASS));
                    break;
                case 4:
                    System.out.println("You did a lil jig!\nNothing happened.");
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
            userSelection = tryParsingInt(scanner.nextLine());
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

        // TODO: implement a check here for whether the card's target type is AOE.
        // TODO: implement AOE functionality in Card

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

    /* Entities don't currently have a description. neither do enemies. This is a stretch goal for if we ever add those
    private void handleGetEnemyDescription(List<Entity> enemies, Entity slayer) {
        Entity selectedTarget = null;
        while(selectedTarget == null) {
            int userSelection = userSelectTargetNumber(enemies) -1;
            if(userSelection <= enemies.size() +1) {
                return;
            } else if (userSelection == enemies.size()) {
                System.out.println("It's you!");
            }
            else {
                System.out.println(enemies.get(userSelection).getDescription());
            }
        }
    }*/

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
    //TODO: write an actually satisfying ending. Maybe give the player the option to skip all of it though lol
    public void onVictory() {
        System.out.println("You win!");
    }

    public void onDefeat() {
        System.out.println(
                """
                Tragically, you meet your end. The Tower held up to its reputation -- you wonder, briefly, whether there was anything
                 you could've done to prevent this.\
                Could you have brought more supplies? Worn different armor? Refused to go on this quest at all?\
                But it's pointless. Your last thought as you slip into the great unknown, is the fate of the poor villagers,
                 who's fates will never be known'.\
                (sorry, no option to try again. Well, except for restarting the program of course.)
                """);
    }

    @Override
    public void startGame() {
        System.out.println(
                """
                You are the Slayer; a powerful and accomplished being heralded across the lands.\
                
                Today, you're tackling your biggest challenge yet: the Tower.\
                
                What's inside is completely unknown, but none who step inside are never seen again.\
                
                And yet, reports of missing persons in the surrounding areas have skyrocketed since its sudden appearance a few months ago.\
                
                At their wits end, the desperate people came to you, seeking aid.\
                
                You push open the large doors, and step inside...\
                
                (press enter to continue)
                """);
        scanner.nextLine();
        displayFinishedListener.ActionPerformed(emptyEntityAction);
    }

    @Override
    public void update(RelevantGameData relevantGameData) {
        for(String notif : relevantGameData.getNotifications()) {
            System.out.println(notif);
        }
        displayFinishedListener.ActionPerformed(emptyEntityAction);
    }

}
