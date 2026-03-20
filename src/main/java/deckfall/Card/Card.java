package deckfall.Card;

import deckfall.Entity.*;


public abstract class Card {
    protected String name;
    protected int energyCost;

    public Card(String cardName, int energyCost) {
        this.name = cardName;
        this.energyCost = energyCost;
    }

    public abstract void play(Entity user, Entity target);

    public String getName()    { return name; }
    public int getEnergyCost() { return energyCost; }
}
