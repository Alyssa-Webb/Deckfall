package deckfall.Factory;

import deckfall.Card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardFactory {
    public static Card createCard(CardType type) {
        return switch (type) {
            case SLASH -> new SlashCard();
            case SHIELD -> new ShieldCard();
            case HEAL -> new RestoreHealthCard();
        };
    }

    // Testing purposes
    public static List<Card> createFixedDeck() {
        return new ArrayList<>(List.of(
                new SlashCard("Simple Slash", 1, "Deal 4 points of slash damage to one enemy", 4),
                new SlashCard("Simple Slash", 1, "Deal 4 points of slash damage to one enemy", 4),
                new SlashCard("Throw Down", 0, "Your final stand, dealing 3 damage.", 3),
                new RestoreHealthCard("Moon's Blessing", 3, "Receive the blessing of the moon, and receive up to 7 points of health", 7),
                new ShieldCard("Block", 1, "Block up to 5 points of incoming damage", 5)
        ));
    }

    public static List<Card> createFullDeck() {
        return new ArrayList<>(List.of(
                // Slash Cards
                new SlashCard("Simple Slash", 1, "Deal 4 points of slash damage to one enemy.", 4),
                new SlashCard("Simple Slash", 1, "Deal 4 points of slash damage to one enemy.", 4),
                new SlashCard("Throw Down", 0, "Your final stand, dealing 3 damage.", 3),
                new SlashCard("Cleave", 2, "A heavy swing that deals 10 damage.", 10),
                new SlashCard("Cleave", 2, "A heavy swing that deals 10 damage.", 10),
                new SlashCard("Quick Slash", 0, "A cheap hit that deals 3 damage.", 3),

                // Shield Cards
                new ShieldCard("Block", 1, "Block up to 5 points of incoming damage.", 5),
                new ShieldCard("Block", 1, "Block up to 5 points of incoming damage.", 5),
                new ShieldCard("Block", 1, "Block up to 5 points of incoming damage.", 5),
                new ShieldCard("Iron Wall", 2, "Gain 9 block this turn.", 9),
                new ShieldCard("Brace", 1, "Gain 6 block this turn.", 6),

                // Heal Cards
                new RestoreHealthCard("Moon's Blessing", 3, "Receive the blessing of the moon and heal up to 7 HP.", 7),
                new RestoreHealthCard("First Aid", 1, "Patch your wounds for 5 HP.", 5),
                new RestoreHealthCard("First Aid", 1, "Patch your wounds for 5 HP.", 5),
                new RestoreHealthCard("First Aid", 1, "Patch your wounds for 4 HP.", 4)
        ));
    }
}
