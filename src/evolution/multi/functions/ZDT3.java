package evolution.multi.functions;

public class ZDT3 extends MultiObjectiveFunction {


    @Override
    public double[] evaluate(double[] x) {
        double[] vals =  new double[2];

        vals[0] = f1(x);
        vals[1] = f2(x);

        return vals;
    }

    @Override
    public double[] getReferencePoint() {
        return new double[]{11.0, 11.0};
    }

    @Override
    public double getOptimalHypervolume() {
        return 128.77811613069076060;
    }

    double g(double[] ch) {
        double ret = 0;
        for (int i = 1; i < ch.length; i++)
            ret += ch[i];
        ret *= 9.0/(ch.length - 1);
        ret += 1;
        return ret;
    }

    double f1(double[] ch) {
        return ch[0];
    }

    double f2(double[] ch) {
        double div = f1(ch) / g(ch);
        double ret = Math.sqrt(div);
        ret = 1 - ret - div*Math.sin(10*Math.PI*f1(ch));
        return g(ch)*ret;
    }

}
