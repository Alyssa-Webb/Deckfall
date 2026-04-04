package deckfall.DataClasses;

public class SideEffect {
    public final SideEffectType sideEffectType;
    public final String gameData;
    //this is very much a default implementation

    public SideEffect(SideEffectType sideEffectType, String gameData){
        this.sideEffectType = sideEffectType;
        this.gameData = gameData;
    }
}
