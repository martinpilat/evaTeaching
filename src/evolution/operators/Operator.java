package evolution.operators;

import evolution.Population;

/**
 * @author Martin Pilat
 */
public interface Operator {

    public void operate(Population parents, Population offspring);

}
