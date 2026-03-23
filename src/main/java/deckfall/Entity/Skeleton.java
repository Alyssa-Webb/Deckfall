package deckfall.Entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Skeleton extends Enemy {
    private static final String DEFAULT_SKELETON_NAME = "Skeleton";
    private static final int DEFAULT_HEALTH = 10;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    // private static final String DEFAULT_SKELETON_DESCRIPTION = "Skeleton. Skeleton fights from a far, using a Bow and Shield. Since Skeleton is ranged, better chance at blocking."

    public Skeleton() { super(DEFAULT_SKELETON_NAME, DEFAULT_HEALTH); }

    public Skeleton(String enemyName, int healthPoints){ super(enemyName, healthPoints); }

    // Skeleton -- fights with bow, blocks often.
    public void decideIntent() {
        int roll = rand.nextInt(100);
        if (roll < 50) { this.currentIntent = IntentType.ATTACK;}
        else { this.currentIntent = IntentType.DEFEND; }
        System.out.println(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent (Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = rand.nextInt(0) + 5;
            if (damage == 0) {
                System.out.println(getName() + " misses! Dealt *" + damage + "* damage... ouch.");
            }
            else if (1 <= damage && damage <= 5) {
                System.out.println(getName() + " throws a jab, dealing *" + damage + "* damage!");
            }
            else {
                System.out.println(getName() + " commits to a right hook, dealing *" + damage + "* damage!");
            }
            slayer.takeDamage(damage);
        } else if (currentIntent == IntentType.DEFEND) {
            int block = rand.nextInt(5) + 5;
            System.out.println(getName() + " is blocking! Blocked for *" + block + "* damage!");
        }
    }
}
