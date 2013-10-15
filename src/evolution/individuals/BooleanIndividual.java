package evolution.individuals;

import evolution.RandomNumberGenerator;

/**
 * Individual represented using an array of booleans.
 *
 * @author Martin Pilat
 */
public class BooleanIndividual extends ArrayIndividual {

    int length = 0;
    boolean[] genes = null;

    public BooleanIndividual(int length) {
        this.length = length;
        genes = new boolean[length];
    }

    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for(boolean b : genes) {
            if (b)
                s.append("1");
            else
                s.append("0");
        }
        s.append("]");
        return s.toString();
    }

    /**
     * Returns the internal array of booleans.
     *
     * @return The internall array.
     */
    public boolean[] toBooleanArray() {
        return genes;
    }

    @Override
    public Object get(int n) {
        return genes[n];
    }

    @Override
    public void set(int n, Object o) {
        genes[n] = (Boolean) o;
    }

    /**
     * Initializes the individual to a random sequence of boolean values.
     */
    @Override
    public void randomInitialization() {

        for (int i = 0; i < length; i++) {
            genes[i] = RandomNumberGenerator.getInstance().nextDouble() > 0.5;
        }

    }

    @Override
    public Object clone() {

        BooleanIndividual newBI = (BooleanIndividual) super.clone();
        newBI.genes = new boolean[genes.length];

        System.arraycopy(genes, 0, newBI.genes, 0, genes.length);

        newBI.length = length;

        return newBI;
    }

    @Override
    public int length() {
        return genes.length;
    }

}
