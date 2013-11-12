package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F06AttractiveSectorFunction extends RealFunction {

    public F06AttractiveSectorFunction(int D) {
        super(D);
    }

    @Override
    public void reinit() {
        initXopt();
        double g1 = RandomNumberGenerator.getInstance().nextGaussian();
        double g2 = RandomNumberGenerator.getInstance().nextGaussian();
        fopt = Math.min(1000.0, Math.max(-1000.0, (Math.round(100.0 * 100.0 * g1 / g2) / 100.0)));
        R = getRandomRotationMatrix();
        Q = getRandomRotationMatrix();
        lambda = createLambda(10);
    }

    @Override
    public double value(double[] x) {
        double[] z = mult(Q, mult(lambda, mult(R, minus(x, xopt))));
        double[] s = new double[D];

        for (int i = 0; i < D; i++) {
            s[i] = z[i] * xopt[i] > 0 ? 100 : 1;
        }

        double sum = 0;
        for (int i = 0; i < D; i++) {
            sum += Math.pow(s[i] * z[i], 2);
        }

        return Math.pow(Tosz(new double[]{sum})[0], 0.9) + fopt;
    }

}
