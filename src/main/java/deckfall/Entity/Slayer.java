package deckfall.Entity;

import deckfall.Card.*;
import deckfall.Observer.GameEventBus;

public class Slayer extends Entity {
    private static final String DEFAULT_SLAYER_NAME = "Slayer";
    private static final int DEFAULT_HEALTH = 50;
    private static final int DEFAULT_DRAW_COUNT = 5;
    private static final int DEFAULT_MAX_ENERGY = 3;
    private static final String DEFAULT_SLAYER_DESCRIPTION = "You are the Slayer. You have been summoned by your realm to seek understanding behind the origin of missing persons.";


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
        this.energy = maxEnergy;
        this.block = 0;
        addToDeck(DEFAULT_CARD_DECK);
    }

    @Override
    public String evalMove(Card selectedCard, Entity target) {
        if(selectedCard.getEnergyCost() > energy){
            return "You don't have enough mana. This card requires " + selectedCard.getEnergyCost() + " mana.";
        }

        //TODO: give cards a target type (slayer-only, any, and enemy-only). For now they can do whatever to whomever lol

        return "";
    }

    public String getDescription() { return DEFAULT_SLAYER_DESCRIPTION; }

    @Override
    public boolean isSlayer() {
        return true;
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
    public boolean playCard(Card card, Entity enemy) {
        if (!hand.contains(card)) {
            GameEventBus.getGameEventBus().notifyDefaultNotification("Card not in hand");
            return false;
        }
        if (energy < card.getEnergyCost()) {
            GameEventBus.getGameEventBus().notifyNotEnoughEnergy(this.getName(), card);
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
