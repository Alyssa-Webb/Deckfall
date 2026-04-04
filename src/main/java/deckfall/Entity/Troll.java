package deckfall.Entity;

import java.util.Random;

public class Troll extends Enemy {
    private static final String DEFAULT_TROLL_NAME = "Troll";
    private static final int DEFAULT_HEALTH = 25;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    // private static final String DEFAULT_TROLL_DESCRIPTION = "Troll. Troll fights with bare fists. Troll hits hard and sometimes blocks."

    public Troll() { super(DEFAULT_TROLL_NAME, DEFAULT_HEALTH); }

    public Troll(String enemyName, int healthPoints){ super(enemyName, healthPoints); }

    // Troll -- Hits hard with fists, and rarely blocks. (80/20)
    public void decideIntent() {
        int roll = rand.nextInt(100);
        if (roll < 80) { this.currentIntent = IntentType.ATTACK;}
        else { this.currentIntent = IntentType.DEFEND; }
        System.out.println(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent (Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = rand.nextInt(16);
            if (damage == 0) {
                System.out.println(getName() + " misses! Dealt *" + damage + "* damage... ouch.");
            }
            else if (1 <= damage && damage <= 7) {
                System.out.println(getName() + " throws a jab, dealing *" + damage + "* damage!");
            }
            else {
                System.out.println(getName() + " commits to a right hook, dealing *" + damage + "* damage!");
            }
            slayer.takeDamage(damage);
        } else if (currentIntent == IntentType.DEFEND) {
            int block = rand.nextInt(5) + 5;
            System.out.println(getName() + " is blocking! Blocked for *" + block + "* damage!");
            this.gainBlock(block);
        }
    }
}