package deckfall.Factory;
import deckfall.Tower.Level;
import deckfall.Tower.Difficulty;
import org.junit.jupiter.api.Test;

import static deckfall.Factory.LevelFactory.createLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelFactoryTests {
    @Test
    void earlyGameHasCorrectBattleRange() {
        for (int i = 0; i < 30; i++) {
            int count = countBattles(createLevel(Difficulty.EARLYGAME));
            assertTrue(count >= 1 && count <= 2,
                    "Expected 1-2 battles, got " + count);
        }
    }

    @Test
    void lateGameHasCorrectBattleRange() {
        for (int i = 0; i < 30; i++) {
            int count = countBattles(createLevel(Difficulty.LATEGAME));
            assertTrue(count >= 2 && count <= 5,
                    "Expected 2-5 battles, got " + count);
        }
    }

    @Test
    void endGameHasExactlyOneBattle() {
        assertEquals(1, countBattles(createLevel(Difficulty.ENDGAME)));
    }

    private int countBattles(Level level) {
        int count = 0;
        while (!level.levelIsCleared()) {
            level.getNextBattle();
            count++;
        }
        return count;
    }
}
