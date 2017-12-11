package evolution.rules.conditions;

public abstract class Condition {

    public abstract void randomInitialization();
    public abstract boolean matches(double value);
    public abstract void mutate(Object parameter);
    public abstract Object clone();
    public abstract String toString();

}
