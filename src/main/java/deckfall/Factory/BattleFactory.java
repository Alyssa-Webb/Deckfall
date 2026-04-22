package deckfall.Tower;
import deckfall.Factory.EnemyFactory;

import java.util.LinkedList;


public class BattleFactory {
    public static LinkedList<Battle> createBattles(int battleCount, BattleDifficulty difficulty) {
        LinkedList<Battle> battles = new LinkedList<>();
        for (int i = 0; i < battleCount; i++) {
            battles.add(createBattle(difficulty));
        }
        return battles;
    }

    private static Battle createBattle(BattleDifficulty difficulty) {
        return switch (difficulty) {
            case EARLY -> createEarlyBattle();
            case LATE  -> createLateBattle();
            case BOSS  -> createBossBattle();
        };
    }

    private static Battle createEarlyBattle() { return new Battle(EnemyFactory.createRandomEnemies(2)); }
    private static Battle createLateBattle()  { /* spawn mid-tier enemies */ }
    private static Battle createBossBattle()  { /* spawn boss */ }
}