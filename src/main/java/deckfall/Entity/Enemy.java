package deckfall.Entity;


public abstract class Enemy extends Entity {
    public Enemy (String enemyName, int healthPoints) {
        super(enemyName, healthPoints);
    }

    public Enemy(String enemyName, int healthPoints, String description) {
        super(enemyName, healthPoints, description);
    }

    // Enemies decide their intent before their turn (during Player's turn)
    abstract public void decideIntent();

    abstract public void executeIntent(Slayer slayer);

    abstract public IntentType getCurrentIntent();

    protected IntentType decideIntentFromRoll(int roll, int attackChancePercent) {
        if (roll < attackChancePercent) {
            return IntentType.ATTACK;
        }
        return IntentType.DEFEND;
    }
}
