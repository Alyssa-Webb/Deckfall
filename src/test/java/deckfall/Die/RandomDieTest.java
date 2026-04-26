package deckfall.Die;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomDieTest {
    @Test
    void testRollRangeWithMaxTen() {
        RandomDie die = new RandomDie(10);
        int roll = die.roll();
        assertTrue(roll >= 0 && roll <= 10);
    }

    @Test
    void testRollWithMaxZeroAlwaysZero() {
        RandomDie die = new RandomDie(0);
        assertEquals(0, die.roll());
    }
}

