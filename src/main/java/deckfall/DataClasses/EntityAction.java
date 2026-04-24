package deckfall.DataClasses;

import deckfall.Card.Card;
import deckfall.Entity.Entity;
import deckfall.Game.MoveTypes;

public class EntityAction {
    private static final String DEFAULT_ACTION = "pass";
    private static final MoveTypes DEFAULT_ACTION_ENUM = MoveTypes.PASS;

    //this covers like EVERY base I could think of. What we actually use in practice will differ from what's available.
    private Entity target;
    private String targetID;
    private String action = DEFAULT_ACTION;
    private MoveTypes action_enum = DEFAULT_ACTION_ENUM;
    private Card selectedCard;
    private String cardID;
    private String playerName;

    public EntityAction setTarget(Entity target){
        this.target = target;
        return this;
    }
    public Entity getTarget() {
        return target;
    }

    public EntityAction setTargetID(String targetID) {
        this.targetID = targetID;
        return this;
    }
    public String getTargetID() {
        return targetID;
    }

    public EntityAction setAction(String action){
        this.action = action;
        return this;
    }
    public String getAction() {
        return action;
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

    public EntityAction setCardID(String cardID) {
        this.cardID = cardID;
        return this;
    }
    public String getCardID() {
        return cardID;
    }

    public EntityAction setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public String getPlayerName() {
        return playerName;
    }
}
