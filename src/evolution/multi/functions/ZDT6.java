package evolution.multi.functions;

public class ZDT6 extends MultiObjectiveFunction {


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
        return 117.51857519692037009;
    }

    double g(double[] ch) {
        double ret = 0;
        for (int i = 1; i < ch.length; i++)
            ret += ch[i];
        ret /= ch.length - 1;
        ret = Math.pow(ret, 0.25);
        ret *= 9;
        ret += 1;
        return ret;
    }

    double f1(double[] ch) {
        double ret = Math.sin(6*Math.PI*ch[0]);
        ret = Math.pow(ret, 6);
        ret *= Math.exp(-4*ch[0]);
        ret = 1 - ret;
        return ret;
    }

    double f2(double[] ch) {
        double ret = f1(ch)/g(ch);
        ret = Math.pow(ret, 2);
        ret = g(ch)*(1-ret);
        return ret;
    }

}
