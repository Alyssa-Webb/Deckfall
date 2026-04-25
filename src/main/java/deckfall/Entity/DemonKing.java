package deckfall.Entity;

import deckfall.Die.Die;
import deckfall.Die.RandomDie;

import java.util.Random;

public class DemonKing extends Enemy {
    private static final String DEFAULT_DEMON_KING_NAME = "Demon King";
    private static final int DEFAULT_HEALTH = 100;
    private IntentType currentIntent;
    private static final String DEFAULT_DEMON_KING_DESCRIPTION = "Demon King. Ruler of this very Tower, preparing his horde of minions to be unleashed unto the realm to do his very bidding. Beware the dark energy he uses, some say they can hear the screams from the souls trapped within.";

    //private static final int INTENT_RANGE = 100;
    private static final int ATTACK_RANGE = 15;
    private static final int BLOCK_RANGE = 10;
    public static final int MIN_BLOCK = 8;

    public DemonKing() {
        super(DEFAULT_DEMON_KING_NAME, DEFAULT_HEALTH);
        changeAttackDie(new RandomDie(ATTACK_RANGE));
        changeBlockDie(new RandomDie(BLOCK_RANGE));
    }


    public DemonKing(String enemyName, int healthPoints){ super(enemyName, healthPoints); }

    public String getDescription() { return DEFAULT_DEMON_KING_DESCRIPTION; }

    // Skeleton -- fights with bow, blocks often.
    public void decideIntent() {
        int roll = intentDie.roll();
        if (roll < 50) { this.currentIntent = IntentType.ATTACK;}
        else { this.currentIntent = IntentType.DEFEND; }
        notifications.add(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent(Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = attackDie.roll();

            if (damage == 0) {
                notifications.add(getName() + " misses! The ground trembles, but you are safe.");
            } else if (damage <= 10) {
                notifications.add(getName() + " strikes with dark energy, dealing *" + damage + "* damage!");
            } else {
                notifications.add(getName() + " unleashes a HELLISH SMITE, dealing *" + damage + "* damage!");
            }
            slayer.takeDamage(damage);

        } else if (currentIntent == IntentType.DEFEND) {
            int block = blockDie.roll() + MIN_BLOCK;
            notifications.add(getName() + " summons a dark barrier! Blocked for *" + block + "* damage!");
            this.gainBlock(block);
        }
    }
}
