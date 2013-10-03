package evolution.cv5.simulation;

import evolution.Population;
import evolution.individuals.Individual;
import evolution.operators.Operator;

public class DummyOperator implements Operator {

    public void operate(Population parents, Population offspring) {
        for (int i = 0; i < parents.getPopulationSize(); i++) {
            offspring.add((Individual) parents.get(i).clone());
        }
    }


}
