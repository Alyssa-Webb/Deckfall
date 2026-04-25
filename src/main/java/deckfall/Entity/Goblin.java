package deckfall.Entity;

import java.util.Random;

public class Goblin extends Enemy {
    private static final String DEFAULT_GOBLIN_NAME = "Goblin";
    private static final int DEFAULT_HEALTH = 15;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    // private static final String DEFAULT_GOBLIN_DESCRIPTION = "Goblin. Goblin fights with wrench. Goblin does not know how to defend itself..."

    public Goblin() { super(DEFAULT_GOBLIN_NAME, DEFAULT_HEALTH); }

    public Goblin(String enemyName, int healthPoints){
        super(enemyName, healthPoints);
    }

    // Goblin -- Only fights
    public void decideIntent () {
        this.currentIntent = IntentType.ATTACK;
        notifications.add(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent (Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = rand.nextInt(11);
            if (damage == 0) {
                notifications.add(getName() + " misses! Dealt *" + damage + "* damage... ouch.");
            } else if (1 <= damage && damage <= 5) {
                notifications.add(getName() + " pokes you with its wrench, dealing *" + damage + "* damage!");
            } else {
                notifications.add(getName() + " swings their wrench, dealing *" + damage + "* damage with the hook of the wrench!");
            }
            slayer.takeDamage(damage);
        }
    }
}
