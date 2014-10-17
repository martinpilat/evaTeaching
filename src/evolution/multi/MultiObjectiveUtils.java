package evolution.multi;

import evolution.Population;
import evolution.individuals.Individual;
import evolution.individuals.MultiRealIndividual;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Utilities useful for multi-objective optimization.
 *
 * @author Martin Pilat
 */
public class MultiObjectiveUtils {


    /**
     * Checks the dominance between individuals
     *
     * @param i1 First individual
     * @param i2 Second individual
     * @return *true* if the first individual dominates the second individual, *false* otherwise
     */
    public static boolean dominates(Individual i1, Individual i2) {
        MultiRealIndividual mri1 = (MultiRealIndividual)i1;
        MultiRealIndividual mri2 = (MultiRealIndividual)i2;

        double[] mri1Values = mri1.getMultiObjectiveValues();
        double[] mri2Values = mri2.getMultiObjectiveValues();

        boolean strong = false;
        for (int i = 0; i < mri1Values.length; i++) {
            if (mri1Values[i] < mri2Values[i]) {
            	strong = true;
            }
            if (mri1Values[i] > mri2Values[i]) {
                return false;
                }
            }
        return strong;
    }

    /**
     * Extracts the first non-dominated front from the population.
     *
     * @param pop The population
     * @return The first non-dominated front from *pop*, *null* if *pop* is empty.
     */

    public static ArrayList<Individual> getNonDominatedFront(ArrayList<Individual> pop) {

        if (pop.isEmpty()) {
            return null;
        }

        ArrayList<Individual> front = new ArrayList<Individual>();

        for (int i = 0; i < pop.size(); i++) {
            boolean dominated = false;
            for (int j = 0; j < pop.size(); j++) {
                if (i == j)
                    continue;
                if (dominates(pop.get(j), pop.get(i))) {
                    dominated = true;
                }
            }
            if (!dominated)
                front.add(pop.get(i));
        }

        return front;
    }

    /**
     * Assigns the front number to each individual in the population
     *
     * @param pop The population
     */


    public static void assignFrontNumbers(Population pop) {

        ArrayList<Individual> inds = new ArrayList<Individual>();

        for (int i = 0; i < pop.getPopulationSize(); i++) {
            inds.add(pop.get(i));
        }

        ArrayList<Individual> front = getNonDominatedFront(inds);
        double frontNumber = 1.0;
        while (front != null) {
            inds.removeAll(front);
            for (Individual i: front) {
                MultiRealIndividual mri = (MultiRealIndividual)i;
                mri.setFront(frontNumber);
            }
            front = getNonDominatedFront(inds);
            frontNumber++;
        }

    }

    /**
     * Assigns the crowding distance to each individual in the front.
     *
     * Assumes all the individuals in the input list are mutually non-dominated.
     * WARNING: Works only for bi-objective problems.
     *
     * @param front The front
     */
    public static void assignCrowdingDistance(ArrayList<Individual> front) {

        ArrayList<MultiRealIndividual> mris = new ArrayList<MultiRealIndividual>();

        for (Individual i : front) {
            mris.add((MultiRealIndividual)i);
        }

        Collections.sort(mris, new ObjectiveValueComparator(0));

        mris.get(0).setSsc(Double.MAX_VALUE);
        mris.get(mris.size()-1).setSsc(Double.MAX_VALUE);

        for (int i = 1; i < mris.size() - 1; i++) {
            double ssc = mris.get(i+1).getMultiObjectiveValues()[0] - mris.get(i-1).getMultiObjectiveValues()[0];
            ssc += mris.get(i-1).getMultiObjectiveValues()[1] - mris.get(i+1).getMultiObjectiveValues()[1];
            mris.get(i).setSsc(ssc);
        }

    }

    /**
     * Assigns both the front number and the crowding distance to all the individuals in the population.
     *
     * WARNING: Works only for bi-objective problems.
     *
     * @param pop The population
     */

    public static void assignFrontAndSsc(Population pop) {

        ArrayList<Individual> inds = new ArrayList<Individual>();

        for (int i = 0; i < pop.getPopulationSize(); i++) {
            inds.add(pop.get(i));
        }

        ArrayList<Individual> front = getNonDominatedFront(inds);
        double frontNumber = 1.0;
        while (front != null) {
            inds.removeAll(front);
            for (Individual i: front) {
                MultiRealIndividual mri = (MultiRealIndividual)i;
                mri.setFront(frontNumber);
            }
            assignCrowdingDistance(front);
            front = getNonDominatedFront(inds);
            frontNumber++;
        }

    }

    /**
     * Calculates the hypervolume of the population given the reference point
     *
     * WARNING: Works only for bi-objective problems.
     *
     * @param pop the input population
     * @param reference the reference point
     * @return the hypervolume of the population {@code pop} w.r.t to the reference point {@code reference}
     */

    public static double calculateHypervolume(Population pop, double[] reference) {

        ArrayList<Individual> inds = new ArrayList<Individual>();

        for (int i = 0; i < pop.getPopulationSize(); i++) {
            inds.add(pop.get(i));
        }

        ArrayList<Individual> front = getNonDominatedFront(inds);

        ArrayList<MultiRealIndividual> mris = new ArrayList<MultiRealIndividual>();

        for (Individual i : front) {
            mris.add((MultiRealIndividual)i);
        }

        Collections.sort(mris, new ObjectiveValueComparator(0));

        double volume = 0.0;
        int size = mris.size();
        for (int i = 0; i < size - 1; i++) {
            volume += (reference[1] - mris.get(i).getMultiObjectiveValues()[1])*(mris.get(i+1).getMultiObjectiveValues()[0] - mris.get(i).getMultiObjectiveValues()[0]);
        }
        volume += (reference[1] - mris.get(size - 1).getMultiObjectiveValues()[1])*(reference[0] - mris.get(size - 1).getMultiObjectiveValues()[0]);

        return volume;
    }


    /**
     * Prints the objective values of the individual.
     *
     * WARNING: Works only for bi-objective problems.
     *
     * @param mri The individual.
     * @param bw OutputStreamWriter where to print the individual.
     * @throws IOException
     */
    public static void printIndividual(MultiRealIndividual mri, OutputStreamWriter bw) throws IOException {

        bw.write(mri.getMultiObjectiveValues()[0] + " " + mri.getMultiObjectiveValues()[1] + "\n");

    }

    /**
     * Compares two individuals based on one of the objective values. Used for sorting during some of the calculations.
     */

    static class ObjectiveValueComparator implements Comparator<MultiRealIndividual> {

        int index = 0;

        /**
         * Constructor.
         *
         * @param index The index of the objective value used for comparison.
         */

        public ObjectiveValueComparator(int index) {
            this.index = index;
        }

        @Override
        public int compare(MultiRealIndividual o1, MultiRealIndividual o2) {
            return Double.compare(o1.getMultiObjectiveValues()[index], o2.getMultiObjectiveValues()[index]);
        }
    }
}
