package evolution;

/**
 * The replacement decides, which of the parents and offspring are considered by
 * the environmental selection at the end of each generation.
 *
 * @author Martin Pilat
 */
public interface Replacement {

    /**
     * This function combines the parents and offspring and decides which of them
     * are considered in the environmental selection after each generation.
     *
     * @param parents   The population of parents from the beginning of the generation
     * @param offspring The population of offpring created from the parents using the operators.
     * @return The combined population of offspring and parents.
     */
    public Population replace(Population parents, Population offspring);

}
