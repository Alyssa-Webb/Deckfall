package deckfall.DataClasses;

import deckfall.Card.Card;
import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;

import java.util.List;

public class RelevantGameData {
    private final List<Card> cards;
    private final List<Entity> enemies;
    private final Slayer slayer;

    public RelevantGameData(List<Card> cards, List<Entity> enemies, Slayer slayer) {
        this.cards = cards;
        this.enemies = enemies;
        this.slayer = slayer;
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Entity> getEnemies() {
        return enemies;
    }

    public Slayer getSlayer() {
        return slayer;
    }
}
