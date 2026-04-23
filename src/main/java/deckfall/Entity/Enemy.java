package deckfall.Entity;


public abstract class Enemy extends Entity {
    public Enemy (String enemyName, int healthPoints) {
        super(enemyName, healthPoints);
    }

    public Enemy(String enemyName, int healthPoints, String description) {
        super(enemyName, healthPoints, description);
    }

    abstract public void decideIntent();

    abstract public void executeIntent(Slayer slayer);

    abstract public IntentType getCurrentIntent();

}
