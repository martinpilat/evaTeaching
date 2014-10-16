package evolution.selectors;

import evolution.Population;

/**
 * Interface for all selections.
 * 
 * @author Martin Pilat
 */
public interface Selector {

    /**
     * Performs the selection. Selects {@code howMany} individuals from the {@from} population and 
     * places them to {@to} population.
     * 
     * @param howMany number of individuals to select
     * @param from the population from which to select
     * @param to the population to place the individuals to
     */

    public void select(int howMany, Population from, Population to);

}
