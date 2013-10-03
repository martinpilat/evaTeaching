package evolution.selectors;

import evolution.Population;

/**
 * @author Martin Pilat
 */
public interface Selector {

    public void select(int howMany, Population from, Population to);

}
