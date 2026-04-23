package deckfall.Entity;
import java.util.Random;

public class Skeleton extends Enemy {
    private static final String DEFAULT_SKELETON_NAME = "Skeleton";
    private static final int DEFAULT_HEALTH = 10;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    private static final String DEFAULT_SKELETON_DESCRIPTION = "Skeleton. Skeleton fights from a far, using a Bow and Shield. Since Skeleton is ranged, better chance at blocking.";

    public Skeleton() { super(DEFAULT_SKELETON_NAME, DEFAULT_HEALTH, DEFAULT_SKELETON_DESCRIPTION); }

    public Skeleton(String enemyName, int healthPoints){ super(enemyName, healthPoints, DEFAULT_SKELETON_DESCRIPTION); }

    // Skeleton -- fights with bow, blocks often.
    public void decideIntent() {
        int roll = rand.nextInt(100);
        this.currentIntent = decideIntentFromRoll(roll, 50);
        System.out.println(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent (Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = rand.nextInt(11);
            printAttackMessage(
                    damage,
                    "throws a jab, dealing *{damage}* damage!",
                    "commits to a right hook, dealing *{damage}* damage!",
                    5
            );
            slayer.takeDamage(damage);
        } else if (currentIntent == IntentType.DEFEND) {
            int block = rand.nextInt(5) + 5;
            System.out.println(getName() + " is blocking! Blocked for *" + block + "* damage!");
            this.gainBlock(block);
        }
    }

    @Override
    public IntentType getCurrentIntent() {
        return currentIntent;
    }
}
