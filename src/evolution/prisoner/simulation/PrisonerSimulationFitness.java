package evolution.prisoner.simulation;

import evolution.FitnessFunction;
import evolution.prisoner.Result;
import evolution.prisoner.Strategy;
import evolution.prisoner.Strategy.Move;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;

import java.util.ArrayList;
import java.util.Random;

public class PrisonerSimulationFitness implements FitnessFunction {
    ArrayList<Strategy> strategies;
    Random rnd = new Random();
    int maxEncounters;

    public PrisonerSimulationFitness(ArrayList<Strategy> strategies, int maxEncounters) {
        this.strategies = strategies;
        this.maxEncounters = maxEncounters;
    }

    public static Move[] toMovesArray(Individual ind) {

        IntegerIndividual iind = (IntegerIndividual) ind;

        Move[] moves = new Move[iind.length()];

        for (int i = 0; i < iind.length(); i++) {
            moves[i] = Move.values()[(Integer) iind.get(i)];
        }

        return moves;
    }


    public double evaluate(Individual ind) {

        IntegerIndividual iind = (IntegerIndividual) ind;
        Strategy es = strategies.get((Integer) iind.get(0));

        int score = 0;

        for (int i = 0; i < maxEncounters; i++) {
            Strategy s = strategies.get(rnd.nextInt(strategies.size()));

            int sc1 = 0;
            int sc2 = 0;

            String str1 = "";
            String str2 = "";

            int iters = 150 + rnd.nextInt(100);

            for (int m = 0; m < iters; m++) {

                Move s1Move = null;
                Move s2Move = null;
                try {
                    s1Move = es.nextMove();

                } catch (Exception e) {
                    sc1 = 0;
                }
                try {
                    s2Move = s.nextMove();
                } catch (Exception e) {
                    sc2 = 0;
                }

                str1 += (s1Move == null) ? "E" : s1Move.getLabel();
                str2 += (s2Move == null) ? "E" : s2Move.getLabel();

                Result r1 = new Result(s1Move, s2Move);
                Result r2 = new Result(s2Move, s1Move);

                sc1 += r1.getMyScore();
                sc2 += r2.getMyScore();

                try {
                    es.reward(r1);
                } catch (Exception e) {
                    sc1 = 0;
                }
                try {
                    s.reward(r2);
                } catch (Exception e) {
                    sc2 = 0;
                }

            }
            score += sc1;

        }
        iind.setObjectiveValue(score);
        return score;
    }
}
