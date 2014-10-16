package evolution;

/**
 * Simple replacement which merges the parent and offspring populations to create the merged one.
 *
 * @author Martin Pilat
 */

public class MergingReplacement implements Replacement{
    @Override
    public Population replace(Population parents, Population offspring) {
        Population merged = new Population();
        merged.addAll((Population)parents.clone());
        merged.addAll((Population)offspring.clone());

        return merged;
    }
}
