package deckfall.Entity;

public class Goblin extends Enemy {
    private static final String DEFAULT_SLAYER_NAME = "Goblin";
    private static final int DEFAULT_HEALTH = 15;

    public Goblin() {
        super(DEFAULT_SLAYER_NAME, DEFAULT_HEALTH);
    }

    public Goblin(String enemyName, int healthPoints){
        super(enemyName, healthPoints);
    }
}
