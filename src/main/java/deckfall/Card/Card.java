package deckfall.Card;

import deckfall.Entity.*;


public abstract class Card {
    private final String name;
    private final int energyCost;
    private final String description;
    private final CardType type;

    public Card(String cardName, int energyCost, String description, CardType type) {
        this.name = cardName;
        this.energyCost = energyCost;
        this.description = description;
        this.type = type;
    }

    public abstract void play(Entity user, Entity target);

    public String getName()    { return name; }
    public int getEnergyCost() { return energyCost; }
    public String getDescription() { return description; }
    public CardType getType() { return type; }

    public String getSimpleString() {
        return getName() + " (" + getEnergyCost() + ")";
    }

    @Override
    public String toString() {
        return getName() + ":\n\t" + getDescription() + "\nMana cost: " + getEnergyCost();
    }
}
