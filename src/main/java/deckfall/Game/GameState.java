package deckfall.Game;

public enum GameState {
    GAME_START,                 // Game is running
    PLAYER_TURN,                // It is the Player's (Slayer) turn
    AWAITING_CONTINUE_PROMPT,   // Waiting for Player to continue to next level or battle
    GAME_WIN,                   // Game is won
    GAME_LOSS                   // Game is lost
}
