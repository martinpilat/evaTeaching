package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F16WeierstrassFunction extends RealFunction {

    public F16WeierstrassFunction(int D) {
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
        lambda = createLambda(0.01);
    }

    @Override
    public double value(double[] x) {
        double[] z = mult(R, diagMult(lambda, mult(Q, Tosz(mult(R, minus(x, xopt))))));

        double f0 = 0.0;
        for (int i = 0; i < 12; i++) {
            f0 += 1.0 / Math.pow(2, i) * Math.cos(2 * Math.PI * Math.pow(3, i) * 0.5);
        }

        double sum = 0;
        for (double d : z) {
            double is = 0.0;
            for (int i = 0; i < 12; i++) {
                is += 1.0 / Math.pow(2, i) * Math.cos(2 * Math.PI * Math.pow(3, i) * (d + 0.5));
            }
            sum += is;
        }

        return 10 * Math.pow(1.0 / D * sum - f0, 3) + 10.0 / D * fpen(x) + fopt;
    }

}
