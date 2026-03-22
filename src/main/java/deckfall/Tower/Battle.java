package deckfall.Tower;

import deckfall.Entity.Entity;

import java.util.LinkedList;
import java.util.List;

public class Battle {
    private final LinkedList<Entity> entities;

    public Battle(LinkedList<Entity> foes) {
        this.entities = foes;
    }

    public boolean battleOver() {
        return entities.stream().noneMatch(Entity::isAlive);
    }

    public List<Entity> getFoes() {
        return entities;
    }

    public void addPlayerCharacter(Entity player, int position){
        entities.add(position, player);
    }

    public Entity getNextPlayer() {
        Entity entity = entities.pop();
        entities.addLast(entity);
        return entity;
    }
}
