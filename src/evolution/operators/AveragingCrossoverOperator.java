package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.RealIndividual;


/**
 * An arithmetic crossover operator. Computes weighted sum of the two vectors, the weight
 * is chosen randomly and is different for each coordinate.
 *
 * @author Martin Pilat
 */
public class AveragingCrossoverOperator implements Operator {

    double xOverProb = 0;
    RandomNumberGenerator rng = null;

    public AveragingCrossoverOperator(double prob) {
        xOverProb = prob;
        rng = RandomNumberGenerator.getInstance();
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        parents.shuffle();

        for (int i = 0; i < size / 2; i++) {
            RealIndividual p1 = (RealIndividual) parents.get(2 * i);
            RealIndividual p2 = (RealIndividual) parents.get(2 * i + 1);

            RealIndividual o1 = (RealIndividual) p1.clone();
            RealIndividual o2 = (RealIndividual) p2.clone();

            if (rng.nextDouble() < xOverProb) {

                double weight = 0.5 * rng.nextDouble();

                int point = rng.nextInt(p1.length());

                for (int j = point; j < p1.length(); j++) {
                    double d1 = (Double) o1.get(j);
                    double d2 = (Double) o2.get(j);

                    o1.set(j, weight * d1 + (1 - weight) * d2);
                    o2.set(j, (1 - weight) * d1 + weight * d2);

                }

            }

            offspring.add(o1);
            offspring.add(o2);
        }

    }
}
