package deckfall.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemonKingTest {
    @Test
    void testDefaultConstructor() {
        DemonKing defaultDemonKing = new DemonKing();
        assertEquals("Demon King", defaultDemonKing.getName());
        assertEquals(50, defaultDemonKing.getHP());
    }
}
