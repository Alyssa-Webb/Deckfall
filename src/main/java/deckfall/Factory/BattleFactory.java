package deckfall.Factory;

import deckfall.Entity.Entity;
import deckfall.Tower.Battle;
import deckfall.Tower.Difficulty;

import java.util.LinkedList;

public class BattleFactory {

    private static Battle createEarlyBattle() {
        return new Battle(EnemyFactory.createRandomEnemies(1));
    }

    private static Battle createLateBattle() {
        return new Battle(EnemyFactory.createRandomEnemies(3));
    }

    private static Battle createBossBattle() {
        LinkedList<Entity> entities = new LinkedList<>();
        entities.add(EnemyFactory.createBoss());
        return new Battle(entities);
    }

    private static Battle createBattle(Difficulty difficulty) {
        return switch (difficulty) {
            case EARLYGAME -> createEarlyBattle();
            case LATEGAME  -> createLateBattle();
            case BOSS -> createBossBattle();
            default -> createEarlyBattle();
        };
    }

    public static LinkedList<Battle> createBattles(int battleCount, Difficulty difficulty) {
        LinkedList<Battle> battles = new LinkedList<>();
        for (int i = 0; i < battleCount; i++) {
            battles.add(createBattle(difficulty));
        }
        return battles;
    }

}
