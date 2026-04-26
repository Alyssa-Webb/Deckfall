package deckfall.Die;

import java.util.Random;

public class RandomDie implements Die {
    Random random = new Random();
    private final int maxRoll;

    public RandomDie(int maxRoll) {
        this.maxRoll = maxRoll;
    }

    @Override
    public int roll() {
        return random.nextInt(maxRoll+1);
    }
}
