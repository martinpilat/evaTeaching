package evolution.multi;

import evolution.Population;
import evolution.individuals.Individual;
import evolution.individuals.MultiRealIndividual;
import evolution.selectors.Selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Implementation of the NSGA-II selection.
 */

public class NSGA2Selector implements Selector {

    @Override
    public void select(int howMany, Population from, Population to) {
        MultiObjectiveUtils.assignFrontAndSsc(from);

        ArrayList<Individual> inds = new ArrayList<Individual>();

        for(int i = 0; i < from.getPopulationSize(); i++) {
            inds.add(from.get(i));
        }

        Collections.sort(inds, new NSGA2Comparator());

        for (int i = 0; i < howMany; i++) {
            to.add(inds.get(i));
        }
    }

    /**
     * Compares the individuals based on the front number (lower is better) and the crowding distance (larger is better).
     */

    class NSGA2Comparator implements Comparator<Individual> {

        @Override
        public int compare(Individual o1, Individual o2) {
            MultiRealIndividual mri1 = (MultiRealIndividual)o1;
            MultiRealIndividual mri2 = (MultiRealIndividual)o2;

            if (mri1.getFront() < mri2.getFront()) {
                return -1;
            }
            if (mri1.getFront() > mri2.getFront()) {
                return 1;
            }
            if (mri1.getSsc() > mri2.getSsc()) {
                return -1;
            }
            if (mri1.getSsc() < mri2.getSsc()) {
                return 1;
            }
            return 0;
        }

    }
}
