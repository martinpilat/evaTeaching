package evolution.rules;

import evolution.FitnessFunction;
import evolution.RandomNumberGenerator;
import evolution.individuals.Individual;

import java.util.*;

public class RuleFitness implements FitnessFunction{

    double[][] trainingData;
    Integer[] trainingClasses;
    double[][] testingData;
    Integer[] testingClasses;

    public RuleFitness(ArrayList<double[]> data, ArrayList<Integer> classes) {
        RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

        int len = data.size();
        HashSet<Integer> testingIdx = new HashSet<Integer>();

        while (testingIdx.size() < len/3) {
            int nextIdx = rng.nextInt(len);
            if (!testingIdx.contains(nextIdx)) {
                testingIdx.add(nextIdx);
            }
        }

        ArrayList<double[]> testingDataList = new ArrayList<double[]>();
        ArrayList<Integer> testingClassesList = new ArrayList<Integer>();
        ArrayList<double[]> trainingDataList = new ArrayList<double[]>();
        ArrayList<Integer> trainingClassesList = new ArrayList<Integer>();

        for (int i = 0; i < len; i++) {
            if (testingIdx.contains(i)) {
                testingDataList.add(data.get(i));
                testingClassesList.add(classes.get(i));
            }
            else {
                trainingDataList.add(data.get(i));
                trainingClassesList.add(classes.get(i));
            }
        }

        this.testingData = testingDataList.toArray(new double[][] {});
        this.testingClasses = testingClassesList.toArray(new Integer[] {});
        this.trainingData = trainingDataList.toArray(new double[][] {});
        this.trainingClasses = trainingClassesList.toArray(new Integer[] {});
    }

    double accuracy(Individual ind, double[][] data, Integer[] classes) {
        RuleIndividual rind = (RuleIndividual)ind;

        double acc = 0.0;

        for (int i = 0; i < data.length; i++) {
            HashMap<Integer, Double> classVotes = new HashMap<Integer, Double>();
            for (Rule r : rind.getRules()) {
                if (r.matches(data[i])) {
                    int cls = r.getClassLabel();
                    double votes = 0.0;
                    if (classVotes.containsKey(cls))
                        votes = classVotes.get(cls);
                    classVotes.put(cls, votes+1.0);
                }
            }

            int bestClass = 0;
            double mostVotes = 0;
            for (Map.Entry<Integer, Double> entry: classVotes.entrySet()) {
                if (entry.getValue() > mostVotes) {
                    mostVotes = entry.getValue();
                    bestClass = entry.getKey();
                }
            }

            if (bestClass == classes[i]) {
                acc += 1.0;
            }
        }

        return acc/data.length;
    }


    @Override
    public double evaluate(Individual ind) {
        ind.setObjectiveValue(1-accuracy(ind, testingData, testingClasses));
        return accuracy(ind, trainingData, trainingClasses);
    }
}
