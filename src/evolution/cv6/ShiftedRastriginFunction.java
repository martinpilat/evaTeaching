package evolution.cv6;

public class ShiftedRastriginFunction implements RealFunction {

    @Override
    public double value(double[] x) {

        int n = x.length;

        double sum = 0;

        for (int i = 0; i < n; i++) {
            sum += (x[i] - 2) * (x[i] - 2) - 10 * Math.cos(2 * Math.PI * (x[i] - 2));
        }

        return (59 * n - sum) / (59 * n + 10 * n);
    }

}
