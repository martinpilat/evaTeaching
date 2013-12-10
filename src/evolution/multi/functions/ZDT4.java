package evolution.multi.functions;

public class ZDT4 extends MultiObjectiveFunction {


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
        return 120.0 + 2.0/3.0;
    }

    double g(double[] ch) {
        double ret = 0;
        for (int i = 1; i < ch.length; i++)
            ret += ch[i]*ch[i] - 10*Math.cos(4*Math.PI*ch[i]);
        ret += 1 + 10*(ch.length - 1);
        return ret;
    }

    double f1(double[] ch) {
        return ch[0];
    }

    double f2(double[] ch) {
        double ret = Math.sqrt(f1(ch)/g(ch));
        ret = g(ch)*(1-ret);
        return ret;
    }

}
