package deckfall.Controller;

import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.Slayer;
import deckfall.Game.Game;
import deckfall.Game.GameState;
import deckfall.Game.MoveTypes;
import deckfall.Observer.GameEventObserver;
import deckfall.Tower.Difficulty;
import deckfall.Tower.TowerBuilder;

public class GameController {
    private Game game;
    private GameState gameState = GameState.GAME_START;
    // I'm concerned about a race condition when it comes to having multiple 'Observers' since they're also gonna need to manage input (ugh)
    private final GameEventObserver view;
    private final InformationDisplayFinishedListener displayContinuePlayingListener = new InformationDisplayFinishedListener();

    public GameController(Game game, GameEventObserver view) {
        this.view = view;
        setGame(game);
        view.addDisplayFinishedListener(displayContinuePlayingListener);
        view.addUserInputListener(new UserInputReceivedListener());
    }

    private void setGame(Game game) {
        this.game = game;
        this.game.setObserver(view);
    }

    public void gameStart() {
        gameState = GameState.GAME_START;
        view.startGame();
    }

    private void syncFromGame() {
        gameState = game.getGameState();
    }

    private void nextState() {
        switch (gameState) {
            /*
            case NOTIFYING_OF_SIDE_EFFECTS:
                    SideEffect sideEffect = game.getSideEffect();
                    evalSideEffect(sideEffect);
                    break;
             */
            case PLAYER_TURN -> view.requestUserInput(game.getRelevantGameData());
            case AWAITING_CONTINUE_PROMPT -> view.promptContinue(game.getContinuePromptMessage(), this::onContinueAcknowledged);
            case GAME_WIN, GAME_LOSS -> {}
            case GAME_START -> {}
        }   
    }

    // Asks the player if they want to continue (after a battle / level)
    private void onContinueAcknowledged() {
        displayContinuePlayingListener.ActionPerformed(new EntityAction());
    }

    public class InformationDisplayFinishedListener implements Listener {
        @Override
        public void ActionPerformed(EntityAction entityAction) {
            game.onDisplayAcknowledged(entityAction);
            syncFromGame();
            nextState();
        }
    }

    public class UserInputReceivedListener implements Listener {
        @Override
        public void ActionPerformed(EntityAction entityAction) {
            if (entityAction.getAction_enum() == MoveTypes.RESTART_GAME) {
                setGame(new Game(new Slayer(), TowerBuilder.buildTower(Difficulty.EASY)));
                gameStart();
                return;
            }

            String inputError = game.handleSlayerInput(entityAction);
            if (!inputError.isEmpty()) {
                view.onInvalidMoveSelected(inputError);
                return;
            }
            syncFromGame();
            MoveTypes move = entityAction.getAction_enum();
            if (move == MoveTypes.GET_CARD_DESCRIPTION || move == MoveTypes.GET_ENTITY_DESCRIPTION) {
                return;
            }
            if (gameState == GameState.PLAYER_TURN) {
                RelevantGameData data = game.getRelevantGameData();
                Thread t = new Thread(new RequestUserInputTask(data), "deckfall-request-input");
                t.setDaemon(true);
                t.start();
                return;
            }
            nextState();
        }
    }

    private final class RequestUserInputTask implements Runnable {
        private final RelevantGameData data;

        private RequestUserInputTask(RelevantGameData data) {
            this.data = data;
        }

        @Override
        public void run() {
            view.requestUserInput(data);
        }
    }
}
