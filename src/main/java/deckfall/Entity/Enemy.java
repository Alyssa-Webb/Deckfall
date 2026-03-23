package deckfall.Entity;


public abstract class Enemy extends Entity {
    private static final int DEFAULT_BLOCK_AMOUNT = 0;

    public Enemy (String enemyName, int healthPoints) {
        super(enemyName, healthPoints);
    }

    // Enemies decide their intent before their turn (during Player's turn)
    abstract public void decideIntent();

    abstract public void executeIntent(Slayer slayer);


}
