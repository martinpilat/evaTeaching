package evolution.multi;

import evolution.FitnessEvaluator;
import evolution.FitnessFunction;
import evolution.Population;
import evolution.individuals.Individual;
import evolution.individuals.MultiRealIndividual;
import evolution.multi.functions.MultiObjectiveFunction;

/**
 * Created with IntelliJ IDEA.
 * User: marti_000
 * Date: 8.12.13
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
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
