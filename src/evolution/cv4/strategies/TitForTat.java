package evolution.cv4.strategies;

import evolution.cv4.Result;
import evolution.cv4.Strategy;

public class TitForTat extends Strategy {

    Result lastMove = null;

    @Override
    public String authorName() {
        return "Martin Pilat";
    }

    @Override
    public String getName() {
        return "Tit for Tat";
    }

    @Override
    public Move nextMove() {
        if (lastMove == null)
            return Move.COOPERATE;
        return lastMove.getOponentsMove();
    }

    @Override
    public void reward(Result res) {
        lastMove = res;
    }

    @Override
    public void reset() {
        lastMove = null;
    }

}
