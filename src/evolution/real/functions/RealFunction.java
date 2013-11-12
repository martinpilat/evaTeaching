package evolution.real.functions;

import evolution.RandomNumberGenerator;

public abstract class RealFunction {

    protected int D;
    protected double fopt;
    protected double[] xopt;
    protected double[][] R;
    protected double[][] Q;
    protected double[][] lambda;
    protected double[] onePlusMinus;

    protected RealFunction(int D) {
        this.D = D;
    }

    protected double fpen(double[] x) {

        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(Math.max(0, Math.abs(x[i]) - 5), 2);
        }

        return sum;
    }

    protected double[] Tasy(double[] x, double beta) {

        double[] y = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            if (x[i] > 0) {
                y[i] = Math.pow(x[i], 1 + beta * Math.sqrt(x[i]) * i / (D - 1));
            } else {
                y[i] = x[i];
            }
        }

        return y;
    }

    protected double xBar(double x) {
        if (x == 0)
            return 0;
        return Math.log(Math.abs(x));
    }

    protected double c1(double x) {
        if (x > 0)
            return 10;
        return 5.5;
    }

    protected double c2(double x) {
        if (x > 0)
            return 7.9;
        return 3.1;
    }

    protected double[] Tosz(double[] x) {

        double[] y = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            y[i] = Math.signum(x[i]) * Math.exp(xBar(x[i]) + 0.049 * (Math.sin(c1(x[i]) * xBar(x[i]) + Math.sin(c2(x[i]) * xBar(x[i])))));
        }

        return y;
    }

    protected void initXopt() {
        xopt = new double[D];

        for (int i = 0; i < D; i++) {
            xopt[i] = RandomNumberGenerator.getInstance().nextDouble() * 4 - 8;
        }
    }

    protected double[][] createLambda(double alpha) {

        double[][] lam = new double[D][D];

        for (int i = 0; i < D; i++) {
            for (int j = 0; j < D; j++) {
                lam[i][j] = 0;
            }
        }

        for (int i = 0; i < D; i++) {
            lam[i][i] = Math.pow(alpha, 0.5 * i / (D - 1));
        }

        return lam;
    }


    protected double[][] getRandomRotationMatrix() {
        double[][] R = new double[D][D];
        //1. Generate random matrix with normally distributed entries
        for (int i = 0; i < D; i++) {
            for (int j = 0; j < D; j++)
                R[i][j] = RandomNumberGenerator.getInstance().nextGaussian();
        }

        double prod;

        //2. Run Gramm-Schmidt orthonormalization to get a random rotation matrix
        //1st coordinate is row, 2nd is column.
        for (int i = 0; i < D; i++) {
            for (int j = 0; j < i; j++) {
                prod = 0;
                for (int k = 0; k < D; k++) {
                    prod += R[k][i] * R[k][j];
                }
                for (int k = 0; k < D; k++) {
                    R[k][i] -= prod * R[k][j];
                }
            }
            prod = 0;
            for (int k = 0; k < D; k++) {
                prod += R[k][i] * R[k][i];
            }
            for (int k = 0; k < D; k++) {
                R[k][i] /= Math.sqrt(prod);
            }
        }

        return R;
    }

    protected double[][] mult(double[][] a, double[][] b) {
        double[][] c = new double[D][D];

        for (int i = 0; i < D; i++)
            for (int j = 0; j < D; j++)
                for (int k = 0; k < D; k++)
                    c[i][k] += a[i][k] * b[k][j];
        return c;
    }

    protected double[] mult(double[][] a, double[] b) {
        double[] c = new double[D];

        for (int i = 0; i < D; i++) {
            for (int j = 0; j < D; j++) {
                c[i] += a[i][j] * b[j];
            }
        }

        return c;
    }

    protected double[] mult(double[] a, double[][] b) {
        double[] c = new double[D];

        for (int i = 0; i < D; i++) {
            for (int j = 0; j < D; j++) {
                c[i] = a[i] * b[j][i];
            }
        }

        return c;
    }

    protected double[] minus(double[] a, double[] b) {
        double[] c = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] - b[i];
        }

        return c;
    }

    protected double norm(double[] a) {
        double sum = 0;

        for (double d : a) {
            sum += d * d;
        }

        return Math.sqrt(sum);
    }

    protected double norm2(double[] a) {
        double sum = 0;

        for (double d : a) {
            sum += d * d;
        }

        return sum;
    }

    protected double[] createOnePlusMinus() {
        double[] o = new double[D];

        for (int i = 0; i < D; i++) {
            o[i] = RandomNumberGenerator.getInstance().nextDouble() < 0.5 ? -1.0 : 1.0;
        }

        return o;
    }

    public abstract double value(double[] x);

    public abstract void reinit();

}
