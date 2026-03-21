package deckfall.Card;

import deckfall.Entity.Entity;

public class SlashCard extends AttackCard {
    static final String DEFAULT_SLASH_NAME      = "Slash";
    static final int DEFAULT_SLASH_ENERGY_COST  = 1;
    static final int DEFAULT_SLASH_DAMAGE       = 7;

    public SlashCard() {
        super(DEFAULT_SLASH_NAME, DEFAULT_SLASH_ENERGY_COST, DEFAULT_SLASH_DAMAGE);
    }

    public SlashCard(String cardName, int energyCost, int damageDealt) {
        super(cardName, energyCost, damageDealt);
    }

    @Override
    public void play(Entity user, Entity target) {
        target.takeDamage(damageDealt); // damageDealt inherited from AttackCard
    }
}
