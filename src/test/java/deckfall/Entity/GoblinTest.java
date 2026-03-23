package deckfall.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoblinTest {
    @Test
    void testDefaultConstructor() {
        Goblin defaultGoblin = new Goblin();
        assertEquals("Goblin", defaultGoblin.getName());
        assertEquals(15, defaultGoblin.getHP());
    }

}