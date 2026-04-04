package deckfall.Card;

public abstract class HealCard extends Card {
    private int healthHealed;

    public HealCard(String name, int energyCost, String description, int healthHealed) {
        super(name, energyCost, description, CardType.HEAL);
    }
}
