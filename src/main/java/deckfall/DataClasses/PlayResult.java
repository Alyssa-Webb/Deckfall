package deckfall.DataClasses;

public class PlayResult {
    public final boolean failure;
    public final String message;

    public PlayResult(boolean failure, String message) {
        this.failure = failure;
        this.message = message;
    }
}
