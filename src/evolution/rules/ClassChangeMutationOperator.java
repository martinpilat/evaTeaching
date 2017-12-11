package evolution.rules;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.operators.Operator;

public class ClassChangeMutationOperator implements Operator {

    double mutProb;
    double mutProbPerBit;
    RandomNumberGenerator rng;
    int numClasses;

    public ClassChangeMutationOperator(double mutProb, double mutProbPerBit, int numClasses) {
        this.mutProb = mutProb;
        this.rng = RandomNumberGenerator.getInstance();
        this.numClasses = numClasses;
    }

    @Override
    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            RuleIndividual p1 = (RuleIndividual) parents.get(i);
            RuleIndividual o1 = (RuleIndividual) p1.clone();

            if (rng.nextDouble() < mutProb) {
                for (int j = 0; j < o1.length(); j++) {
                        if (rng.nextDouble() < mutProbPerBit) {
                            o1.getRules().get(j).setClassLabel(rng.nextInt(numClasses));
                        }
                }
            }

            offspring.add(o1);
        }

    }
}
