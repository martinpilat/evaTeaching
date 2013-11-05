package evolution.prisoner.strategies;

import evolution.prisoner.Result;
import evolution.prisoner.Strategy;

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
