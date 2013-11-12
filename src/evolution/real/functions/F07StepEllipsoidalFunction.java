package evolution.real.functions;

import evolution.RandomNumberGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F07StepEllipsoidalFunction extends RealFunction {

    public F07StepEllipsoidalFunction(int D) {
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
        double[] zC = diagMult(lambda, mult(R, minus(x, xopt)));
        double[] zW = new double[D];

        for (int i = 0; i < D; i++) {
            zW[i] = zC[i] > 0.5 ? Math.floor(0.5 + zC[i]) : Math.floor(0.5 + zC[i]) / 10.0;
        }

        double[] z = mult(Q, zW);

        double sum = 0;
        for (int i = 0; i < D; i++) {
            sum += Math.pow(10, 2.0 * i / (D - 1)) * Math.pow(z[i], 2);
        }

        return 0.1 * Math.max(Math.abs(zC[1]) / 1e4, sum) + fpen(x) + fopt;
    }

}
