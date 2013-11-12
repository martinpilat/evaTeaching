package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F20SchwefelFunction extends RealFunction {

    double scale;

    public F20SchwefelFunction(int D) {
        super(D);
        scale = Math.max(1, Math.sqrt(D) / 8.0);
    }

    @Override
    public void reinit() {
        double g1 = RandomNumberGenerator.getInstance().nextGaussian();
        double g2 = RandomNumberGenerator.getInstance().nextGaussian();
        fopt = Math.min(1000.0, Math.max(-1000.0, (Math.round(100.0 * 100.0 * g1 / g2) / 100.0)));
        lambda = createLambda(10);
        onePlusMinus = createOnePlusMinus();
        xopt = new double[D];
        for (int i = 0; i < D; i++) {
            xopt[i] = 4.2096874633 / 2.0 * onePlusMinus[i];
        }
    }

    @Override
    public double value(double[] x) {
        double[] xR = new double[D];
        for (int i = 0; i < D; i++) {
            xR[i] = 2 * x[i] * onePlusMinus[i];
        }

        double[] zR = new double[D];
        zR[0] = xR[0];
        for (int i = 0; i < D - 1; i++) {
            zR[i + 1] = xR[i + 1] + 0.25 * (xR[i] - xopt[i]);
        }

        double[] z = mult(lambda, minus(zR, xopt));

        for (int i = 0; i < D; i++) {
            z[i] = 100 * z[i] + xopt[i];
        }

        double sum = 0;
        for (double d : z) {
            sum += d * Math.sin(Math.sqrt(Math.abs(d)));
        }

        double[] zS = new double[D];
        for (int i = 0; i < D; i++) {
            zS[i] = z[i] / 100.0;
        }

        return -1.0 / D * sum + 4.189828872724339 + 100 * fpen(zS) + fopt;
    }

}
