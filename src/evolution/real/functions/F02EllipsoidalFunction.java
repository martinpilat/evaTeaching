package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F02EllipsoidalFunction extends RealFunction {

    public F02EllipsoidalFunction(int D) {
        super(D);
    }

    @Override
    public void reinit() {
        initXopt();
        double g1 = RandomNumberGenerator.getInstance().nextGaussian();
        double g2 = RandomNumberGenerator.getInstance().nextGaussian();
        fopt = Math.min(1000.0, Math.max(-1000.0, (Math.round(100.0 * 100.0 * g1 / g2) / 100.0)));
    }

    @Override
    public double value(double[] x) {
        double[] z = Tosz(minus(x, xopt));

        double sum = 0;
        for (int i = 0; i < D; i++) {
            sum += Math.pow(10, 6.0 * i / (D - 1)) * z[i] * z[i];
        }

        return sum + fopt;
    }

}
