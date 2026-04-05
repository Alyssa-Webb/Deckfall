package deckfall.Entity;

import deckfall.Card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity {
    protected String name;
    protected int healthPoints;
    protected int maxHealthPoints;
    protected int block;

    protected List<Card> deck = new ArrayList<>();
    protected List<Card> hand = new ArrayList<>();
    protected List<Card> discardPile = new ArrayList<>();

    protected static final List<Card> DEFAULT_CARD_DECK = List.of(
            new SlashCard("Simple Slash", 1, "Deal 2 points of Slash damage to one enemy", 2),
            new SlashCard("Simple Slash", 1, "Deal 2 points of Slash damage to one enemy", 2),
            new SlashCard("Throw Down", 0, "Your final stand.", 1),
            new RestoreHealthCard("Moon's blessing", 3, "Receive the blessing of the moon, and receive up to 5 points of health", 5),
            new ShieldCard("Block", 1, "Block up to 3 points of incoming damage", 3)
    );

    public Entity(String name, int healthPoints) {
        this.name = name;
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
            hand.add(deck.removeFirst());
        }
    }

    public void discardHand() {
        discardPile.addAll(hand);
        hand.clear();
    }

    // Getter Methods
    public String getName() { return name; }
    public int getHP()      { return healthPoints; }
    public int getMaxHP()   { return maxHealthPoints; }
    public List<Card> getHand()   { return hand; }
    public int getBlock()     { return block; }

    public String evalMove(Card selectedCard, Entity target) {
        //TODO: eval whether the card is in the entity's hand, whether they have enough energy/mana? to use it,
        // and whether the selected target is valid for the type of card selected. If any are false, return a string explaining that
        return "";
    }

    public void pass() {
    }
}