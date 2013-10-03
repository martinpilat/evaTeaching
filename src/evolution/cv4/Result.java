package evolution.cv4;

import evolution.cv4.Strategy.Move;

public class Result {

    int[][] rewards = {{3, 0}, {5, 1}};

    Move myMove;
    Move oponentsMove;

    public Result(Move my, Move op) {
        this.myMove = my;
        this.oponentsMove = op;
    }

    public Move getOponentsMove() {
        return oponentsMove;
    }

    public Move getMyMove() {
        return myMove;
    }

    public int getMyScore() {
        if (myMove == null)
            return 0;
        if (oponentsMove == null)
            return 5;
        return rewards[myMove.ordinal()][oponentsMove.ordinal()];
    }

    public int getOponentsScore() {
        if (oponentsMove == null)
            return 0;
        if (myMove == null)
            return 5;
        return rewards[oponentsMove.ordinal()][myMove.ordinal()];
    }

}