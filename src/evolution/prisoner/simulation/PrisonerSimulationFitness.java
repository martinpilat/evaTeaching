package evolution.prisoner.simulation;

import evolution.FitnessEvaluator;
import evolution.FitnessFunction;
import evolution.Population;
import evolution.prisoner.Result;
import evolution.prisoner.Strategy;
import evolution.prisoner.Strategy.Move;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;

import java.util.ArrayList;
import java.util.Random;

public class PrisonerSimulationFitness implements FitnessEvaluator {
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

    @Override
    public void evaluate(Population pop) {

        for (int i = 0; i < pop.getPopulationSize(); i++) {
            double fitness = evaluate(pop.get(i), pop);
            pop.get(i).setFitnessValue(fitness);
        }

    }

    public void setCores(int cores) { //ignore setting number of cores for now
    }

    public double evaluate(Individual ind, Population pop) {

        int score = 0;

        for (int i = 0; i < maxEncounters; i++) {

            IntegerIndividual op = (IntegerIndividual) pop.get(rnd.nextInt(pop.getPopulationSize()));
            IntegerIndividual iind = (IntegerIndividual) ind;
            Strategy indS = null;
            Strategy opS = null;
            try {
                indS = strategies.get((Integer) iind.get(0)).getClass().newInstance();
                opS = strategies.get((Integer) op.get(0)).getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            int sc1 = 0;
            int sc2 = 0;

            int iters = 150 + rnd.nextInt(100);

            for (int m = 0; m < iters; m++) {

                Move s1Move = null;
                Move s2Move = null;
                try {
                    s1Move = indS.nextMove();

                } catch (Exception e) {
                    sc1 = 0;
                }
                try {
                    s2Move = opS.nextMove();
                } catch (Exception e) {
                    sc2 = 0;
                }

                Result r1 = new Result(s1Move, s2Move);
                Result r2 = new Result(s2Move, s1Move);

                sc1 += r1.getMyScore();
                sc2 += r2.getMyScore();

                try {
                    indS.reward(r1);
                } catch (Exception e) {
                    sc1 = 0;
                }
                try {
                    opS.reward(r2);
                } catch (Exception e) {
                    sc2 = 0;
                }

            }

            score += sc1;

        }
        ind.setObjectiveValue(score);

        return score;
    }
}
