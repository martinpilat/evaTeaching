package org.pikater.core.utilities.evolution.operators;

import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.RealIndividual;

/**
 * Performs the polynomial mutation as described in:
 * 
 * Deb, Kalyanmoy and Mayank Goyal (1996). “A combined genetic adaptive search (GeneAS) for
 * engineering design.” In: Computer Science and Informatics 26.4, pp. 30–45
 * 
 * @author Martin Pilat
 *
 */

public class PolynomialMutationOperator implements Operator {

    private double mutRate;
    
    private static final double ETA_M = 100;

    /**
     * Constructor, sets the probability of mutation
     * 
     * @param mutationRate the probability of mutation
     */
    
    public PolynomialMutationOperator(double mutationRate) {
        mutRate = mutationRate;

    }

    public void operate(Population parents, Population offspring) {
        for (int i = 0; i < parents.getPopulationSize(); i++) {
            Individual p = parents.get(i);
            RealIndividual o = (RealIndividual) p.clone();

            if (RandomNumberGenerator.getInstance().nextDouble() < mutRate) {
                mutate(o);
            }

            offspring.add(o);
        }
    }

    /**
     * Performs the mutation itself.
     * 
     * @param ch the individual to mutate, is changed in the method
     */
    private void mutate(RealIndividual ch) {
        for (int i = 0; i < ch.length(); i++) {
            double y = (Double)ch.get(i);
            if (RandomNumberGenerator.getInstance().nextDouble() > mutRate) {
                continue;
            }
            double yLow = ch.getMin();
            double yHi = ch.getMax();
            double delta1 = (y - yLow) / (yHi - yLow);
            double delta2 = (yHi - y) / (yHi - yLow);
            double rnd = RandomNumberGenerator.getInstance().nextDouble();
            double mut_pow = 1.0 / (ETA_M + 1.0);
            double deltaq;
            if (rnd <= 0.5) {
                double xy = 1.0 - delta1;
                double val = 2.0 * rnd + (1.0 - 2.0 * rnd) * Math.pow(xy, ETA_M + 1.0);
                deltaq = Math.pow(val, mut_pow) - 1.0;
            } else {
                double xy = 1.0 - delta2;
                double val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * Math.pow(xy, ETA_M + 1.0);
                deltaq = 1.0 - (Math.pow(val, mut_pow));
            }
            y = y + deltaq * (yHi - yLow);
            if (y < yLow) {
                y = yLow;
            }
            if (y > yHi) {
                y = yHi;
            }
            ch.set(i, new DoubleValue(y) );
        }
    }
}
