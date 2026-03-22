package deckfall.Tower;

import deckfall.Entity.Entity;

import java.util.LinkedList;

public class Level {
    LinkedList<Battle> battles;

    public Level(LinkedList<Battle> battles) {
        this.battles = battles;
    }

    public boolean levelOver() {
        return battles.isEmpty();
    }

    public Battle getNextBattle(){
        return battles.pop();
    }
}
