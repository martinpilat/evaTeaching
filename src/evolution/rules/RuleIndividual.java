package evolution.rules;

import evolution.RandomNumberGenerator;
import evolution.individuals.ArrayIndividual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RuleIndividual extends ArrayIndividual {

    ArrayList<Rule> rules;
    int maxRules;
    int numConditionsPerRule;
    double[] lb;
    double[] ub;
    int numClasses;

    public RuleIndividual(int maxRules, int numConditionsPerRule, int numClasses, double[] lb, double[] ub) {
        this.numConditionsPerRule = numConditionsPerRule;
        this.maxRules = maxRules;
        this.ub = ub;
        this.lb = lb;
        this.rules = new ArrayList<Rule>();
        this.numClasses = numClasses;
    }

    @Override
    public Object get(int n) {
        return rules.get(n);
    }

    @Override
    public void set(int n, Object o) {
        rules.set(n, (Rule)o);
    }

    @Override
    public int length() {
        return 0;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    @Override
    public void randomInitialization() {

        int ruleNum = RandomNumberGenerator.getInstance().nextInt(maxRules);

        rules = new ArrayList<Rule>();

        for (int i = 0; i < ruleNum; i++) {
            rules.add(RuleFactory.createRandomRule(numConditionsPerRule, numClasses, lb, ub));
        }

    }

    @Override
    public Object clone() {
        RuleIndividual r = new RuleIndividual(maxRules, numConditionsPerRule, numClasses, lb, ub);
        r.rules = new ArrayList<Rule>(rules.size());
        for (int i = 0; i < rules.size(); i++) {
            r.rules.add((Rule) rules.get(i).clone());
        }
        return r;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < rules.size(); i++) {
            str.append(rules.get(i));
            if (i < rules.size() - 1)
                str.append("\n");
        }
        return str.toString();
    }
}
