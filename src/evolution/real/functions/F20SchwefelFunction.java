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
            xopt[i] = 0.5 * 4.2096874633 * onePlusMinus[i];
        }
    }

    @Override
    public double value(double[] x) {
        double condition = 10.;
        double tmp, Fadd, Fpen = 0.0, Ftrue = 0.0;

        Fadd = fopt;

        double[] zR = new double[D];
        for (int i = 0; i < D; i++)
        {
            zR[i] = 2. * x[i];
            if (xopt[i] < 0.)
                zR[i] *= -1.;
        }

        double[] z = new double[D];
        z[0] = zR[0];
        for (int i = 1; i < D; i++)
        {
            z[i] = zR[i] + 0.25 * (zR[i-1] - 2. * Math.abs(xopt[i-1]));
        }

        for (int i = 0; i < D; i++)
        {
            z[i] -= 2 * Math.abs(xopt[i]);
        }

        z = diagMult(lambda, z);

        for (int i = 0; i < D; i++)
        {
            z[i] = 100. * (z[i] + 2 * Math.abs(xopt[i]));
        }

    /* BOUNDARY HANDLING*/
        for (int i = 0; i < D; i++)
        {
            tmp = Math.abs(z[i]) - 500.;
            if (tmp > 0.)
            {
                Fpen += tmp * tmp;
            }
        }
        Fadd += 0.01 * Fpen;

    /* COMPUTATION core*/
        for (int i = 0; i < D; i++)
        {
            Ftrue += z[i] * Math.sin(Math.sqrt(Math.abs(z[i])));
        }
        Ftrue = 0.01 * ((418.9828872724339) - Ftrue / (double)D);

        Ftrue += Fadd;

        return Ftrue;
    }

}
