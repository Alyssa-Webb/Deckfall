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

    @Test
    void testEntityBlockAbsorption() {
        DemonKing boss = new DemonKing();
        boss.gainBlock(10);

        // If boss takes 15 damage, 10 should be blocked, 5 should hit HP
        int initialHP = boss.getHP();
        boss.takeDamage(15);

        assertEquals(initialHP - 5, boss.getHP(), "HP should only drop by 5 after block");
        assertEquals(0, boss.getBlock(), "Block should be fully consumed");
    }
}