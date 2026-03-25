package deckfall.Controller;

import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.PlayResult;

public interface UserInputListener {
    public void UserActionPerformed(EntityAction action);
}
