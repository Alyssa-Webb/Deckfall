package deckfall.Card;

import deckfall.Factory.CardFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackCardTest {

    @Test
    void testDefaultSlashCard () {
        Card card = CardFactory.createCard(CardType.SLASH);
        assertEquals( "Slash", card.getName());
        assertEquals(1, card.getEnergyCost());
    }
}
