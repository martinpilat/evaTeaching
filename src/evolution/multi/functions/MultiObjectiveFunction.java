package evolution.multi.functions;

/**
 *
 * Based class for all multi objective functions.
 *
 */
public abstract class MultiObjectiveFunction {

    /**
     * Evaluate the vector *x* and returns its objective values.
     *
     * @param x The vector to evaluate
     * @return Objective values of *x*
     */

    public abstract double[] evaluate(double[] x);

    /**
     * Returns the optimal hypervolume for the given problem.
     *
     * Assumes infinite population size.
     *
     * @return The optimal hypervolume
     */

    public abstract double getOptimalHypervolume();

    /**
     * Returns the reference point for the computation of the hypervolume.
     *
     * @return The reference point
     */

    public abstract double[] getReferencePoint();


}
