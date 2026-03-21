package deckfall.Factory;

import deckfall.Entity.*;

import java.util.Random;

public class EnemyFactory {

    // Spawn enemy by type
    public static Enemy createEnemy(EnemyType type) {
        return switch (type) {
            case SKELETON -> new Skeleton();
            case GOBLIN   -> new Goblin();
            case TROLL    -> new Troll();
        };
    }

    // Spawn custom enemies by type
    public static Enemy createEnemy(EnemyType type, String enemyName, int healthPoints) {
        return switch (type) {
            case SKELETON -> new Skeleton(enemyName, healthPoints);
            case GOBLIN -> new Goblin(enemyName, healthPoints);
            case TROLL -> new Troll(enemyName, healthPoints);
        };
    }

    // Randomly spawn an enemy
    public static Enemy createRandomEnemy() {
        EnemyType[] types = EnemyType.values();
        EnemyType randomType = types[new Random().nextInt(types.length)];
        return createEnemy(randomType);
    }
}
