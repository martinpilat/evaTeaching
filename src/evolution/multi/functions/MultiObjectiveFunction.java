package evolution.multi.functions;

/**
 * Created with IntelliJ IDEA.
 * User: marti_000
 * Date: 8.12.13
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class MultiObjectiveFunction {

    public abstract double[] evaluate(double[] x);
    public abstract double getOptimalHypervolume();
    public abstract double[] getReferencePoint();


}
