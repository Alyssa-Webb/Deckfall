package deckfall.Card;

public abstract class AttackCard extends Card {
    protected int damageDealt;

    //Card(String cardName, int energyCost, String description, CardType type)
    public AttackCard(String name, int energyCost, String description, int damageDealt) {
        super(name, energyCost, description, CardType.SLASH);
        this.damageDealt = damageDealt;
    }

    public int getDamageDealt() {
        return damageDealt;
    }
}