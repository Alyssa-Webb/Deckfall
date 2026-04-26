package deckfall.DataClasses;

import deckfall.Card.Card;
import deckfall.Entity.Entity;
import deckfall.Game.MoveTypes;

public class EntityAction {
    private static final MoveTypes DEFAULT_ACTION_ENUM = MoveTypes.PASS;

    private Entity target;
    private MoveTypes action_enum = DEFAULT_ACTION_ENUM;
    private Card selectedCard;

    public EntityAction setTarget(Entity target){
        this.target = target;
        return this;
    }
    public Entity getTarget() {
        return target;
    }

    public EntityAction setSelectedCard(Card selectedCard){
        this.selectedCard = selectedCard;
        return this;
    }
    public Card getSelectedCard() {
        return selectedCard;
    }

    public EntityAction setAction_enum(MoveTypes move) {
        action_enum = move;
        return this;
    }
    public MoveTypes getAction_enum() {
        return action_enum;
    }
}
