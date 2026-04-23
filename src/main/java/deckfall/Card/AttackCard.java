package deckfall.Card;

public abstract class AttackCard extends Card {
    protected int damageDealt;

    public AttackCard(String name, int energyCost, String description, int damageDealt) {
        super(name, energyCost, description, CardType.SLASH, TargetType.ENEMY_ONLY);
        this.damageDealt = damageDealt;
    }
}