package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.ArrayIndividual;

/**
 * A mutation which swaps the values on different positions in a single individual.
 * 
 * @author Martin Pilat
 */
public class SwappingMutationOperator implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    /**
     * Constructor, sets the probabilities
     * @param mutationProbability the probability of mutating an individual
     * @param geneChangeProbability the percentage of genes which will be swapped in a mutated individual
     */

    public SwappingMutationOperator(double mutationProbability, double geneChangeProbability) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            ArrayIndividual p1 = (ArrayIndividual) parents.get(i);
            ArrayIndividual o1 = (ArrayIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbability) {
                for (int j = 0; j < geneChangeProbability * p1.length(); j++) {
                    int r1 = RandomNumberGenerator.getInstance().nextInt(p1.length());
                    int r2 = RandomNumberGenerator.getInstance().nextInt(p1.length());
                    Object v1 = o1.get(r1);
                    Object v2 = o1.get(r2);

                    o1.set(r1, v2);
                    o1.set(r2, v1);
                }
            }

            offspring.add(o1);
        }
    }
}
