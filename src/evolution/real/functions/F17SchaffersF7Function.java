package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F17SchaffersF7Function extends RealFunction {

    public F17SchaffersF7Function(int D) {
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
        double[] z = diagMult(lambda, mult(Q, Tasy(mult(R, minus(x, xopt)), 0.5)));
        double[] s = new double[D - 1];

        for (int i = 0; i < D - 1; i++) {
            s[i] = Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]);
        }

        double sum = 0;
        for (double d : s) {
            sum += Math.sqrt(d) + Math.sqrt(d) * Math.pow(Math.sin(50 * Math.pow(d, 0.2)), 2);
        }

        return Math.pow(1.0 / (D - 1) * sum, 2) + 10.0 * fpen(x) + fopt;
    }

}
