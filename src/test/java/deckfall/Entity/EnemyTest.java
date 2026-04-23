package deckfall.Entity;

import deckfall.Factory.EnemyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTests {

    @Test
    void createEnemyReturnsSkeleton() {
        assertInstanceOf(Skeleton.class, EnemyFactory.createEnemy(EnemyType.SKELETON));
    }

    @Test
    void createEnemyReturnsGoblin() {
        assertInstanceOf(Goblin.class, EnemyFactory.createEnemy(EnemyType.GOBLIN));
    }

    @Test
    void createEnemyReturnsTroll() {
        assertInstanceOf(Troll.class, EnemyFactory.createEnemy(EnemyType.TROLL));
    }

    @Test
    void createRandomEnemy() {
        Enemy enemy = EnemyFactory.createRandomEnemy();
        assertNotNull(enemy);
        assertTrue(enemy.isAlive());
    }

    @Test
    void createCustomEnemy() {
        Enemy enemy = EnemyFactory.createEnemy(EnemyType.SKELETON, "Skelly", 1);
        assertEquals("Skelly", enemy.getName());
        assertEquals(1, enemy.getHP());
    }
}
