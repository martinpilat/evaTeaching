package evolution.rules.conditions;

import evolution.RandomNumberGenerator;

public class GreaterThanCondition extends Condition {

    double boundary;
    double minValue;
    double maxValue;

    public GreaterThanCondition(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void randomInitialization() {
        this.boundary = RandomNumberGenerator.getInstance().nextDouble()*(maxValue - minValue) + minValue;
    }

    public boolean matches(double value) {
        return boundary > value;
    }

    public void mutate(Object parameter) {
        this.boundary += ((Double) parameter)*RandomNumberGenerator.getInstance().nextGaussian();
    }

    public Object clone() {
        GreaterThanCondition g = new GreaterThanCondition(minValue, maxValue);
        g.boundary = boundary;
        return g;
    }

    public String toString() {
        return String.format("> %1$.3f", boundary);
    }

}
