package evolution.sga;

import evolution.FitnessFunction;
import evolution.individuals.BooleanIndividual;
import evolution.individuals.Individual;

/**
 * @author Martin Pilat
 */
public class ExampleFitnessFunction implements FitnessFunction {

    /**
     * THis is an example fitness function

     * @param ind The individual which shall be evaluated
     * @return The number of 1s in the individual
     */

    public double evaluate(Individual ind) {

        BooleanIndividual bi = (BooleanIndividual) ind;
        boolean[] genes = bi.toBooleanArray();

        double fitness = 0.0;

        for (int i = 0; i < genes.length; i++) {
            if (genes[i])
                fitness += 1.0;
        }

        ind.setObjectiveValue(fitness); //this sets the objective value, can be different from the fitness function

        return fitness;
    }

}
