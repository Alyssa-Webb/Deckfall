package deckfall.Entity;

public class DemonKing extends Enemy {
    private static final String DEFAULT_DEMON_KING_NAME = "Demon King";
    private static final int DEFAULT_HEALTH = 100;

    public DemonKing() {
        super(DEFAULT_DEMON_KING_NAME, DEFAULT_HEALTH);
    }

    public DemonKing(String enemyName, int healthPoints){
        super(enemyName, healthPoints);
    }
}
