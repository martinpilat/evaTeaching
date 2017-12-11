package evolution.rules;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.operators.Operator;

import java.util.Collections;

public class RulesCrossoverOperator implements Operator{

    double xoverProb;
    RandomNumberGenerator rng;

    public RulesCrossoverOperator(double xoverProb) {
        this.xoverProb = xoverProb;
        this.rng = RandomNumberGenerator.getInstance();
    }


    @Override
    public void operate(Population parents, Population offspring) {
        int size = parents.getPopulationSize();

        parents.shuffle();

        for (int i = 0; i < size / 2; i++) {
            RuleIndividual p1 = (RuleIndividual) parents.get(2 * i);
            RuleIndividual p2 = (RuleIndividual) parents.get(2 * i + 1);

            RuleIndividual o1 = (RuleIndividual) p1.clone();
            RuleIndividual o2 = (RuleIndividual) p2.clone();

            if (rng.nextDouble() < xoverProb) {

                int o1rules = o1.getRules().size();
                int o2rules = o2.getRules().size();

                // make sure individual with more rules is o2
                if (o1rules > o2rules) {
                    RuleIndividual tmp = o2;
                    o2 = o1;
                    o1 = tmp;
                }

                int shorter = Math.min(o1rules, o2rules);
                int longer = Math.max(o1rules, o2rules);

                for (int j = 0; j < shorter; j++) {
                    if (rng.nextDouble() < 0.5) {
                        Rule r = o2.getRules().get(j);
                        o2.set(j, o1.getRules().get(j));
                        o1.set(j, r);
                    }
                }

                for (int j = shorter; j < longer; j++) {
                    if (rng.nextDouble() < 0.5) {
                        o1.getRules().add(o2.getRules().get(j));
                        o2.getRules().set(j, null);
                    }
                }
                o2.getRules().removeAll(Collections.singleton(null));
            }

            offspring.add(o1);
            offspring.add(o2);
        }
    }
}
