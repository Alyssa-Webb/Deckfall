package deckfall.Entity;

import deckfall.Card.*;
import deckfall.Factory.CardFactory;
import deckfall.Observer.GameEventBus;

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

    protected List<String> notifications = new ArrayList<>();

    protected static final List<Card> DEFAULT_CARD_DECK = CardFactory.createFullDeck();

    public Entity(String name, int healthPoints) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.maxHealthPoints = healthPoints;
    }

    // Entity Methods
    public void takeDamage(int damageTaken) {
        int unblockedDamage = Math.max(0, damageTaken - this.block);

        GameEventBus.getGameEventBus().notifyEntityDamaged(name, unblockedDamage);

        this.block = Math.max(0, this.block - damageTaken);
        this.healthPoints = Math.max(0, this.healthPoints - unblockedDamage);
    }

    // Combat Methods
    public void gainBlock(int blockAmount) {
        this.block += blockAmount;
        GameEventBus.getGameEventBus().notifyEntityDefense(name, blockAmount);
    }

    public void gainHealth(int healAmount) {
        this.healthPoints += healAmount;
        if (this.healthPoints > maxHealthPoints) { this.healthPoints = maxHealthPoints; }
        GameEventBus.getGameEventBus().notifyEntityHeal(name, healAmount);
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
        if (deck.size() < drawCount) {
            deck.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(deck);
            GameEventBus.getGameEventBus().notifyDeckShuffled();
        }
        for (int i = 0; i < drawCount && !deck.isEmpty(); i++) {
            Card drawn = deck.removeFirst();
            hand.add(drawn);
            GameEventBus.getGameEventBus().notifyCardDrawn(drawn);
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
    public List<String> getNotifications() {
        List<String> retNotifications = notifications;
        notifications = new ArrayList<>();
        return retNotifications;
    }
    abstract public String getDescription();

    public String evalMove(Card selectedCard, Entity target) {
        return "Cannot play cards rn. Try passing instead.";
    }

    public void pass() {
    }

    @Override
    public String toString() {
        return getName() + "(" + getHP() + ")";
    }

    public boolean isSlayer() {
        return false;
    }

    abstract public void decideIntent();

    abstract public void executeIntent(Slayer slayer);
}