package deckfall.Entity;

import deckfall.Card.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class Entity {
    private static final String DEFAULT_ENTITY_DESCRIPTION = "No description available.";
    protected String name;
    protected String description;
    protected int healthPoints;
    protected int maxHealthPoints;
    protected int block;

    protected List<Card> deck = new ArrayList<>();
    protected List<Card> hand = new ArrayList<>();
    protected List<Card> discardPile = new ArrayList<>();

    public static Logger logger;


    public Entity(String name, int healthPoints) {
        this(name, healthPoints, DEFAULT_ENTITY_DESCRIPTION);
    }

    public Entity(String name, int healthPoints, String description) {
        this.name = name;
        this.description = description;
        this.healthPoints = healthPoints;
        this.maxHealthPoints = healthPoints;
    }

    // Entity Methods
    public void takeDamage(int damageTaken) {
        int damageThatHitsHP = Math.max(0, damageTaken - this.block);
        this.block = Math.max(0, this.block - damageTaken);
        this.healthPoints = Math.max(0, this.healthPoints - damageThatHitsHP);
    }

    // Combat Methods
    public void gainBlock(int blockAmount) {
        this.block += blockAmount;
    }

    public void heal(int healthHealed) {
        this.healthPoints = Math.min(this.maxHealthPoints, this.healthPoints + Math.max(0, healthHealed));
    }

    public boolean isAlive() {
        return this.healthPoints > 0;
    }

    // Card Methods
    public void addToDeck(Card card) {
        deck.add(card);
    }
    public void addToDeck(List<Card> cards) {
        deck.addAll(cards);
    }

    public void drawHand(int drawCount) {
        if (deck.size() <= drawCount) {
            deck.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(deck);
        }
        for (int i = 0; i < drawCount && !deck.isEmpty(); i++) {
            hand.add(deck.remove(0));
        }
    }

    public void discardHand() {
        discardPile.addAll(hand);
        hand.clear();
    }

    // Getter Methods
    public String getName()     { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public int getHP()          { return healthPoints; }
    public int getMaxHP()       { return maxHealthPoints; }
    public List<Card> getHand() { return hand; }
    public int getDrawPileSize()   { return deck.size(); }
    public int getDiscardPileSize() { return discardPile.size(); }
    public int getBlock()       { return block; }

    public String evalMove(Card selectedCard, Entity target) {
        // TODO: eval whether the card is in the entity's hand,
        // whether they have enough energy/mana? to use it,
        // and whether the selected target is valid for the type of card selected.

        if (!hand.contains(selectedCard)) {
            return "Card not in hand.";
        }

        if (target == null) {
            return "No target selected.";
        }
        if (!target.isAlive()) {
            return "Target is already dead.";
        }

        return "";
    }

    public void pass() {
        // Logic for passing the turn
    }

    public void startTurn() {
        this.block = 0;
    }

    @Override
    public String toString() {
        return getName() + "(" + getHP() + ")";
    }
}