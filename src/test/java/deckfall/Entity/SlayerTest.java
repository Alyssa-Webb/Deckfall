package deckfall.Entity;

import deckfall.Card.Card;
import deckfall.Card.ShieldCard;
import deckfall.Card.SlashCard;
import deckfall.Factory.SlayerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlayerTest {

    private Slayer slayer;
    private Enemy goblin;

    @BeforeEach
    void setUp() {
        slayer = SlayerFactory.createSlayer("TestSlayer", 30);
        goblin = new Goblin("TestGoblin", 15);
    }

    // Basic Constructor Tests

    @Test
    void testDefaultConstructor() {
        Slayer defaultSlayer = new Slayer();
        assertEquals("Slayer", defaultSlayer.getName());
        assertEquals(50, defaultSlayer.getHP());
    }

    @Test
    void testCustomConstructor() {
        assertEquals("TestSlayer", slayer.getName());
        assertEquals(30, slayer.getHP());
    }

    // Card Tests

    @Test
    void testStartTurnDrawsCards() {
        slayer.startTurn();
        assertFalse(slayer.getHand().isEmpty());
    }

    @Test
    void testEndTurnDiscardsHand() {
        slayer.startTurn();
        slayer.endTurn();
        assertTrue(slayer.getHand().isEmpty());
    }

    @Test
    void testPlayedCardMovesToDiscardPile() {
        slayer.startTurn();
        Card currentCard = slayer.getHand().get(0);
        slayer.playCard(currentCard, goblin);
        assertFalse(slayer.getHand().contains(currentCard));
    }

    @Test
    void testCannotPlayCardNotInHand() {
        SlashCard notInHand = new SlashCard();
        boolean played = slayer.playCard(notInHand, goblin);
        assertFalse(played);
    }

}