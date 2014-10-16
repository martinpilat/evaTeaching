package evolution;

/**
 * Interface for the evaluation of all individuals in the population at once.
 *
 * @author Martin Pilat
 */
public interface FitnessEvaluator {

    /**
     * Evaluates the whole population.
     *
     * Must assign fitness to all individuals in the population.
     *
     * @param pop The population to evaluate.
     */

    public void evaluate(Population pop);


}
