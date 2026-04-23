package deckfall.Factory;

import deckfall.Entity.Slayer;


public class SlayerFactory {

    public static Slayer createSlayer(String slayerName) {
        return new Slayer(slayerName);
    }

    public static Slayer createSlayer(String slayerName, int healthPoints) {
        return new Slayer(slayerName, healthPoints);
    }
}