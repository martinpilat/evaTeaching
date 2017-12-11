package evolution.rules;

import evolution.*;
import evolution.individuals.Individual;
import evolution.selectors.TournamentSelector;

import java.io.*;
import java.util.*;

public class RulesMain {

    static int maxGen;
    static int popSize;
    static int maxRules;
    static double xoverProb;
    static double mutProb;
    static double mutSigma;
    static String inputFile;
    static String logFilePrefix;
    static int repeats;
    static String bestPrefix;
    static double eliteSize;
    static double mutProbPerBit;
    static Properties prop;
    static String outputDirectory;
    static String objectiveFilePrefix;
    static String objectiveStatsFile;
    static String fitnessFilePrefix;
    static String fitnessStatsFile;
    static String detailsLogPrefix;
    static ArrayList<double[]> attrs;
    static ArrayList<Integer> targets;

    static double[] lb;
    static double[] ub;
    static int numClasses;
    static int numConditions;

    public static void main(String[] args) {

        prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("properties/ga-rules.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("ea.maxGenerations", "20"));
        popSize = Integer.parseInt(prop.getProperty("ea.popSize", "30"));
        xoverProb = Double.parseDouble(prop.getProperty("ea.xoverProb", "0.8"));
        mutProb = Double.parseDouble(prop.getProperty("ea.mutProb", "0.05"));
        mutProbPerBit = Double.parseDouble(prop.getProperty("ea.mutProbPerBit", "0.1"));
        mutSigma = Double.parseDouble(prop.getProperty("ea.mutSigma", "0.04"));
        eliteSize = Double.parseDouble(prop.getProperty("ea.eliteSize", "0.1"));
        maxRules = Integer.parseInt(prop.getProperty("ea.maxRules", "10"));
        inputFile = prop.getProperty("prob.inputFile", "resources/iris.csv");

        repeats = Integer.parseInt(prop.getProperty("xset.repeats", "10"));

        DetailsLogger.disableLog();

        outputDirectory = prop.getProperty("xlog.outputDirectory", "real");
        logFilePrefix = prop.getProperty("xlog.filePrefix", "log");
        String path = outputDirectory  + System.getProperty("file.separator") + logFilePrefix;
        objectiveFilePrefix = path + ".objective";
        objectiveStatsFile = path + ".objective_stats";
        bestPrefix = path + ".best";
        fitnessFilePrefix = path + ".fitness";
        fitnessStatsFile = path + ".fitness_stats";
        detailsLogPrefix = path + ".details";

        // read input csv

        attrs = new ArrayList<double[]>();
        targets = new ArrayList<Integer>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                double[] as = new double[tokens.length - 1];
                for (int i = 0; i < tokens.length - 1; i++) {
                    as[i] = Double.parseDouble(tokens[i]);
                }
                attrs.add(as);
                targets.add(Integer.parseInt(tokens[tokens.length - 1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        lb = Arrays.copyOf(attrs.get(0), attrs.get(0).length);
        ub = Arrays.copyOf(attrs.get(0), attrs.get(0).length);
        for (double[] at: attrs) {
            for (int i = 0; i < at.length; i++) {
                lb[i] = Math.min(lb[i], at[i]);
                ub[i] = Math.max(ub[i], at[i]);
            }
        }

        numClasses = 0;
        for (Integer i: targets) {
            numClasses = Math.max(i, numClasses);
        }

        numClasses += 1; //we assume class numbers start from 0
        numConditions = attrs.get(0).length;

        File outDir = new File(outputDirectory);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        List<Individual> bestInds = new ArrayList<Individual>();

        for (int i = 0; i < repeats; i++) {
            RandomNumberGenerator.getInstance().reseed(i);
            Individual best = run(i);
            bestInds.add(best);
        }

        StatsLogger.processResults(fitnessFilePrefix, fitnessStatsFile, repeats, maxGen, popSize);
        StatsLogger.processResults(objectiveFilePrefix, objectiveStatsFile, repeats, maxGen, popSize);

        for (int i = 0; i < bestInds.size(); i++) {
            System.out.println("run " + i + ": best objective=" + bestInds.get(i).getObjectiveValue());
        }

    }

    static Individual run(int number) {

        //Initialize logging of the run

        DetailsLogger.startNewLog(detailsLogPrefix + "." + number + ".xml");
        DetailsLogger.logParams(prop);

        try {

            RuleIndividual sample = new RuleIndividual(maxRules, numConditions, numClasses, lb, ub);

            Population pop = new Population();
            pop.setPopulationSize(popSize);
            pop.setSampleIndividual(sample);
            pop.createRandomInitialPopulation();

            EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
            ea.addOperator(new RulesCrossoverOperator(xoverProb));
            ea.addOperator(new ConditionMutationOperator(mutProb, mutProbPerBit, mutSigma));
            ea.addOperator(new ClassChangeMutationOperator(mutProb, mutProbPerBit, 3));
            ea.setFitnessFunction(new RuleFitness(attrs, targets));
            ea.addEnvironmentalSelector(new TournamentSelector());
            ea.setElite(eliteSize);

            OutputStreamWriter fitnessOut = new OutputStreamWriter(new FileOutputStream(fitnessFilePrefix + "." + number));
            OutputStreamWriter objectiveOut = new OutputStreamWriter(new FileOutputStream(objectiveFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                RuleIndividual ri = (RuleIndividual)pop.getSortedIndividuals().get(0);
                Double error_rate = ri.getObjectiveValue();
                System.out.println("Generation " + i + ": "  + error_rate);

                StatsLogger.logFitness(pop, fitnessOut);
                StatsLogger.logObjective(pop, objectiveOut);
            }

            RuleIndividual ri = (RuleIndividual)pop.getSortedIndividuals().get(0);
            Double error_rate = ri.getObjectiveValue();
            System.out.println("End: "  + error_rate);

            OutputStreamWriter bestOut = new OutputStreamWriter(new FileOutputStream(bestPrefix + "." + number));

            bestOut.write("Fitness: " + ri.getFitnessValue() + "\n");
            bestOut.write("Objective: " + ri.getObjectiveValue() + "\n");
            bestOut.write(ri.toString());

            fitnessOut.close();
            objectiveOut.close();
            bestOut.close();

            DetailsLogger.writeLog();

            return ri;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
