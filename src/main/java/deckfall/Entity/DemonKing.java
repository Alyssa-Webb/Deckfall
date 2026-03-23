package deckfall.Entity;

import java.util.Random;

public class DemonKing extends Enemy {
    private static final String DEFAULT_DEMON_KING_NAME = "Demon King";
    private static final int DEFAULT_HEALTH = 100;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    // private static final String DEFAULT_SKELETON_DESCRIPTION = "Skeleton. Skeleton fights from a far, using a Bow and Shield. Since Skeleton is ranged, better chance at blocking."

    public DemonKing() { super(DEFAULT_DEMON_KING_NAME, DEFAULT_HEALTH); }

    public DemonKing(String enemyName, int healthPoints){ super(enemyName, healthPoints); }

    // Skeleton -- fights with bow, blocks often.
    public void decideIntent() {
        int roll = rand.nextInt(100);
        if (roll < 50) { this.currentIntent = IntentType.ATTACK;}
        else { this.currentIntent = IntentType.DEFEND; }
        System.out.println(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent(Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = rand.nextInt(16);

            if (damage == 0) {
                System.out.println(getName() + " misses! The ground trembles, but you are safe.");
            } else if (damage <= 10) {
                System.out.println(getName() + " strikes with dark energy, dealing *" + damage + "* damage!");
            } else {
                System.out.println(getName() + " unleashes a HELLISH SMITE, dealing *" + damage + "* damage!");
            }
            slayer.takeDamage(damage);

        } else if (currentIntent == IntentType.DEFEND) {
            int block = rand.nextInt(11) + 10;
            System.out.println(getName() + " summons a dark barrier! Blocked for *" + block + "* damage!");
            this.gainBlock(block);
        }
    }
}
