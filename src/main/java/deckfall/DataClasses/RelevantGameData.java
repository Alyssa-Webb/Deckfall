package deckfall.DataClasses;

import deckfall.Card.Card;
import deckfall.Entity.Slayer;

import java.util.ArrayList;
import java.util.List;

public class RelevantGameData {
    private final List<Card> cards;
    private final List<String> enemies;
    //TODO: get rid of slayer
    private final Slayer slayer;

    public RelevantGameData(List<Card> cards, List<String> enemies, Slayer slayer) {
        this.cards = cards;
        this.enemies = enemies;
        this.slayer = slayer;
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public Slayer getSlayer() {
        return slayer;
    }
}
