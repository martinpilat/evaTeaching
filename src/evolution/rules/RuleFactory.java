package evolution.rules;

import evolution.RandomNumberGenerator;
import evolution.rules.conditions.GreaterThanCondition;
import evolution.rules.conditions.LessThanCondition;
import evolution.rules.conditions.UniversalCondition;

public class RuleFactory {

    public static Rule createRandomRule(int numconditions, int numClasses, double[] lb, double[] ub) {

        RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

        Rule r = new Rule();

        for (int i = 0; i < numconditions; i++) {
            double ball = rng.nextDouble();
            if (ball < 0.25) {
                LessThanCondition lc = new LessThanCondition(lb[i], ub[i]);
                lc.randomInitialization();
                r.addCondition(lc);
            }
            else if (ball < 0.5) {
                GreaterThanCondition gc = new GreaterThanCondition(lb[i], ub[i]);
                gc.randomInitialization();
                r.addCondition(gc);
            }
            else {
                r.addCondition(new UniversalCondition());
            }
        }

        r.setClassLabel(rng.nextInt(numClasses));

        return r;
    }
}
