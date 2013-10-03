package evolution;

/**
 * A simple instance of the FitnessEvaluator class. It takes as a parameter
 * a fitness function and uses it to assign the fitness to all individuals
 * in the population.
 *
 * @author Martin Pilat
 */
public class SimpleEvaluator implements FitnessEvaluator {

    FitnessFunction fitness;

    /**
     * Creates a SimpleEvaluator which uses the fitness specified and assigns
     * the value provided by this fitness to all the individuals in the population.
     *
     * @param fitness The fitness which shall be used during the evaluation.
     */

    public SimpleEvaluator(FitnessFunction fitness) {
        this.fitness = fitness;
    }

    public void evaluate(Population pop) {

        for (int i = 0; i < pop.getPopulationSize(); i++) {
            pop.get(i).setFitnessValue(fitness.evaluate(pop.get(i)));
        }

    }

}
