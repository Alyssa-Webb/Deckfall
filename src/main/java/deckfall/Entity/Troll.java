package deckfall.Entity;

import deckfall.Die.RandomDie;
import deckfall.Observer.GameEventBus;

public class Troll extends Enemy {
    private static final String DEFAULT_TROLL_NAME = "Troll";
    private static final int DEFAULT_HEALTH = 25;
    private IntentType currentIntent;
    private static final String DEFAULT_TROLL_DESCRIPTION = "Troll. Troll fights with bare fists. Troll hits hard and sometimes blocks.";

    private static final int ATTACK_RANGE = 15;
    private static final int BLOCK_RANGE = 10;
    private static final int MIN_BLOCK = 5;

    public Troll() {
        super(DEFAULT_TROLL_NAME, DEFAULT_HEALTH);
        changeAttackDie(new RandomDie(ATTACK_RANGE));
        changeBlockDie(new RandomDie(BLOCK_RANGE));
    }

    public Troll(String enemyName, int healthPoints){ super(enemyName, healthPoints); }

    public String getDescription() { return DEFAULT_TROLL_DESCRIPTION; }

    // Troll -- Hits hard with fists, and rarely blocks. (80/20)
    public void decideIntent() {
        int roll = intentDie.roll();
        if (roll < 80) { this.currentIntent = IntentType.ATTACK;}
        else { this.currentIntent = IntentType.DEFEND; }
        GameEventBus.getGameEventBus().notifyDecideIntent(getName(), this.currentIntent);
    }

    public void executeIntent (Slayer slayer) {
        if (this.currentIntent == IntentType.ATTACK) {
            int damage = attackDie.roll();
            if (damage == 0) {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " misses! Dealt *" + damage + "* damage... ouch.");
            }
            else if (1 <= damage && damage <= 7) {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " throws a jab, dealing *" + damage + "* damage!");
            }
            else {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " commits to a right hook, dealing *" + damage + "* damage!");
            }
            slayer.takeDamage(damage);
        } else if (this.currentIntent == IntentType.DEFEND) {
            int block = blockDie.roll() + MIN_BLOCK;
            GameEventBus.getGameEventBus().notifyEntityDefense(this.getName(), block);
            this.gainBlock(block);
        }
    }
}