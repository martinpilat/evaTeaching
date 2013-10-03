package evolution;

import evolution.individuals.Individual;

/**
 * An interface for all fitness function.
 *
 * @author Martin Pilat
 */
public interface FitnessFunction {

    /**
     * This method evaluates the fitness of specified individual.
     *
     * @param ind The individual which shall be evaluated
     * @return The fitness of the evaluated individual
     */
    public double evaluate(Individual ind);

}
