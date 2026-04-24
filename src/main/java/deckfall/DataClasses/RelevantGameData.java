package deckfall.DataClasses;

import deckfall.Card.Card;
import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;

import java.util.List;

// Java is suggesting this be a record class
public class RelevantGameData {
    private final List<Card> cards;
    private final List<Entity> enemies;
    private final Slayer slayer;
    private final int floorNumber;
    private final int currentBattleIndex;
    private final int totalBattlesOnCurrentFloor;

    public RelevantGameData(List<Card> cards, List<Entity> enemies, Slayer slayer, int floorNumber, int currentBattleIndex, int totalBattlesOnCurrentFloor ) {
        this.cards = cards;
        this.enemies = enemies;
        this.slayer = slayer;
        this.floorNumber = floorNumber;
        this.currentBattleIndex = currentBattleIndex;
        this.totalBattlesOnCurrentFloor = totalBattlesOnCurrentFloor;
    }

    public List<Card> getCards() { return cards; }

    public List<Entity> getEnemies() { return enemies; }

    public Slayer getSlayer() { return slayer; }

    public int getFloorNumber() { return floorNumber; }

    public int getCurrentBattleIndex() { return currentBattleIndex; }

    public int getTotalBattlesOnCurrentFloor() { return totalBattlesOnCurrentFloor; }
}

