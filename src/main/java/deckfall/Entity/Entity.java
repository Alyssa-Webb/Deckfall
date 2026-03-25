package deckfall.Entity;

import deckfall.Card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity {
    protected String name;
    protected int healthPoints;
    protected int maxHealthPoints;

    protected List<Card> deck = new ArrayList<>();
    protected List<Card> hand = new ArrayList<>();
    protected List<Card> discardPile = new ArrayList<>();

    public Entity(String name, int healthPoints) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.maxHealthPoints = healthPoints;
    }

    // Entity Methods
    public void takeDamage(int damageTaken) {
        this.healthPoints = Math.max(0, this.healthPoints - damageTaken);
    }

    public boolean isAlive() {
        return this.healthPoints > 0;
    }

    // Card Methods
    public void addToDeck(Card card) {
        deck.add(card);
    }

    public void drawHand(int drawCount) {
        if (deck.size() < drawCount) {
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
    public String getName() { return name; }
    public int getHP()      { return healthPoints; }
    public int getMaxHP()   { return maxHealthPoints; }
    public List<Card> getHand()   { return hand; }

    public String evalMove(Card selectedCard, Entity target) {
        //TODO: eval whether the card is in the entity's hand, whether they have enough energy/mana? to use it,
        // and whether the selected target is valid for the type of card selected. If any are false, return a string explaining that
        return "";
    }
}