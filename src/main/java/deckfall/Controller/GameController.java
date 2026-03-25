package deckfall.Controller;

import deckfall.DataClasses.Action;
import deckfall.DataClasses.EntityAction;
import deckfall.Game.Game;
import deckfall.Game.GameState;
import deckfall.Game.MoveTypes;
import deckfall.Observer.GameEventManager;
import deckfall.Observer.GameEventObserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private Game game;
    private GameState gameState = GameState.GAME_START;
    // I'm concerned about a race condition when it comes to having multiple 'Observers' since they're also gonna need to manage input (ugh)
    private GameEventManager manager;

    public GameController(Game game, GameEventManager manager) {
        this.game = game;
        this.manager = manager;
    }

    public void gameStart() {
        manager.visualizeGame();
    }

    private void next() {
        //do different things depending on the current state
        switch (gameState){
            case NOTIFYING_OF_SIDE_EFFECTS:
                Action action = k;
                if (action == null) {

                }
                break;
            case ENEMY_TURN:

                break;
            case PLAYER_TURN:
                break;
            case BATTLE_START:
                break;
            case BATTLE_END:
                break;
            case LEVEL_START:
                break;
            case LEVEL_END:
                break;
            case GAME_OVER:
                break;
            case GAME_START:
                break;
            case null, default:
                System.err.println("I don't think this will be necessary but ¯\\_(ツ)_/¯");
                break;
        }
    }

    private Action parseEntityAction(){
        return new Action() {
            @Override
            public void execute() {
                System.out.println("A");
            }
        };
    }


    public class InformationDisplayFinishedListener implements UserInputListener {
        @Override
        public void UserActionPerformed(EntityAction e) {
            gameState = game.evalNextGameState(e, GameState.GAME_START);
            next();
        }
    }

    public class UserInputReceivedListener implements UserInputListener {
        @Override
        public void UserActionPerformed(EntityAction e) {
            String isMoveValid = game.evalValidityOfMove(e);
            if(!isMoveValid.isEmpty()){
                manager.onInvalidMoveSelected(isMoveValid);
                manager.requestUserInput(game.getCurrentTurnHolder().getHand());
            }
            /*Action action = () -> {
                System.out.println("A");
            };*/
            if(e.getAction_enum() == MoveTypes.PASS || e.getAction_enum() == MoveTypes.USE_CARD){
                gameState = game.evalNextGameState();
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
