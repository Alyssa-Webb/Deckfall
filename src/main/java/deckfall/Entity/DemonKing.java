package deckfall.Entity;

import java.util.Random;

public class DemonKing extends Enemy {
    private static final String DEFAULT_DEMON_KING_NAME = "Demon King";
    private static final int DEFAULT_HEALTH = 100;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    private static final String DEFAULT_DEMON_KING_DESCRIPTION = "Demon King. Ruler of this very Tower, preparing his horde of minions to be unleashed unto the realm to do his very bidding. Beware the dark energy he uses, some say they can hear the screams from the souls trapped within.";

    public DemonKing() { super(DEFAULT_DEMON_KING_NAME, DEFAULT_HEALTH, DEFAULT_DEMON_KING_DESCRIPTION); }

    public DemonKing(String enemyName, int healthPoints){ super(enemyName, healthPoints, DEFAULT_DEMON_KING_DESCRIPTION); }

    // Demon King -- fights with dark energy, does not block.
    public void decideIntent() {
        int roll = rand.nextInt(100);
        this.currentIntent = decideIntentFromRoll(roll, 50);
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

    @Override
    public IntentType getCurrentIntent() {
        return currentIntent;
    }
}
