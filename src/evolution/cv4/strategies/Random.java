package evolution.cv4.strategies;

import evolution.cv4.Result;
import evolution.cv4.Strategy;

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
