package evolution;

/**
 * Simple replacement as is done in the Simple Genetic Algortihm (i.e. the parents
 * are dropped).
 *
 * @author Martin Pilat
 */
public class SGAReplacement implements Replacement {

    /**
     * An implementation of replacement. Drops the parents and only considers the
     * offspring.
     *
     * @param parents   The parents from the previous population (ignored).
     * @param offspring The offspring created by the operators.
     * @return A clone of the offspring population.
     */
    public Population replace(Population parents, Population offspring) {
        return (Population) offspring.clone();
    }

}
