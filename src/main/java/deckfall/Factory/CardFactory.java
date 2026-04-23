package deckfall.Factory;

import deckfall.Card.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class CardFactory {
    private static final int STARTER_DECK_SIZE = 10;

    // Curated base card set used for the starter deck and random pool.
    private static final List<Supplier<Card>> BASE_CARDS = List.of(
            // Attack Cards
            () -> new SlashCard("Simple Slash", 1, "Deal 2 points of slash damage to one enemy.", 2),
            () -> new SlashCard("Throw Down", 0, "Your final stand.", 1),
            () -> new SlashCard("Cleave", 2, "A heavy swing that deals 10 damage.", 10),
            () -> new SlashCard("Quick Jab", 0, "A cheap hit that deals 3 damage.", 3),

            // Defense Cards
            () -> new ShieldCard("Block", 1, "Block up to 3 points of incoming damage.", 3),
            () -> new ShieldCard("Iron Wall", 2, "Gain 9 block this turn.", 9),
            () -> new ShieldCard("Brace", 1, "Gain 6 block this turn.", 6),

            // Heal Cards
            () -> new RestoreHealthCard("Moon's Blessing", 3, "Receive the blessing of the moon and heal up to 5 HP.", 5),
            () -> new RestoreHealthCard("First Aid", 1, "Patch your wounds for 3 HP.", 3),
            () -> new RestoreHealthCard("Second Wind", 2, "Recover 7 HP.", 7)
    );

    private static final List<Supplier<Card>> PLAYER_CARD_POOL = List.copyOf(BASE_CARDS);

    public static Card createCard(CardType type) {
        return switch (type) {
            case SLASH -> new SlashCard();
            case SHIELD -> new ShieldCard();
            case HEAL -> new RestoreHealthCard();
        };
    }

    public static List<CardType> getAllPlayerCardTypes() {
        return getAllPlayerPoolCards().stream()
                .map(Card::getType)
                .toList();
    }

    public static Card createRandomPlayerCard() {
        int randomIndex = ThreadLocalRandom.current().nextInt(PLAYER_CARD_POOL.size());
        return PLAYER_CARD_POOL.get(randomIndex).get();
    }

    public static List<Card> createRandomPlayerDeck(int deckSize) {
        List<Card> deck = new ArrayList<>();
        for (int i = 0; i < deckSize; i++) {
            deck.add(createRandomPlayerCard());
        }
        return deck;
    }

    public static List<Card> createStarterPlayerDeck() {
        List<Card> starterDeck = new ArrayList<>(getBaseCards());
        int remainingCards = Math.max(0, STARTER_DECK_SIZE - starterDeck.size());
        starterDeck.addAll(createRandomPlayerDeck(remainingCards));
        return starterDeck;
    }

    public static List<Card> getBaseCards() {
        List<Card> cards = new ArrayList<>();
        for (Supplier<Card> supplier : BASE_CARDS) {
            cards.add(supplier.get());
        }
        return cards;
    }

    public static List<Card> getAllPlayerPoolCards() {
        List<Card> cards = new ArrayList<>();
        for (Supplier<Card> supplier : PLAYER_CARD_POOL) {
            cards.add(supplier.get());
        }
        return cards;
    }
}
