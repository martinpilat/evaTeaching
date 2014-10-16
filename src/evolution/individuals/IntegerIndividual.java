package evolution.individuals;

import evolution.RandomNumberGenerator;

import java.util.Arrays;

/**
 * An individual represented by an array of integers from a specified range.
 *
 * @author Martin Pilat
 */
public class IntegerIndividual extends ArrayIndividual {

    int length = 0;
    int[] genes = null;
    int min = 0;
    int max = 0;

    /**
     * Creates an individual of specified length with the specified limits. The genes
     * are uninitialized.
     *
     * @param length The length of the individual.
     * @param min    The minimum value in the individual.
     * @param max    The upper limit of the value of the individual (the maximum gene will
     *               be max - 1)
     */
    public IntegerIndividual(int length, int min, int max) {
        this.length = length;
        genes = new int[length];
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String toString() {
        return Arrays.toString(genes);
    }

    /**
     * Returns the internall array.
     *
     * @return The internal integer array.
     */
    public int[] toIntArray() {
        return genes;
    }

    @Override
    public Object get(int n) {
        return genes[n];
    }

    @Override
    public void set(int n, Object o) {
        genes[n] = (Integer) o;
    }

    /**
     * Randomly initializes the individual.
     */
    @Override
    public void randomInitialization() {

        for (int i = 0; i < length; i++) {
            genes[i] = RandomNumberGenerator.getInstance().nextInt(max - min) + min;
        }

    }

    @Override
    public Object clone() {

        IntegerIndividual newBI = (IntegerIndividual) super.clone();
        newBI.genes = new int[genes.length];

        System.arraycopy(genes, 0, newBI.genes, 0, genes.length);

        newBI.length = length;

        return newBI;
    }

    @Override
    public int length() {
        return genes.length;
    }


}
