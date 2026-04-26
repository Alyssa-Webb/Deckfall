package deckfall.Tower;

import java.util.LinkedList;

public class Level {
    LinkedList<Battle> battles;
    private int totalBattles;
    private Battle currentBattle;

    public Level(LinkedList<Battle> battles) {
        this.battles = battles;
        this.totalBattles = battles.size();
    }

    public boolean levelIsCleared() {
        return battles.isEmpty();
    }

    public boolean hasNextBattle() {
        return !battles.isEmpty();
    }

    public int remainingBattles() {
        return battles.size();
    }

    public int getTotalBattles() {
        return this.totalBattles;
    }

    public Battle getCurrentBattle() {
        return battles.getFirst();
    }

    public Battle getNextBattle(){
        if (levelIsCleared()) {
            throw new IllegalStateException("No battles remaining in this level.");
        }
        return battles.pop();
    }
}