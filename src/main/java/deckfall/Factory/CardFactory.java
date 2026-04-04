package deckfall.Factory;

import deckfall.Card.*;

public class CardFactory {
    public static Card createCard(CardType type) {
        return switch (type) {
            case SLASH -> new SlashCard();
            case SHIELD -> new ShieldCard();
            case HEAL -> new RestoreHealthCard();
        };
    }
}
