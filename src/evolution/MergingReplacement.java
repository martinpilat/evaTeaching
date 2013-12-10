package evolution;

public class MergingReplacement implements Replacement{
    @Override
    public Population replace(Population parents, Population offspring) {
        Population merged = new Population();
        merged.addAll((Population)parents.clone());
        merged.addAll((Population)offspring.clone());

        return merged;
    }
}
