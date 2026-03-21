package deckfall.Entity;

public class Troll extends Enemy {
    private static final String DEFAULT_SLAYER_NAME = "Troll";
    private static final int DEFAULT_HEALTH = 25;

    public Troll() {
        super(DEFAULT_SLAYER_NAME, DEFAULT_HEALTH);
    }

    public Troll(String enemyName, int healthPoints){
        super(enemyName, healthPoints);
    }
}