package deckfall.Factory;

import deckfall.Card.*;
import deckfall.Entity.Slayer;


public class SlayerFactory {

    public static Slayer createSlayer(String slayerName) {
        Slayer slayer = new Slayer(slayerName);
        buildStartingDeck(slayer);
        return slayer;
    }

    public static Slayer createSlayer(String slayerName, int healthPoints) {
        Slayer slayer = new Slayer(slayerName, healthPoints);
        buildStartingDeck(slayer);
        return slayer;
    }

    private static void buildStartingDeck(Slayer slayer) {
        slayer.addToDeck(new SlashCard());
        slayer.addToDeck(new SlashCard());
        slayer.addToDeck(new SlashCard());
        slayer.addToDeck(new ShieldCard());
        slayer.addToDeck(new ShieldCard());
    }
}