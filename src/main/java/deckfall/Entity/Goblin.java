package deckfall.Entity;

import deckfall.Die.RandomDie;
import deckfall.Observer.GameEventBus;

public class Goblin extends Enemy {
    private static final String DEFAULT_GOBLIN_NAME = "Goblin";
    private static final int DEFAULT_HEALTH = 15;
    private IntentType currentIntent;
    private static final String DEFAULT_GOBLIN_DESCRIPTION = "Goblin. Goblin fights with wrench. Goblin does not know how to defend itself...";

    private static final int ATTACK_RANGE = 10;

    public Goblin() {
        super(DEFAULT_GOBLIN_NAME, DEFAULT_HEALTH);
        changeAttackDie(new RandomDie(ATTACK_RANGE));
    }

    public Goblin(String enemyName, int healthPoints){
        super(enemyName, healthPoints);
    }

    // Goblin -- Only fights
    public void decideIntent () {
        this.currentIntent = IntentType.ATTACK;
        GameEventBus.getGameEventBus().notifyDecideIntent(getName(), currentIntent);
    }

    public String getDescription() { return DEFAULT_GOBLIN_DESCRIPTION; }

    public void executeIntent (Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = attackDie.roll();
            if (damage == 0) {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " misses! Dealt *" + damage + "* damage... ouch.");
            } else if (1 <= damage && damage <= 5) {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " pokes you with its wrench, dealing *" + damage + "* damage!");
            } else {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " swings their wrench, dealing *" + damage + "* damage with the hook of the wrench!");
            }
            slayer.takeDamage(damage);
        }
    }
}
