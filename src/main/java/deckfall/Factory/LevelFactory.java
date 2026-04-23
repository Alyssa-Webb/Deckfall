package deckfall.Factory;
import deckfall.Tower.*;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import static deckfall.Factory.BattleFactory.createBattles;

/**
 * Early Game:  Levels 1 through 5 - facing 1-2 battle
 * Late Game:   Levels 6 through 9 - facing 2-5 battles
 * End Game:    Level 10 - Facing Final Boss Battle (Demon King)
 */
public class LevelFactory {
    public static Level createLevel(Difficulty levelState) {
        return switch (levelState) {
            case EARLYGAME  -> createEarlyGameLevel();
            case LATEGAME   -> createLateGameLevel();
            case ENDGAME    -> createEndGameLevel();
            default         -> createEarlyGameLevel();
        };
    }
    public static Level createEarlyGameLevel() {
        int battleCount = ThreadLocalRandom.current().nextInt(1, 3);
        LinkedList<Battle> battles = createBattles(battleCount, Difficulty.EARLYGAME);
        return new Level(battles);
    }

    public static Level createLateGameLevel() {
        int battleCount = ThreadLocalRandom.current().nextInt(2, 6);
        LinkedList<Battle> battles = createBattles(battleCount, Difficulty.LATEGAME);
        return new Level(battles);
    }

    public static Level createEndGameLevel() {
        return new Level(createBattles(1, Difficulty.ENDGAME));
    }
}