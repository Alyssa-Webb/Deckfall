package deckfall.Card;

import deckfall.Entity.Entity;

public class RestoreHealthCard extends HealCard {
    static final String DEFAULT_HEAL_NAME     = "Restore Health";
    static final int DEFAULT_HEAL_ENERGY_COST = 1;
    static final int DEFAULT_HEALTH_RESTORED = 5;
    static final String DEFAULT_HEAL_DESCRIPTION = "Restore Health";

    public RestoreHealthCard() {
        super(DEFAULT_HEAL_NAME, DEFAULT_HEAL_ENERGY_COST, DEFAULT_HEAL_DESCRIPTION, DEFAULT_HEALTH_RESTORED);
    }

    public RestoreHealthCard(String name, int energyCost, String description, int healthHealed) {
        super(name, energyCost, description, healthHealed);
    }

    @Override
    public void play(Entity user, Entity target) {
        user.heal(getHealthHealed());
    }
}
