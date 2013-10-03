package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.RealIndividual;

/**
 * @author Martin Pilat
 */
public class GaussianMutationOperator implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();
    double sigma = 1.0;

    public GaussianMutationOperator(double mutationProbability, double geneChangeProbability) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
    }

    public GaussianMutationOperator(double mutationProbability, double geneChangeProbability, double sigma) {
        this(mutationProbability, geneChangeProbability);
        this.sigma = sigma;
    }

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
