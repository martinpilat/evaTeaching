package evolution.prisoner.strategies;

import evolution.prisoner.Result;
import evolution.prisoner.Strategy;

public class AlwaysDeceive extends Strategy {

    @Override
    public String authorName() {
        return "Martin Pilat";
    }

    @Override
    public String getName() {
        return "Always deceive";
    }

    @Override
    public Move nextMove() {
        return Move.DECEIVE;
    }

    @Override
    public void reward(Result res) {
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}
