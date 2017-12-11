package evolution.rules.conditions;

public class UniversalCondition extends Condition {


    @Override
    public void randomInitialization() {
    }

    @Override
    public boolean matches(double value) {
        return true;
    }

    @Override
    public void mutate(Object parameter) {
    }

    @Override
    public Object clone() {
        return new UniversalCondition();
    }

    @Override
    public String toString() {
        return "   *   ";
    }
}
