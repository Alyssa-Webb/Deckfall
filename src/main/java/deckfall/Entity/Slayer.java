package deckfall.Entity;

import deckfall.Card.Card;
import deckfall.Factory.CardFactory;

public class Slayer extends Entity {
    private static final String DEFAULT_SLAYER_NAME = "Slayer";
    private static final String DEFAULT_SLAYER_DESCRIPTION = "Slayer. A battle-worn adventurer climbing the tower floor by floor.";
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
        super(slayerName, healthPoints, DEFAULT_SLAYER_DESCRIPTION);
        this.maxEnergy = DEFAULT_MAX_ENERGY;
        this.energy    = maxEnergy;
        this.block     = 0;
        addToDeck(CardFactory.createStarterPlayerDeck());
    }

    // Start and End Turn Methods

    @Override
    public void startTurn() {
        super.startTurn();
        this.energy = maxEnergy;
        drawHand(DEFAULT_DRAW_COUNT);
    }

    public void endTurn() {
        discardHand();
    }

    // Card Methods
    public boolean playCard(Card card, Entity enemy) {
        String moveError = evalMove(card, enemy);
        if (!moveError.isEmpty()) {
            System.out.println(moveError);
            return false;
        }

        energy -= card.getEnergyCost();
        card.play(this, enemy);
        hand.remove(card);
        discardPile.add(card);
        return true;
    }

    @Override
    public String evalMove(Card selectedCard, Entity target) {
        // Call the parent validation (hand check, target check)
        String baseValidation = super.evalMove(selectedCard, target);
        if (!baseValidation.isEmpty()) {
            return baseValidation;
        }

        // Add the Slayer-specific Energy check
        if (this.energy < selectedCard.getEnergyCost()) {
            return "Not enough energy (" + this.energy + "/" + selectedCard.getEnergyCost() + ")";
        }

        return "";
    }

    // Getter Methods
    public int getEnergy()    { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
}