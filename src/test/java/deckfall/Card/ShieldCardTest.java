package deckfall.Card;

import deckfall.Factory.CardFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShieldCardTest {
    @Test
    void testDefaultShieldCard () {
        Card card = CardFactory.createCard(CardType.SHIELD);
        assertEquals( "Shield Block", card.getName());
        assertEquals(1, card.getEnergyCost());
    }
}
