package deckfall.Entity;

import java.util.Random;

public class Goblin extends Enemy {
    private static final String DEFAULT_GOBLIN_NAME = "Goblin";
    private static final int DEFAULT_HEALTH = 15;
    private IntentType currentIntent;
    private static final Random rand = new Random();
    private static final String DEFAULT_GOBLIN_DESCRIPTION = "Goblin. Goblin fights with wrench. Goblin does not know how to defend itself...";

    public Goblin() { super(DEFAULT_GOBLIN_NAME, DEFAULT_HEALTH, DEFAULT_GOBLIN_DESCRIPTION); }

    public Goblin(String enemyName, int healthPoints){
        super(enemyName, healthPoints, DEFAULT_GOBLIN_DESCRIPTION);
    }

    // Goblin -- Only fights
    public void decideIntent () {
        this.currentIntent = IntentType.ATTACK;
        System.out.println(getName() + " prepares to " + currentIntent + "!");
    }

    public void executeIntent (Slayer slayer) {
        if (currentIntent == IntentType.ATTACK) {
            int damage = rand.nextInt(11);
            printAttackMessage(
                    damage,
                    "pokes you with its wrench, dealing *{damage}* damage!",
                    "swings their wrench, dealing *{damage}* damage with the hook of the wrench!",
                    5
            );
            slayer.takeDamage(damage);
        }
    }

    @Override
    public IntentType getCurrentIntent() {
        return currentIntent;
    }
}
