package deckfall.Tower;

import java.util.LinkedList;
import java.util.List;

public class Tower {
    LinkedList<Level> levels;
    private int totalLevels;

    public Tower() {
        this.levels = new LinkedList<>();
    }

    public Tower(LinkedList<Level> floors) {
        this.levels = floors;
        this.totalLevels = floors.size();
    }

    public boolean isCleared(){
        return levels.isEmpty();
    }

    public Level getCurrentLevel() { return this.levels.getFirst(); }

    public int getTotalLevels() {
        return this.totalLevels;
    }

    public Level getNextLevel() {
        if (levels.isEmpty()) {
            return null;
        }
        return levels.pop();
    }
}