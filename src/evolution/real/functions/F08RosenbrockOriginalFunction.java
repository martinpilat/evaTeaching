package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F08RosenbrockOriginalFunction extends RealFunction {

    double scale;

    public F08RosenbrockOriginalFunction(int D) {
        super(D);
        scale = Math.max(1, Math.sqrt(D) / 8.0);
    }

    @Override
    public void reinit() {
        initXopt();
        for (int i = 0; i < D; i++) {
            xopt[i] *= 0.75;    //this is in the original BBOB source
        }
        double g1 = RandomNumberGenerator.getInstance().nextGaussian();
        double g2 = RandomNumberGenerator.getInstance().nextGaussian();
        fopt = Math.min(1000.0, Math.max(-1000.0, (Math.round(100.0 * 100.0 * g1 / g2) / 100.0)));
    }

    @Override
    public double value(double[] x) {
        double[] z = minus(x, xopt);

        for (int i = 0; i < D; i++) {
            z[i] = scale * z[i] + 1;
        }

        double sum = 0;
        for (int i = 0; i < D - 1; i++) {
            sum += 100 * Math.pow((z[i] * z[i] - z[i + 1]), 2) + Math.pow(z[i] - 1, 2);
        }

        return sum + fopt;
    }

}
