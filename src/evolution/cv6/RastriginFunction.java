package evolution.cv6;

public class RastriginFunction implements RealFunction {

    @Override
    public double value(double[] x) {

        int n = x.length;

        double sum = 0;

        for (int i = 0; i < n; i++) {
            sum += x[i] * x[i] - 10 * Math.cos(2 * Math.PI * x[i]);
        }

        return (35 * n - sum) / (35 * n + 10 * n);
    }

}
