package evolution.operators;

import evolution.Population;

/**
 * Interface for all genetic operators. 
 * 
 * @author Martin Pilat
 */
public interface Operator {

	/**
	 * Performs the genetic operator. Crossover should take the parents in pair from the {@code parents}
	 * population, clone them, perform the operation and put the offspring into the {@code offspring} 
	 * population.
	 * 
	 * @param parents the population of parents
	 * @param offspring the population of offspring
	 */

    public void operate(Population parents, Population offspring);

}
