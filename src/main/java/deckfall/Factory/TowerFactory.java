package deckfall.Factory;
import deckfall.Tower.Battle;
import deckfall.Tower.Difficulty;
import deckfall.Tower.Level;
import deckfall.Tower.Tower;

import java.util.LinkedList;

/**
 * Presets for Tower Difficulty.
 * Easy: 1 enemy per battle.
 * Medium: 2 enemies per battle.
 * Hard: 3 enemies per battle.
 **/
public class TowerFactory {

    public static Tower createStandardTower (Difficulty towerDifficulty) {
        LinkedList<Level> floors = new LinkedList<>();

        // Levels 1-5 (Early Game)
        for (int i = 0; i < 5; i++) {
            floors.add(createLevelWithFixedDifficulty(towerDifficulty, Difficulty.EARLYGAME));
        }

        // Levels 6-9 (Late Game)
        for (int i = 0; i < 4; i++) {
            floors.add(createLevelWithFixedDifficulty(towerDifficulty, Difficulty.LATEGAME));
        }

        // Level 10 (Boss)
        floors.add(LevelFactory.createLevel(Difficulty.BOSS));

        return new Tower(floors);
    }

    public static Tower createSmallTower (Difficulty towerDifficulty) {
        LinkedList<Level> floors = new LinkedList<>();

        // Levels 1-4 (Early Game)
        for (int i = 0; i < 5; i++) {
            floors.add(createLevelWithFixedDifficulty(towerDifficulty, Difficulty.EARLYGAME));
        }

        // Level 5 (Boss)
        floors.add(LevelFactory.createLevel(Difficulty.BOSS));

        return new Tower(floors);
    }

    private static Level createLevelWithFixedDifficulty(Difficulty towerDifficulty, Difficulty stageDifficulty) {
        int enemyCount = switch (towerDifficulty) {
            case EASY   -> 1;
            case MEDIUM -> 2;
            case HARD   -> 3;
            default -> 1;
        };

        LinkedList<Battle> battles = new LinkedList<>();

        // Determine number of battles in this level based on stageDifficulty
        int numBattles = (stageDifficulty == Difficulty.EARLYGAME) ? 2 : 3;

        for (int i = 0; i < numBattles; i++) {
            // Manually creating a battle with the specific enemy count
            LinkedList<deckfall.Entity.Entity> enemies = EnemyFactory.createRandomEnemies(enemyCount);
            battles.add(new Battle(enemies));
        }

        return new Level(battles);
    }
}
