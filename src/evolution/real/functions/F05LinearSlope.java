package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F05LinearSlope extends RealFunction {

    public F05LinearSlope(int D) {
        super(D);
    }

    @Override
    public void reinit() {
        xopt = createOnePlusMinus();
        for (int i = 0; i < D; i++) {
            xopt[i] *= 5;
        }
        double g1 = RandomNumberGenerator.getInstance().nextGaussian();
        double g2 = RandomNumberGenerator.getInstance().nextGaussian();
        fopt = Math.min(1000.0, Math.max(-1000.0, (Math.round(100.0 * 100.0 * g1 / g2) / 100.0)));
    }

    @Override
    public double value(double[] x) {
        double[] z = new double[D];

        for (int i = 0; i < D; i++) {
            z[i] = x[i] * xopt[i] < 25 ? x[i] : xopt[i];
        }

        double[] s = new double[D];
        for (int i = 0; i < D; i++) {
            s[i] = Math.signum(xopt[i]) * Math.pow(10, 1.0 * i / (D - 1));
        }

        double sum = 0.0;
        for (int i = 0; i < D; i++) {
            sum += 5 * Math.abs(s[i]) - s[i] * z[i];
        }

        return sum + fopt;
    }

}
