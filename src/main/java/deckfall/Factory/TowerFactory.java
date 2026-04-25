package deckfall.Factory;

import deckfall.Tower.Battle;
import deckfall.Tower.Difficulty;
import deckfall.Tower.Level;
import deckfall.Tower.Tower;

import java.util.LinkedList;

/**
 * Presets for Tower difficulty.
 * EASY   : 1 enemy per battle.
 * MEDIUM : 2 enemies per battle.
 * HARD   : 3 enemies per battle.
 **/
public class TowerFactory {

    public static Tower createStandardTower(Difficulty towerDifficulty) {
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

    public static Tower createSmallTower(Difficulty towerDifficulty) {
        LinkedList<Level> floors = new LinkedList<>();

        // Levels 1-4 (Early Game)  ← was looping 5 times before
        for (int i = 0; i < 4; i++) {
            floors.add(createLevelWithFixedDifficulty(towerDifficulty, Difficulty.EARLYGAME));
        }

        // Level 5 (Boss)
        floors.add(LevelFactory.createLevel(Difficulty.BOSS));

        return new Tower(floors);
    }

    public static Tower createTinyTower(Difficulty towerDifficulty) {
        LinkedList<Level> floors = new LinkedList<>();

        // Level 1 (Early Game)
        floors.add(createLevelWithFixedDifficulty(towerDifficulty, Difficulty.EARLYGAME));

        // Level 2 (Boss)
        floors.add(LevelFactory.createLevel(Difficulty.BOSS));

        return new Tower(floors);
    }

    private static Level createLevelWithFixedDifficulty(Difficulty towerDifficulty, Difficulty stageDifficulty) {
        int enemyCount = switch (towerDifficulty) {
            case EASY   -> 1;
            case MEDIUM -> 2;
            case HARD   -> 3;
            default     -> 1;
        };

        int numBattles = (stageDifficulty == Difficulty.EARLYGAME) ? 2 : 3;

        LinkedList<Battle> battles = new LinkedList<>();
        for (int i = 0; i < numBattles; i++) {
            LinkedList<deckfall.Entity.Entity> enemies = EnemyFactory.createRandomEnemies(enemyCount);
            battles.add(new Battle(enemies));
        }

        return new Level(battles);
    }
}