package deckfall.DataClasses;

import deckfall.Card.Card;
import deckfall.Entity.Entity;
import deckfall.Entity.Slayer;
import deckfall.Tower.Battle;
import deckfall.Tower.Level;

import java.util.List;

public class RelevantGameData {
    private final List<Card>   cards;
    private final List<Entity> enemies;
    private final Slayer slayer;
    private final Battle currentBattle;
    private final int totalBattles;
    private final Level currentLevel;
    private final int          totalFloors;
    private final List<String> notifications;

    public RelevantGameData(
            List<Card>   cards,
            List<Entity> enemies,
            Slayer slayer,
            Battle currentBattle,
            int totalBattles,
            Level currentLevel,
            int totalFloors,
            List<String> notifications
    ) {
        // Create copies to prevent being written over (just safer)
        this.cards = List.copyOf(cards);
        this.enemies = List.copyOf(enemies);
        this.slayer = slayer;
        this.currentBattle = currentBattle;
        this.totalBattles = totalBattles;
        this.currentLevel = currentLevel;
        this.totalFloors = totalFloors;
        this.notifications = List.copyOf(notifications);
    }

    // Getters for current "turn"?
    public List<Card> getCards()         { return cards; }
    public List<Entity> getEnemies()        { return enemies; }
    public Slayer getSlayer()         { return slayer; }
    public Battle getCurrentBattle()  { return currentBattle; }
    public int getTotalBattles()   { return totalBattles; }
    public Level getCurrentLevel()   { return currentLevel; }
    public int getTotalLevels()    { return totalFloors; }
    public List<String> getNotifications()  { return notifications; }
}
