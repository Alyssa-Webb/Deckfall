package deckfall.Card;

public abstract class HealCard extends Card {
    protected final int healthHealed;

    public HealCard(String name, int energyCost, String description, int healthHealed) {
        super(name, energyCost, description, CardType.HEAL, TargetType.SELF_ONLY);
        this.healthHealed = healthHealed;
    }

    public int getHealthHealed() {
        return healthHealed;
    }
}
