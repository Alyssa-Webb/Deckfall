package deckfall.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private Slayer slayer;
    private Enemy goblin;

    @BeforeEach
    void setUp() {
        slayer = new Slayer("TestSlayer", 30);
        goblin = new Goblin("TestGoblin", 15);
    }

    @Test
    void testTakeDamageReducesHP() {
        slayer.takeDamage(10);
        goblin.takeDamage(5);
        assertEquals(20, slayer.getHP());
        assertEquals(10, goblin.getHP());
    }

    @Test
    void testHPDoesNotGoBelowZero() {
        slayer.takeDamage(9999);
        assertEquals(0, slayer.getHP());
    }

    @Test
    void testIsAliveWhenHPAboveZero() {
        assertTrue(slayer.isAlive());
    }

    @Test
    void testIsNotAliveWhenHPIsZero() {
        slayer.takeDamage(9999);
        assertFalse(slayer.isAlive());
    }

    @Test
    void testMaxHPSetCorrectly() {
        assertEquals(slayer.getHP(), slayer.getMaxHP());
    }
}