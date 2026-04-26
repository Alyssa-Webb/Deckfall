package deckfall.Card;

import deckfall.Factory.CardFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealCardTest {
    @Test
    void testDefaultHealCard () {
        Card card = CardFactory.createCard(CardType.HEAL);
        assertEquals( "Restore Health", card.getName());
        assertEquals(1, card.getEnergyCost());
    }
}