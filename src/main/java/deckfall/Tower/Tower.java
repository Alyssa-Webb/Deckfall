package deckfall.Tower;

import java.util.LinkedList;
import java.util.List;

public class Tower {
    LinkedList<Level> floors;

    public Tower(LinkedList<Level> floors) {
        this.floors = floors;
    }

    public boolean isCleared(){
        return floors.isEmpty();
    }

    public Level getNextLevel() {
        return floors.pop();
    }
}
