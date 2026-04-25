package deckfall.Entity;


import deckfall.Die.Die;
import deckfall.Die.RandomDie;

public abstract class Enemy extends Entity {
    private static final int DEFAULT_BLOCK_AMOUNT = 0;
    private static final int DEFAULT_INTENT_RANGE = 100;
    private static final int DEFAULT_ATTACK_RANGE = 10;
    private static final int DEFAULT_BLOCK_RANGE = 5;

    protected Die intentDie = new RandomDie(DEFAULT_INTENT_RANGE);
    protected Die attackDie = new RandomDie(DEFAULT_ATTACK_RANGE);
    protected Die blockDie = new RandomDie(DEFAULT_BLOCK_RANGE);

    public Enemy (String enemyName, int healthPoints) {
        super(enemyName, healthPoints);
    }

    // Enemies decide their intent before their turn (during Player's turn)
    abstract public void decideIntent();

    abstract public void executeIntent(Slayer slayer);

    public void changeIntentDie(Die die) {
        this.intentDie = die;
    }

    public void changeAttackDie(Die die) {
        this.attackDie = die;
    }

    public void changeBlockDie(Die die) {
        this.blockDie = die;
    }

    public void updateAllDice(Die intentDie, Die attackDie, Die blockDie) {
        this.intentDie = intentDie;
        this.attackDie = attackDie;
        this.blockDie = blockDie;
    }
}
