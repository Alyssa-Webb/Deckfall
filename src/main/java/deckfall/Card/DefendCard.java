package deckfall.Card;

public abstract class DefendCard extends Card {
    protected int damageBlocked;

    public DefendCard(String name, int energyCost, String description, int damageBlocked) {
        super(name, energyCost, description, CardType.SHIELD);
        this.damageBlocked = damageBlocked;
    }
}
