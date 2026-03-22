package deckfall.Card;

import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;

public class ShieldCard extends DefendCard {
    static final String DEFAULT_SHIELD_NAME     = "Shield Block";
    static final int DEFAULT_SHIELD_ENERGY_COST = 1;
    static final int DEFAULT_SHIELD_BLOCK       = 5;
    static final String DEFAULT_SHIELD_DESCRIPTION = "Shield Block";

    public ShieldCard() {
        super(DEFAULT_SHIELD_NAME, DEFAULT_SHIELD_ENERGY_COST, DEFAULT_SHIELD_DESCRIPTION, DEFAULT_SHIELD_BLOCK);
    }

    public ShieldCard(String cardName, int energyCost, String description, int damageBlocked) {
        super(cardName, energyCost, description, damageBlocked);
    }

    @Override
    public void play(Entity user, Entity target) {
        if (user instanceof Slayer slayer) {
            slayer.gainBlock(damageBlocked);
        }
    }
}
