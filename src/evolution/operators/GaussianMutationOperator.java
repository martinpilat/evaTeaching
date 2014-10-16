package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.RealIndividual;

/**
 * Performs the Gaussian mutation.
 * 
 * Goes through the individual encoded as a vector of real number and add a random number from 
 * the normal distribution with a given standard deviation to each of the genes with a given 
 * probability. 
 * 
 * @author Martin Pilat
 */
public class GaussianMutationOperator implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();
    double sigma = 1.0;

    /**
     * Constructor, sets the parameters of the mutation
     * @param mutationProbability probability of mutating an individual
     * @param geneChangeProbability probability of changing a gene
     */
    public GaussianMutationOperator(double mutationProbability, double geneChangeProbability) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
    }

    /**
     * Constructor, sets the parameters of the mutation
     * @param mutationProbability probability of mutating an individual
     * @param geneChangeProbability probability of changing a gene
     * @param sigma the standard deviation of the Gaussian mutation 
     */
    public GaussianMutationOperator(double mutationProbability, double geneChangeProbability, double sigma) {
        this(mutationProbability, geneChangeProbability);
        this.sigma = sigma;
    }

    /**
     * Sets the standard deviation of the Gaussian mutation. Can be used to change it adaptively during the 
     * run of the evolution.
     * @param sigma the new standard deviation
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            RealIndividual p1 = (RealIndividual) parents.get(i);
            RealIndividual o1 = (RealIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbability) {
                for (int j = 0; j < o1.length(); j++) {
                    if (rng.nextDouble() < geneChangeProbability) {
                        o1.set(j, ((Double) o1.get(j)) + sigma * RandomNumberGenerator.getInstance().nextGaussian());
                    }
                }
            }

            offspring.add(o1);
        }
    }

}
