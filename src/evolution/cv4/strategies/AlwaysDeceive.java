package evolution.cv4.strategies;

import evolution.cv4.Result;
import evolution.cv4.Strategy;

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
