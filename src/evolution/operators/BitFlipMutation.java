package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.BooleanIndividual;

/**
 * @author Martin Pilat
 */
public class BitFlipMutation implements Operator {

    double mutationProbability;
    double bitFlipProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    public BitFlipMutation(double mutationProbability, double bitFlipProbability) {
        this.mutationProbability = mutationProbability;
        this.bitFlipProbability = bitFlipProbability;
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            BooleanIndividual p1 = (BooleanIndividual) parents.get(rng.nextInt(size));
            BooleanIndividual o1 = (BooleanIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbability) {
                for (int j = 0; j < o1.length(); j++) {
                    if (rng.nextDouble() < bitFlipProbability) {
                        boolean b = o1.toBooleanArray()[j];
                        o1.set(j, !b);
                    }
                }
            }

            offspring.add(o1);
        }
    }

}
