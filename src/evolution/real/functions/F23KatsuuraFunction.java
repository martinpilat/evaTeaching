package evolution.real.functions;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F23KatsuuraFunction extends RealFunction {


    public F23KatsuuraFunction(int D) {
        super(D);
    }

    @Override
    public void reinit() {
        initXopt();
        fopt = 0;
        R = getRandomRotationMatrix();
        Q = getRandomRotationMatrix();
        lambda = createLambda(100);
    }

    @Override
    public double value(double[] x) {
        double[] z = mult(Q, diagMult(lambda, mult(R, minus(x, xopt))));

        double prod = 1.0;
        for (int i = 0; i < D; i++) {
            double sum = 0;
            for (int j = 0; j < 32; j++) {
                sum += Math.abs(Math.pow(2, j) * z[i] - Math.round(Math.pow(2, j) * z[i])) / Math.pow(2, j);
            }
            prod *= Math.pow(1 + (i + 1) * sum, 10.0 / Math.pow(D, 1.2));
        }

        return 10.0 / Math.pow(D, 2) * prod - 10.0 / Math.pow(D, 2) + fpen(x);
    }

}
