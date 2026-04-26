package deckfall.Tower;

import java.util.LinkedList;

public class Level {
    LinkedList<Battle> battles;
    private final int totalBattles;

    public Level(LinkedList<Battle> battles) {
        this.battles = battles;
        this.totalBattles = battles.size();
    }

    public boolean levelIsCleared() {
        return battles.isEmpty();
    }

    public int getTotalBattles() {
        return this.totalBattles;
    }

    public Battle getNextBattle(){
        if (levelIsCleared()) {
            throw new IllegalStateException("No battles remaining in this level.");
        }
        return battles.pop();
    }
}