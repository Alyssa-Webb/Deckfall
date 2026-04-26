package deckfall.Game;

import deckfall.Card.Card;
import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.EnemyType;
import deckfall.Entity.Slayer;
import deckfall.Factory.EnemyFactory;
import deckfall.Factory.SlayerFactory;
import deckfall.Factory.TowerFactory;
import deckfall.Tower.Difficulty;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testValidEntityAction(){
        Game game = new Game(SlayerFactory.createSlayer("Slayer"), TowerFactory.createSmallTower(Difficulty.EASY));
        game.startGame();

        EntityAction entityAction = new EntityAction()
                .setAction_enum(MoveTypes.PASS);

        assertEquals("", game.evalValidityOfMove(entityAction));
    }

    @Test
    public void testValidMove() {
        Slayer slayer = SlayerFactory.createSlayer("Slayer");
        Game game = new Game(slayer, TowerFactory.createSmallTower(Difficulty.EASY));
        game.startGame();
        game.nextGameState();

        assertTrue(game.makeMove(new EntityAction()
                .setAction_enum(MoveTypes.PASS)));
    }

    @Test
    public void testNextGameState() {
        Game game = new Game(SlayerFactory.createSlayer("Slayer"), TowerFactory.createSmallTower(Difficulty.EASY));
        game.startGame();

        assertEquals(GameState.PLAYER_TURN, game.nextGameState());
    }

    @Test
    public void testNotGameOver() {
        Game game = new Game(SlayerFactory.createSlayer("Slayer"), TowerFactory.createSmallTower(Difficulty.EASY));
        game.startGame();

        assertFalse(game.isOver());
        assertFalse(game.currentBattleOver());
    }

    @Test
    public void testRelevantGameData() {
        Slayer slayer = SlayerFactory.createSlayer("Slayer");
        Game game = new Game(slayer, TowerFactory.createSmallTower(Difficulty.EASY));
        game.startGame();

        assertEquals(slayer, game.getRelevantGameData().getSlayer());
    }

    @Test
    public void testEndSlayerTurn() {
        Slayer slayer = SlayerFactory.createSlayer("Slayer");
        Game game = new Game(slayer, TowerFactory.createSmallTower(Difficulty.EASY));
        game.startGame();
        game.startSlayerTurn();

        List<Card> initialCards = game.getRelevantGameData().getCards();

        game.endSlayerTurn();
        game.startSlayerTurn();

        assertNotEquals(initialCards, game.getRelevantGameData().getCards());
    }
}
