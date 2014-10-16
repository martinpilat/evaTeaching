package evolution.individuals;

/**
 * The abstract base of all individuals. Keeps track of fitness and objective values.
 *
 * @author Martin Pilat
 */
public abstract class Individual implements Cloneable {

    double fitnessValue;
    double objectiveValue;
    String logNotes;           //for logging, these will appear in the detailed log

    public String getLogNotes() {
        if (logNotes == null)
            return "";
        return logNotes;
    }

    public void setLogNotes(String logNotes) {
        this.logNotes = logNotes;
    }

    /**
     * Randomly initializes the individual.
     */
    public abstract void randomInitialization();

    /**
     * Sets the objective value
     *
     * @param objective The objective value which shall be set.
     */
    public void setObjectiveValue(double objective) {
        this.objectiveValue = objective;
    }

    /**
     * Returns the objective value of the individual.
     * @return The objective value of the individual.
     */
    public double getObjectiveValue() {
        return objectiveValue;
    }

    /**
     * Sets the fitness value of the individual.
     *
     * @param fitness The fitness value of the individual which shall be set.
     */
    public void setFitnessValue(double fitness) {
        fitnessValue = fitness;
    }

    /**
     * Returns the fitness value of the individual.
     *
     * @return The fitness value of the individual.
     */
    public double getFitnessValue() {
        if (fitnessValue == -Double.MAX_VALUE)
            throw new RuntimeException("Fitness value not evaluated");
        return fitnessValue;
    }

    /**
     * Performs a deep copy of the individual. Resets the fitness value to
     * non-evaluated.
     *
     * @return The deep copy of the individual.
     */

    @Override
    public Object clone() {

        try {
            Individual newInd = (Individual) super.clone();
            newInd.fitnessValue = -Double.MAX_VALUE;
            newInd.logNotes = null;
            return newInd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
