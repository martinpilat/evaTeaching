package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.IntegerIndividual;

/**
 * A mutation for integer encoded individuals. Goes through the indivudal and generates new value from the 
 * valid interval for each of the positions with a given probability.
 *
 * @author Martin Pilat
 */
public class IntegerMutation implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    /**
     * Constructor, sets the probabilities
     * 
     * @param mutationProbability the probability of mutating an individual
     * @param geneChangeProbability the probability of changing a given gene in the mutated individual
     */

    public IntegerMutation(double mutationProbability, double geneChangeProbability) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            IntegerIndividual p1 = (IntegerIndividual) parents.get(i);
            IntegerIndividual o1 = (IntegerIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbability) {
                for (int j = 0; j < o1.length(); j++) {
                    if (rng.nextDouble() < geneChangeProbability) {
                        o1.set(j, RandomNumberGenerator.getInstance().nextInt(o1.getMax() - o1.getMin()) + o1.getMin());
                    }
                }
            }

            offspring.add(o1);
        }
    }

}
