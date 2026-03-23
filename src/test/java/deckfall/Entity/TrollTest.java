package deckfall.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrollTest {
    @Test
    void testDefaultConstructor() {
        Troll defaultTroll = new Troll();
        assertEquals("Troll", defaultTroll.getName());
        assertEquals(25, defaultTroll.getHP());
    }
}
