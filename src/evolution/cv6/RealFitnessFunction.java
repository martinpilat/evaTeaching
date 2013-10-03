package evolution.cv6;

import evolution.FitnessFunction;
import evolution.individuals.Individual;
import evolution.individuals.RealIndividual;

public class RealFitnessFunction implements FitnessFunction {

    private static final long serialVersionUID = 2274261765244005248L;
    evolution.cv6.RealFunction rf;

    public RealFitnessFunction(RealFunction rf) {
        this.rf = rf;
    }

    public double evaluate(Individual aSubject) {

        RealIndividual ri = (RealIndividual) aSubject;
        //do not change these two lines
        double value = rf.value(ri.toDoubleArray());
        ri.setObjectiveValue(value);

        return value;
    }
}
