package deckfall.Card;

public abstract class AttackCard extends Card {
    protected int damageDealt;

    public AttackCard(String name, int energyCost, int damageDealt) {
        super(name, energyCost);
        this.damageDealt = damageDealt;
    }
}