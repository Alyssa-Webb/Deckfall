package deckfall.Game;

import deckfall.Card.AttackCard;
import deckfall.Card.Card;
import deckfall.Card.DefendCard;
import deckfall.Card.HealCard;
import deckfall.DataClasses.*;
import deckfall.Entity.Enemy;
import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;
import deckfall.Observer.GameEventObserver;
import deckfall.Tower.Battle;
import deckfall.Tower.Level;
import deckfall.Tower.Tower;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Game {
    private final Tower tower;
    private Level currentLevel;
    private Battle currentBattle;
    private final List<Battle> battles = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final Slayer slayer;
    private Entity currentTurnHolder;
    private final LinkedList<SideEffect> sideEffects = new LinkedList<>();

    private GameEventObserver view;

    private int floorNumber;
    private int floorBattlesTotal;
    private int floorBattleIndex;
    private GameState currentGameState = GameState.GAME_START;

    public Game(Slayer playerCharacter, Tower tower) {
        this.slayer = playerCharacter;
        this.tower = tower;
        this.currentTurnHolder = slayer;
    }

    // Getter methods
    public GameState getGameState() {
        if (!sideEffects.isEmpty()) {
            return GameState.NOTIFYING_OF_SIDE_EFFECTS;
        }
        return currentGameState;
    }

    public RelevantGameData getRelevantGameData() {
        return new RelevantGameData(
                slayer.getHand(),
                currentBattle != null ? currentBattle.getActiveEnemies() : List.of(),
                slayer,
                floorNumber,
                floorBattleIndex,
                floorBattlesTotal
        );
    }

    public Slayer getSlayer() {
        return slayer;
    }

    public Battle getCurrentBattle() {
        return currentBattle;
    }
    public Entity getCurrentTurnHolder() {return currentTurnHolder;}

    public GameState nextGameState() {
        if (!sideEffects.isEmpty()) {
            return GameState.NOTIFYING_OF_SIDE_EFFECTS;
        }
        if(isOver()){
            if(slayerWon()){
                return GameState.GAME_WIN;
            } else {
                return GameState.GAME_LOSS;
            }
        }
        return currentGameState;
    }

    public SideEffect getSideEffect() {
        if (sideEffects.isEmpty()) {
            return null;
        }
        sideEffects.pop();
        return new SideEffect(SideEffectType.ENEMY_DEATH, "Enemy");
    }

    // Setter methods
    public void setObserver(GameEventObserver observer) {
        this.view = observer;
    }


    // Evaluate Move

    // if it's valid, then it returns an empty string. Otherwise, it returns the reason why the move is invalid.
    public String evalValidityOfMove(EntityAction action) {
        if (currentGameState != GameState.PLAYER_TURN) {
            return "It is not your turn to act.";
        }
        MoveTypes move = action.getAction_enum();
        return switch (move) {
            case USE_CARD -> slayer.evalMove(action.getSelectedCard(), action.getTarget());
            case END_TURN, PASS -> "";
            case GET_CARD_DESCRIPTION -> validateCardForDescription(action.getSelectedCard());
            case GET_ENTITY_DESCRIPTION -> validateEntityForDescription(action.getTarget());
            default -> "That action is not supported.";
        };
    }

    public void makeMove(EntityAction action) {
        MoveTypes move = action.getAction_enum();
        switch (move) {
            case USE_CARD -> applyUseCard(action.getSelectedCard(), action.getTarget());
            case END_TURN -> runEndTurn();
            case PASS -> {
                if (view != null) {
                    view.onTurnPass(slayer.getName());
                }
            }
            case GET_CARD_DESCRIPTION -> notifyCardDescription(action.getSelectedCard());
            case GET_ENTITY_DESCRIPTION -> notifyEntityDescription(action.getTarget());
            default -> {
            }
        }
    }


    // Handling Player input
    public String handleSlayerInput(EntityAction action) {
        String isValidMove = evalValidityOfMove(action);
        if (!isValidMove.isEmpty()) {
            return isValidMove;
        }
        makeMove(action);
        return "";
    }


    public boolean isOver() {
        return currentGameState == GameState.GAME_WIN || currentGameState == GameState.GAME_LOSS;
    }

    public boolean slayerWon() {
        return currentGameState == GameState.GAME_WIN;
    }

    public void startSlayerTurn() {
        slayer.startTurn();
    }

    public void endSlayerTurn() {
        slayer.endTurn();
    }
    public void onDisplayAcknowledged(EntityAction action) {
        switch (currentGameState) {
            case GAME_START -> beginFirstFloorFromIntro(action);
            case AWAITING_CONTINUE_PROMPT -> advanceAfterBattleContinue();
            default -> {
            }
        }
    }

    public String getContinuePromptMessage() {
        if (currentLevel == null) {
            return "Continue?";
        }
        if (currentLevel.hasNextBattle()) {
            return "You've won this battle, are you prepared for the next?.";
        }
        if (!tower.isCleared()) {
            return "You have cleared this floor. Are you prepared to ascend to the next floor?";
        }
        return "You Won! You've defeated the Demon King and saved the realm. Distant cheers from nearby villages shall sing your name for generations.";
    }

    public void executeEnemyTurn() {
        if (currentBattle == null) {
            return;
        }
        for (Entity foe : currentBattle.getActiveEnemies()) {
            if (!(foe instanceof Enemy enemy)) {
                continue;
            }
            int slayerHealthBefore = slayer.getHP();
            int blockBefore = enemy.getBlock();
            enemy.executeIntent(slayer);
            if (view != null && !enemy.getLastActionNarrative().isBlank()) {
                view.defaultNotif(enemy.getLastActionNarrative());
            }
            int slayerHealthLost = slayerHealthBefore - slayer.getHP();
            int blockGained = enemy.getBlock() - blockBefore;
            if (slayerHealthLost > 0) {
                if (view != null) {
                    view.onEntityAttack(enemy.getName(), slayer.getName(), slayerHealthLost);
                }
                if (view != null) {
                    view.onEntityDamaged(slayer.getName(), slayerHealthLost);
                }
            }
            if (blockGained > 0) {
                if (view != null) {
                    view.onEntityDefense(enemy.getName(), blockGained);
                }
            }
        }
    }

    // This is checker for when the user is viewing the description of a card
    private String validateCardForDescription(Card card) {
        if (card == null) {
            return "No card selected.";
        }
        if (!slayer.getHand().contains(card)) {
            return "That card is not in your hand.";
        }
        return "";
    }

    // This is checker for when the user is viewing the description of an enemy
    private String validateEntityForDescription(Entity target) {
        if (target == null) {
            return "No entity selected.";
        }
        if (currentBattle == null) {
            return "No active battle.";
        }
        boolean inBattle = target == slayer || currentBattle.getActiveEnemies().contains(target);
        if (!inBattle) {
            return "That entity is not in this battle.";
        }
        return "";
    }

    private void notifyCardDescription(Card card) {
        if (view != null) {
            view.defaultNotif(card.toString());
        }
    }

    private void notifyEntityDescription(Entity target) {
        if (view != null) {
            view.defaultNotif(target.getName() + ":\n\t" + target.getDescription() + "\n");
        }
    }

    private void beginFirstFloorFromIntro(EntityAction action) {
        if (action != null) {
            String requestedName = action.getPlayerName();
            if (requestedName != null && !requestedName.isBlank()) {
                slayer.setName(requestedName.trim());
            }
        }
        currentLevel = tower.getNextLevel();
        if (currentLevel == null) {
            if (view != null) {
                view.onDefeat();
            }
            currentGameState = GameState.GAME_LOSS;
            return;
        }
        floorNumber = 1;
        floorBattlesTotal = currentLevel.remainingBattles();
        floorBattleIndex = 0;
        if (view != null) {
            view.onFloorEntry(floorNumber);
        }
        startNextBattleInCurrentLevel();
        beginPlayerTurnCycle();
        currentGameState = GameState.PLAYER_TURN;
    }

    private void startNextBattleInCurrentLevel() {
        Battle battle = currentLevel.getNextBattle();
        currentBattle = battle;
        battles.add(battle);
        currentBattle.addPlayerCharacter(slayer);
        entities.clear();
        entities.addAll(currentBattle.getEntities());
        floorBattleIndex++;
        if (view != null) {
            view.onBattleEntry();
        }
        if (view != null) {
            view.onBattleChange(currentBattle.getActivePlayers(), currentBattle.getActiveEnemies());
        }
    }

    private void beginPlayerTurnCycle() {
        slayer.startTurn();
        rollEnemyIntents();
        if (view != null) {
            view.onTurnStart(slayer.getName());
        }
        if (view != null) {
            view.onBattleChange(currentBattle.getActivePlayers(), currentBattle.getActiveEnemies());
        }
    }

    private void rollEnemyIntents() {
        for (Entity foe : currentBattle.getActiveEnemies()) {
            if (foe instanceof Enemy enemy) {
                enemy.decideIntent();
                if (view != null && !enemy.getLastIntentNarrative().isBlank()) {
                    view.defaultNotif(enemy.getLastIntentNarrative());
                }
                if (view != null) {
                    view.onDecideIntent(enemy.getName(), enemy.getCurrentIntent());
                }
            }
        }
    }

    private void applyUseCard(Card card, Entity target) {
        if (card == null) {
            return;
        }

        if (card instanceof AttackCard ac) {
            int raw = ac.getDamageDealt();
            int hpBefore = target.getHP();
            slayer.playCard(card, target);
            int hpLost = hpBefore - target.getHP();
            if (view != null) {
                view.onCardPlayed(card);
            }
            if (view != null) {
                view.onEntityAttack(slayer.getName(), target.getName(), raw);
            }
            if (hpLost > 0) {
                if (view != null) {
                    view.onEntityDamaged(target.getName(), hpLost);
                }
            }
            if (!target.isAlive()) {
                if (view != null) {
                    view.onEnemyDefeat(target.getName());
                }
            }
        } else if (card instanceof DefendCard dc) {
            int block = dc.getBlockAmount();
            slayer.playCard(card, slayer);
            if (view != null) {
                view.onCardPlayed(card);
            }
            if (view != null) {
                view.onEntityDefense(slayer.getName(), block);
            }
        } else if (card instanceof HealCard hc) {
            int before = slayer.getHP();
            slayer.playCard(card, slayer);
            if (view != null) {
                view.onCardPlayed(card);
            }
            int healed = slayer.getHP() - before;
            if (healed > 0) {
                if (view != null) {
                    view.onEntityHeal(slayer.getName(), healed);
                }
            }
        } else {
            slayer.playCard(card, target);
            if (view != null) {
                view.onCardPlayed(card);
            }
        }

        if (!slayer.isAlive()) {
            if (view != null) {
                view.onSlayerDefeat();
            }
            if (view != null) {
                view.onDefeat();
            }
            currentGameState = GameState.GAME_LOSS;
            return;
        }
        if (currentBattle.battleOver()) {
            onBattleWonInternal();
        }
    }

    private void runEndTurn() {
        slayer.endTurn();
        if (view != null) {
            view.onTurnEnd(slayer.getName());
        }
        executeEnemyTurn();
        if (!slayer.isAlive()) {
            if (view != null) {
                view.onSlayerDefeat();
            }
            if (view != null) {
                view.onDefeat();
            }
            currentGameState = GameState.GAME_LOSS;
            return;
        }
        if (currentBattle.battleOver()) {
            onBattleWonInternal();
            return;
        }
        beginPlayerTurnCycle();
    }

    private void onBattleWonInternal() {
        if (view != null) {
            view.onBattleWin();
        }
        currentGameState = GameState.AWAITING_CONTINUE_PROMPT;
    }

    private void advanceAfterBattleContinue() {
        if (currentLevel.hasNextBattle()) {
            startNextBattleInCurrentLevel();
            beginPlayerTurnCycle();
            currentGameState = GameState.PLAYER_TURN;
            return;
        }

        if (view != null) {
            view.onFloorClear(floorNumber);
        }

        if (tower.isCleared()) {
            if (view != null) {
                view.onVictory();
            }
            currentGameState = GameState.GAME_WIN;
            return;
        }

        currentLevel = tower.getNextLevel();
        floorNumber++;
        floorBattlesTotal = currentLevel.remainingBattles();
        floorBattleIndex = 0;
        if (view != null) {
            view.onFloorEntry(floorNumber);
        }
        startNextBattleInCurrentLevel();
        beginPlayerTurnCycle();
        currentGameState = GameState.PLAYER_TURN;
    }
}
