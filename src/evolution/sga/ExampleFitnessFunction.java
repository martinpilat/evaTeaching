package evolution.sga;

import evolution.FitnessFunction;
import evolution.individuals.BooleanIndividual;
import evolution.individuals.Individual;

/**
 * @author Martin Pilat
 */
public class ExampleFitnessFunction implements FitnessFunction {

    public double evaluate(Individual ind) {

        BooleanIndividual bi = (BooleanIndividual) ind;
        boolean[] genes = bi.toBooleanArray();

        double fitness = 0.0;

        for (int i = 0; i < genes.length; i++) {
            if (genes[i])
                fitness += 1.0;
        }

        ind.setObjectiveValue(fitness); //nastavuje hodnotu optimalizovaneho kriteria, nemusi se (obecne) rovnat primo fitness

        return fitness;
    }

}
