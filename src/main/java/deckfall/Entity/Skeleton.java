package deckfall.Entity;

public class Skeleton extends Enemy {
    private static final String DEFAULT_SLAYER_NAME = "Skeleton";
    private static final int DEFAULT_HEALTH = 10;

    public Skeleton() {
        super(DEFAULT_SLAYER_NAME, DEFAULT_HEALTH);
    }

    public Skeleton(String enemyName, int healthPoints){
        super(enemyName, healthPoints);
    }
}
