package deckfall.Entity;


import deckfall.Die.RandomDie;
import deckfall.Observer.GameEventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Skeleton extends Enemy {
    private static final String DEFAULT_SKELETON_NAME = "Skeleton";
    private static final int DEFAULT_HEALTH = 10;
    private IntentType currentIntent;
    private static final String DEFAULT_SKELETON_DESCRIPTION = "Skeleton. Skeleton fights from a far, using a Bow and Shield. Since Skeleton is ranged, better chance at blocking.";

    private static final int ATTACK_RANGE = 15;
    private static final int BLOCK_RANGE = 10;
    private static final int MIN_BLOCK = 5;

    public Skeleton() {
        super(DEFAULT_SKELETON_NAME, DEFAULT_HEALTH);
        changeAttackDie(new RandomDie(ATTACK_RANGE));
        changeBlockDie(new RandomDie(BLOCK_RANGE));
    }

    public Skeleton(String enemyName, int healthPoints){ super(enemyName, healthPoints); }

    public String getDescription() { return DEFAULT_SKELETON_DESCRIPTION; }

    // Skeleton -- fights with bow, blocks often.
    public void decideIntent() {
        int roll = intentDie.roll();
        if (roll < 50) { this.currentIntent = IntentType.ATTACK;}
        else { this.currentIntent = IntentType.DEFEND; }
        GameEventBus.getGameEventBus().notifyDecideIntent(getName(), this.currentIntent);
    }

    public void executeIntent (Slayer slayer) {
        if (this.currentIntent == IntentType.ATTACK) {
            int damage = attackDie.roll();
            if (damage == 0) {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " misses! Dealt *" + damage + "* damage... ouch.");
            }
            else if (1 <= damage && damage <= 5) {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " throws a jab, dealing *" + damage + "* damage!");
            }
            else {
                GameEventBus.getGameEventBus().notifyDefaultNotification(getName() + " commits to a right hook, dealing *" + damage + "* damage!");
            }
            slayer.takeDamage(damage);
        } else if (this.currentIntent == IntentType.DEFEND) {
            int block = blockDie.roll() + MIN_BLOCK;
            GameEventBus.getGameEventBus().notifyEntityDefense(this.getName(), block);
        }
    }
}
