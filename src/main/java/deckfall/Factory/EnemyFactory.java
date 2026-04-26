package deckfall.Factory;

import deckfall.Entity.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnemyFactory {

    // Spawn enemy by type
    public static Enemy createEnemy(EnemyType type) {
        return switch (type) {
            case SKELETON -> new Skeleton();
            case GOBLIN   -> new Goblin();
            case TROLL    -> new Troll();
        };
    }

    // Spawn custom enemy by type with name and HP override
    public static Enemy createEnemy(EnemyType type, String enemyName, int healthPoints) {
        return switch (type) {
            case SKELETON -> new Skeleton(enemyName, healthPoints);
            case GOBLIN   -> new Goblin(enemyName, healthPoints);
            case TROLL    -> new Troll(enemyName, healthPoints);
        };
    }

    // Spawns Boss
    public static Enemy createBoss() {
        return new DemonKing();
    }

    // Randomly spawn one enemy (never spawns boss)
    public static Enemy createRandomEnemy() {
        List<EnemyType> types = new ArrayList<>(List.of(EnemyType.values()));
        EnemyType randomType = types.get(ThreadLocalRandom.current().nextInt(types.size()));
        return createEnemy(randomType);
    }

    // Randomly spawn a number of enemies (never spawns boss)
    public static LinkedList<Entity> createRandomEnemies(int numEnemies) {
        LinkedList<Entity> enemies = new LinkedList<>();
        for (int i = 0; i < numEnemies; i++) {
            enemies.add(createRandomEnemy());
        }
        return enemies;
    }
}