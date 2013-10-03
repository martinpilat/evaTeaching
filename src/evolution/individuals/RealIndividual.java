package evolution.individuals;

import evolution.RandomNumberGenerator;

import java.util.Arrays;

/**
 * @author Martin Pilat
 */
public class RealIndividual extends ArrayIndividual {

    int length = 0;
    double[] genes = null;
    double min = 0;
    double max = 0;

    /**
     * Creates an individual of specified length with the specified limits. The genes
     * are uninitialized.
     *
     * @param length The length of the individual.
     * @param min    The minimum value in the individual.
     * @param max    The upper limit of the value of the individual (the maximum gene will
     *               be max - 1)
     */
    public RealIndividual(int length, double min, double max) {
        this.length = length;
        genes = new double[length];
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String toString() {
        return "fitness: " + getFitnessValue() + " " + Arrays.toString(genes);
    }

    /**
     * Returns the internall array.
     *
     * @return The internal integer array.
     */
    public double[] toDoubleArray() {
        return genes;
    }

    @Override
    public Object get(int n) {
        return genes[n];
    }

    @Override
    public void set(int n, Object o) {
        Double d = (Double) o;
        d = Math.max(d, min);
        d = Math.min(d, max);
        genes[n] = d;
    }

    /**
     * Randomly initializes the individual.
     */
    @Override
    public void randomInitialization() {

        for (int i = 0; i < length; i++) {
            genes[i] = (max - min) * RandomNumberGenerator.getInstance().nextDouble() + min;
        }

    }

    @Override
    public Object clone() {

        RealIndividual newBI = (RealIndividual) super.clone();
        newBI.genes = new double[genes.length];

        System.arraycopy(genes, 0, newBI.genes, 0, genes.length);

        newBI.length = length;

        return newBI;
    }

    @Override
    public int length() {
        return genes.length;
    }

}
