package org.pikater.core.utilities.evolution.selectors;

import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.Individual;

/**
 * Implements the tournament selection. Two individuals are chosen randomly (uniformly) from the population, 
 * their fitness values are compared and the one with better fitness is selected with 80% probability.
 * 
 * @author Martin Pilat
 */
public class TournamentSelector implements Selector{

    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    @Override
    public void select(int howMany, Population from, Population to) {

        for (int i = 0; i < howMany; i++) {
            int i1 = rng.nextInt(from.getPopulationSize());
            int i2 = rng.nextInt(from.getPopulationSize());

            if ((from.get(i1).getFitnessValue() < from.get(i2).getFitnessValue()) && rng.nextDouble() < 0.8) {
                to.add((Individual)from.get(i1).clone());
            }
            else {
                to.add((Individual)from.get(i2).clone());
            }
        }
    }

    

}
