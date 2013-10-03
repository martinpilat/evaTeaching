package evolution.cv10;

import evolution.FitnessFunction;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;

import java.util.Vector;

public class TSPFitness implements FitnessFunction {

    private static final long serialVersionUID = -1141681050507988075L;
    Vector<Coordinates> coords;

    public TSPFitness(Vector<Coordinates> coords) {
        this.coords = coords;
    }

    @Override
    public double evaluate(Individual ind) {

        IntegerIndividual aSubject = (IntegerIndividual) ind;

        double length = 0;

        for (int i = 0; i < aSubject.length() - 1; i++) {

            int start = (Integer) aSubject.get(i);
            int end = (Integer) aSubject.get(i + 1);

            length += Coordinates.distance(coords.get(start), coords.get(end));

        }

        length += Coordinates.distance(coords.get((Integer) aSubject.get(aSubject.length() - 1)), coords.get((Integer) aSubject.get(0)));

        aSubject.setObjectiveValue(length);

        return 1.0 / length;
    }
}
