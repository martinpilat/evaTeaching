package evolution.prisoner.strategies;

import evolution.prisoner.Result;
import evolution.prisoner.Strategy;

public class Random extends Strategy {

    java.util.Random rnd = new java.util.Random();

    @Override
    public Move nextMove() {
        return Move.values()[rnd.nextInt(2)];
    }

    @Override
    public void reward(Result res) {
    }

    @Override
    public String authorName() {
        return "Martin Pilat";
    }

    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }


}
