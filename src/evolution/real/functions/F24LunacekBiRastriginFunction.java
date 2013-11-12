package evolution.real.functions;

/**
 * Created with IntelliJ IDEA.
 * User: Martin Pilat
 * Date: 11.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class F24LunacekBiRastriginFunction extends RealFunction {

    double d = 1.0;
    double s;
    double mu1;
    double[] oneMu;

    public F24LunacekBiRastriginFunction(int D) {
        super(D);
        s = 1.0 - 1.0 / (2.0 * Math.sqrt(D + 20) - 8.2);
        mu1 = -Math.sqrt((2.5 * 2.5 - d) / s);
        oneMu = new double[D];
        for (int i = 0; i < D; i++) {
            oneMu[i] = 2.5;
        }
    }

    @Override
    public void reinit() {
        onePlusMinus = createOnePlusMinus();
        for (int i = 0; i < D; i++) {
            xopt[i] = 2.5 * onePlusMinus[i];
        }
        fopt = 0.0;
        R = getRandomRotationMatrix();
        Q = getRandomRotationMatrix();
        lambda = createLambda(100);
    }

    @Override
    public double value(double[] x) {
        double[] xR = new double[D];
        for (int i = 0; i < D; i++) {
            xR[i] = 2 * Math.signum(xopt[i]) * x[i];
        }
        double[] z = mult(Q, mult(lambda, mult(R, minus(xR, oneMu))));

        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;
        for (int i = 0; i < D; i++) {
            sum1 += Math.pow(xR[i] - 2.5, 2);
            sum2 += Math.pow(xR[i] - mu1, 2);
            sum3 += Math.cos(2.0 * Math.PI * z[i]);
        }

        return Math.min(sum1, d * D + s * sum2) + 10.0 * (D - sum3) + 1e4 * fpen(x);
    }

}
