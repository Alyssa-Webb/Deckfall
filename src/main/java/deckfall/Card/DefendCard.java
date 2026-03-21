package deckfall.Card;

public abstract class DefendCard extends Card {
    protected int damageBlocked;

    public DefendCard(String name, int energyCost, int damageBlocked) {
        super(name, energyCost);
        this.damageBlocked = damageBlocked;
    }
}
