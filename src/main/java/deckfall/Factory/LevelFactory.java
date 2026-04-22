package deckfall.Tower;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class LevelFactory {
    public static Level createLevel(LevelState levelState) {
        return switch (levelState) {
            case EARLYGAME -> createEarlyGameLevel();
            case LATEGAME -> createLateGameLevel();
            case ENDGAME -> createEndGameLevel();
        };
    }

    /* Levels 1 through 5 - facing 1-2 battles */
    public static Level createEarlyGameLevel() {
        int battleCount = ThreadLocalRandom.current().nextInt(1, 3);
        LinkedList<Battle> battles = BattleFactory.createBattles(battleCount, BattleDifficulty.EARLY);
        return new Level(battles);
    }

    /* Levels 6 through 9 - facing 2-5 battles */
    public static Level createLateGameLevel() {
        int battleCount = ThreadLocalRandom.current().nextInt(2, 6);
        LinkedList<Battle> battles = BattleFactory.createBattles(battleCount, BattleDifficulty.LATE);
        return new Level(battles);
    }

    /* Level 10 - Facing Final Boss Battle (Demon King) */
    public static Level createEndGameLevel() {
        return new Level(deckfall.Tower.BattleFactory.createBattles(1, BattleDifficulty.BOSS));
    }

}