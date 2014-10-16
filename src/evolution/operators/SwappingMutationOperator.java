package org.pikater.core.utilities.evolution.operators;

import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.ArrayIndividual;

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
                    double v1 = (double)o1.get(r1);  // Object v1 = o1.get(r1);
                    double v2 = (double)o1.get(r2);  // Object v2 = o1.get(r2);

                    o1.set(r1, new DoubleValue(v2));
                    o1.set(r2, new DoubleValue(v1));
                }
            }

            offspring.add(o1);
        }
    }
}
