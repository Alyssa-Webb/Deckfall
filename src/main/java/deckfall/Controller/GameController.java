package deckfall.Controller;

import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.SideEffect;
import deckfall.Game.Game;
import deckfall.Game.GameState;
import deckfall.Observer.GameEventObserver;
import deckfall.Observer.GameEventBus;

public class GameController {
    private final Game game;
    private GameState gameState = GameState.GAME_START;
    private final GameEventObserver view;


    public GameController(Game game, GameEventObserver view) {
        this.game = game;
        this.view = view;
        view.addDisplayFinishedListener(new InformationDisplayFinishedListener());
        view.addUserInputListener(new UserInputReceivedListener());

    }

    public void gameStart() {
        game.startGame();
        view.startGame();
        GameEventBus.getGameEventBus().registerObserver(view);
    }

    private void next() {
        //do different things depending on the current state
        switch (gameState){
            /*case NOTIFYING_OF_SIDE_EFFECTS:
                //SideEffect sideEffect = game.getSideEffect();
                //evalSideEffect(sideEffect);
                break;*/
            case ENEMY_TURN:
                game.playEnemyTurn();
                view.update(game.getRelevantGameData());
                break;
            case PLAYER_TURN:
                //view.requestUserInput(game.getCurrentTurnHolder().getHand());
                game.startSlayerTurn();
                view.requestUserInput( game.getRelevantGameData() );
                break;
            case BATTLE_START:
                break;
            case BATTLE_END:
                break;
            case LEVEL_START:
                break;
            case LEVEL_END:
                break;
            case GAME_WIN:
                view.onVictory();
                break;
            case GAME_LOSS:
                view.onDefeat();
            case GAME_OVER:
                break;
            case GAME_START:
                view.startGame();
                break;
            case null, default:
                System.err.println("I don't think this will be necessary but ¯\\_(ツ)_/¯");
                break;
        }
    }

    private void evalSideEffect(SideEffect sideEffect) {
        switch(sideEffect.sideEffectType){
            case ENEMY_DEATH:
                view.onEnemyDefeat("Enemy");
                break;
            case null:
                throw new RuntimeException("Something is wrong with the way game changes states");
            default:
                view.defaultNotification(sideEffect.gameData);
        }
    }


    public class InformationDisplayFinishedListener implements Listener {
        @Override
        public void ActionPerformed(EntityAction e) {
            gameState = game.nextGameState();
            next();
        }
    }

    public class UserInputReceivedListener implements Listener {
        @Override
        public void ActionPerformed(EntityAction e) {
            String isMoveValid = game.evalValidityOfMove(e);
            if(!isMoveValid.isEmpty()){
                view.onInvalidMoveSelected(isMoveValid);
            } else {
                GameEventBus.getGameEventBus().clearEvents();
                boolean res = game.makeMove(e);
                if (!res) {
                    view.onInvalidMoveSelected("The move failed. Please try again.");
                    view.requestUserInput(game.getRelevantGameData());
                } else {
                    game.endSlayerTurn();
                    gameState = game.nextGameState();
                    next();
                }
            }

            // send the ActionEvent (or wtv I Really end up goin with) to game.play(ActionEvent)
            //PlayResult playResult = game.play(ActionEvent);
            /*if(playResult.failure) {
                //manager.onMoveInability(playResult);
            } else {
                manager.onMovePlay(playResult);
                evalNextState();
            }
             */
        }
    }
}
