package deckfall.Tower;

import java.util.LinkedList;

public class Level {
    LinkedList<Battle> battles;

    public Level(LinkedList<Battle> battles) {
        this.battles = battles;
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

    public Battle getNextBattle(){
        if (levelIsCleared()) {
            throw new IllegalStateException("No battles remaining in this level.");
        }
        return battles.pop();
    }
}