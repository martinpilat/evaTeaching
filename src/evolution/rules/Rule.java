package evolution.rules;

import evolution.rules.conditions.Condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Rule {

    ArrayList<Condition> conditions;
    int classLabel;

    public Rule() {
        conditions = new ArrayList<Condition>();
    }


    boolean matches(double[] x) {
        boolean match = true;
        for (int i = 0; i < conditions.size(); i++) {
            match = match && (conditions.get(i).matches(x[i]));
        }
        return match;
    }

    public int getClassLabel() {
        return this.classLabel;
    }

    public void setClassLabel(int classLabel) {
        this.classLabel = classLabel;
    }

    public void setCondition(int i, Condition c) {
        conditions.set(i, c);
    }

    public void addCondition(Condition c) {
        conditions.add(c);
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public Object clone() {
        Rule n = new Rule();
        n.classLabel = classLabel;
        n.conditions = new ArrayList<Condition>(conditions.size());
        for (int i = 0; i < conditions.size(); i++) {
            n.conditions.add((Condition) conditions.get(i).clone());
        }
        return n;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(conditions.toArray()));
        sb.append(" -> ");
        sb.append(classLabel);
        return sb.toString();
    }
}
