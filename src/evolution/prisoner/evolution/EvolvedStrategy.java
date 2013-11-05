package evolution.prisoner.evolution;

import evolution.prisoner.Result;
import evolution.prisoner.Strategy;

import java.util.ArrayList;
import java.util.List;

public class EvolvedStrategy extends Strategy {

    List<Result> results = new ArrayList<Result>();
    Move[] moves;

    public EvolvedStrategy(Move[] moves) {
        this.moves = moves;
        reset();
    }

    @Override
    public String authorName() {
        return "Charles Darwin";
    }

    @Override
    public String getName() {
        return "Evolved strategy";
    }

    @Override
    public Move nextMove() {


        Result[] lastMoves = results.subList(results.size() - 3, results.size()).toArray(new Result[0]);


        //oponent made an error (exception)
        if (lastMoves[0].getOponentsMove() == null)
            return Move.COOPERATE;
        if (lastMoves[1].getOponentsMove() == null)
            return Move.COOPERATE;
        if (lastMoves[2].getOponentsMove() == null)
            return Move.COOPERATE;

        //prevod na 6bitove cislo
        int idx = 0;
        idx += lastMoves[0].getMyMove().ordinal() << 5;
        idx += lastMoves[0].getOponentsMove().ordinal() << 4;
        idx += lastMoves[1].getMyMove().ordinal() << 3;
        idx += lastMoves[1].getOponentsMove().ordinal() << 2;
        idx += lastMoves[2].getMyMove().ordinal() << 1;
        idx += lastMoves[2].getOponentsMove().ordinal();

        return moves[idx];
    }

    @Override
    public void reset() {
        results.clear();
        Result r = new Result(Move.COOPERATE, Move.COOPERATE);

        //nez se odehraji prvni 3 tahy, tak budeme spolupracovat
        results.add(r);
        results.add(r);
        results.add(r);

    }

    @Override
    public void reward(Result res) {
        results.add(res);
    }

}
