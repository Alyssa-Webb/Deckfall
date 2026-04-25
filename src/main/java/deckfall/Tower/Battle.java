package deckfall.Tower;

import deckfall.Entity.Entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import deckfall.Entity.Slayer;

public class Battle implements Iterable<Entity> {
    private final LinkedList<Entity> entities;

    public Battle(LinkedList<Entity> foes) {
        this.entities = new LinkedList<>(foes);
    }

    // Battle is over when all non-player entities are dead
    public boolean battleOver() {
        return entities.stream()
                .filter(e -> !(e instanceof Slayer))
                .noneMatch(Entity::isAlive);
    }

    public boolean isPlayerDead() {
        return entities.stream()
                .filter(entity -> entity instanceof Slayer)
                .noneMatch(Entity::isAlive);
    }

    // Returns only living non-player entities
    public List<Entity> getActiveEnemies() {
        return entities.stream()
                .filter(entity -> !(entity instanceof Slayer) && entity.isAlive())
                .toList();
    }

    public List<Entity> getActivePlayers() {
        return entities.stream()
                .filter(entity -> entity instanceof Slayer && entity.isAlive())
                .toList();
    }

    // Returns all entities
    public List<Entity> getEntities() { return entities.stream().toList(); }


    // Player always goes first
    public void addPlayerCharacter(Entity player) {
        entities.addFirst(player);
    }

    public void addPlayerCharacter(Entity player, int position) {
        entities.add(position, player);
    }

    public Entity getNextTurn() {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.pop();
            //I dunno if it would mess anything up, but we could try only adding an entity back to the battle order if they're alive?
            entities.addLast(entity);
            if (entity.isAlive()) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public Iterator<Entity> iterator() {
        return entities.iterator();
    }

    @Override
    public void forEach(Consumer<? super Entity> action) {
        entities.forEach(action);
    }
}