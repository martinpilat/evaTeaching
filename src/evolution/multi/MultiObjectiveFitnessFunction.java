package evolution.multi;

import evolution.FitnessFunction;
import evolution.individuals.Individual;
import evolution.individuals.MultiRealIndividual;
import evolution.multi.functions.MultiObjectiveFunction;

/**
 * Implementation of the fitness function in the multi-objective case.
 */
public class MultiObjectiveFitnessFunction implements FitnessFunction {

    MultiObjectiveFunction mof;

    public MultiObjectiveFitnessFunction(MultiObjectiveFunction mof) {
        this.mof = mof;
    }

    @Override
    public double evaluate(Individual ind) {

        MultiRealIndividual mri = (MultiRealIndividual)ind;
        mri.setMultiObjectiveValues(mof.evaluate(mri.toDoubleArray()));

        return 0; //dummy, not used in multi-objective case
    }
}
