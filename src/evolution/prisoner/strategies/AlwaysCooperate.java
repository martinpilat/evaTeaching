package evolution.prisoner.strategies;

import evolution.prisoner.Result;
import evolution.prisoner.Strategy;

public class AlwaysCooperate extends Strategy {

    @Override
    public Move nextMove() {
        return Move.COOPERATE;
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
        return "Always cooperate";
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}
