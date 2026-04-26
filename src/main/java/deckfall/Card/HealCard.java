package deckfall.Card;

import deckfall.Entity.Entity;

public abstract class HealCard extends Card {
    private int healthHealed;

    public HealCard(String name, int energyCost, String description, int healthHealed) {
        super(name, energyCost, description, CardType.HEAL);
        this.healthHealed = healthHealed;
    }

    public int getHealthHealed() {
        return healthHealed;
    }

    @Override
    public void play(Entity user, Entity target) {
        target.gainHealth(healthHealed);
    }
}
