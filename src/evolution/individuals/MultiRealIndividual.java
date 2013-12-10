package evolution.individuals;

public class MultiRealIndividual extends RealIndividual implements Comparable<MultiRealIndividual> {

    double[] multiObjectiveValues;
    static double hypervolume;
    double front;
    double ssc;

    /**
     * Creates an individual of specified length with the specified limits. The genes
     * are uninitialized.
     *
     * @param length The length of the individual.
     * @param min    The minimum value in the individual.
     * @param max    The upper limit of the value of the individual (the maximum gene will
     *               be max - 1)
     */
    public MultiRealIndividual(int length, double min, double max) {
        super(length, min, max);
    }

    public double[] getMultiObjectiveValues() {
        return multiObjectiveValues;
    }

    public void setMultiObjectiveValues(double[] multiObjectiveValues) {
        this.multiObjectiveValues = multiObjectiveValues;
    }

    @Override
    public double getObjectiveValue() {
        return hypervolume;
    }

    @Override
    public void setObjectiveValue(double value) {
        hypervolume = value;
    }

    public double getSsc() {
        return ssc;
    }

    public void setSsc(double ssc) {
        this.ssc = ssc;
    }

    public double getFront() {
        return front;
    }

    public void setFront(double front) {
        this.front = front;
    }

    @Override
    public int compareTo(MultiRealIndividual o) {
        if (this.getFront() < o.getFront())
            return -1;
        if (this.getFront() == o.getFront() && this.getSsc() < o.getSsc())
            return -1;
        if (this.getFront() == o.getFront() && this.getSsc() == o.getSsc()) {
            return 0;
        }
        return 1;
    }
}
