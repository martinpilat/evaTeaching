package evolution.binPacking;

import evolution.FitnessFunction;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;

import java.util.Vector;

public class HromadkyFitness implements FitnessFunction {

    Vector<Double> weights;
    int K;

    public HromadkyFitness(Vector<Double> weights, int K) {
        this.weights = weights;
        this.K = K;
    }

    public int[] getBinWeights(Individual ind) {

        int[] binWeights = new int[K];

        int[] bins = ((IntegerIndividual) ind).toIntArray();

        for (int i = 0; i < bins.length; i++) {

            binWeights[bins[i]] += weights.get(i);
        }

        return binWeights;

    }

    @Override
    public double evaluate(Individual ind) {

        int[] binWeights = getBinWeights(ind);

        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < K; i++) {
            if (binWeights[i] < min) {
                min = binWeights[i];
            }
            if (binWeights[i] > max) {
                max = binWeights[i];
            }
        }

        ind.setObjectiveValue(max - min);    // tohle doporucuji zachovat

        //sem muzete vlozit vlastni vypocet fitness, muzete samozrejme vyuzit spocitane hmotnosti hromadek

        return 1 / (max - min);
    }
}
