package evolution.selectors;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.Individual;

/**
 * @author Martin Pilat
 */
public class TournamentSelector implements Selector {

    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    public void select(int howMany, Population from, Population to) {

        for (int i = 0; i < howMany; i++) {
            int i1 = rng.nextInt(from.getPopulationSize());
            int i2 = rng.nextInt(from.getPopulationSize());

            if ((from.get(i1).getFitnessValue() > from.get(i2).getFitnessValue()) && rng.nextDouble() < 0.8)
                to.add((Individual) from.get(i1).clone());
            else
                to.add((Individual) from.get(i2).clone());
        }
    }


}
