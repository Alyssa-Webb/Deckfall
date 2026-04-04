package deckfall.Entity;

import deckfall.Card.*;

public class Slayer extends Entity {
    private static final String DEFAULT_SLAYER_NAME = "Slayer";
    private static final int DEFAULT_HEALTH = 50;
    private static final int DEFAULT_DRAW_COUNT = 5;
    private static final int DEFAULT_MAX_ENERGY = 3;

    private final int maxEnergy;
    private int energy;

    public Slayer() {
        this(DEFAULT_SLAYER_NAME, DEFAULT_HEALTH);
    }

    public Slayer(String slayerName) {
        this(slayerName, DEFAULT_HEALTH);
    }

    public Slayer(String slayerName, int healthPoints) {
        super(slayerName, healthPoints);
        this.maxEnergy = DEFAULT_MAX_ENERGY;
        this.energy    = maxEnergy;
        this.block     = 0;
    }

    // Start and End Turn Methods

    public void startTurn() {
        this.energy = maxEnergy;
        this.block = 0;
        drawHand(DEFAULT_DRAW_COUNT);
    }

    public void endTurn() {
        discardHand();
    }


    // Card Methods
    // TODO print statements?
    public boolean playCard(Card card, Entity enemy) {
        if (!hand.contains(card)) {
            System.out.println("Card not in hand.");
            return false;
        }
        if (energy < card.getEnergyCost()) {
            System.out.println("Not enough energy to play " + card.getName());
            return false;
        }

        energy -= card.getEnergyCost();
        card.play(this, enemy);
        hand.remove(card);
        discardPile.add(card);
        return true;
    }

    // Getter Methods
    public int getEnergy()    { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
}
