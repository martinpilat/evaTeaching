package evolution.rules.conditions;

import evolution.RandomNumberGenerator;

public class LessThanCondition extends Condition {

    double boundary;
    double minValue;
    double maxValue;

    public LessThanCondition(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void randomInitialization() {
        this.boundary = RandomNumberGenerator.getInstance().nextDouble()*(maxValue - minValue) + minValue;
    }

    public boolean matches(double value) {
        return boundary < value;
    }

    public void mutate(Object parameter) {
        this.boundary += ((Double) parameter)*RandomNumberGenerator.getInstance().nextGaussian();
    }

    public Object clone() {
        LessThanCondition l = new LessThanCondition(minValue, maxValue);
        l.boundary = boundary;
        return l;
    }

    public String toString() {
        return String.format("< %1$.3f", boundary);
    }

}
