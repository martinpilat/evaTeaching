package evolution;

/**
 * An implementation of replacement which merges the parent and offspring populations.
 * 
 * @author Martin Pilat
 */
public class MergingReplacement implements Replacement {

    @Override
    public Population replace(Population parents, Population offspring) {
        Population replaced = new Population();
        replaced.addAll((Population)parents.clone());
        replaced.addAll((Population)offspring.clone());
        return replaced;
    }
}
