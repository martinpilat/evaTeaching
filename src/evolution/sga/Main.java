package evolution.sga;

import evolution.*;
import evolution.individuals.BooleanIndividual;
import evolution.individuals.Individual;
import evolution.operators.BitFlipMutation;
import evolution.operators.OnePtXOver;
import evolution.selectors.RouletteWheelSelector;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Martin Pilat
 */
public class Main {

    static int maxGen;
    static int popSize;
    static int dimension;
    static double xoverProb;
    static double mutProb;
    static double mutProbPerBit;
    static String fitnessFilePrefix;
    static String fitnessStatsFile;
    static String objectiveFilePrefix;
    static String objectiveStatsFile;
    static String bestPrefix;
    static String outputDirectory;
    static String logFilePrefix;
    static String detailsLogPrefix;
    static int repeats;
    static Properties prop;

    public static void main(String[] args) {

        prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("properties/ga-sga.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("ea.maxGenerations", "20"));
        popSize = Integer.parseInt(prop.getProperty("ea.popSize", "30"));
        xoverProb = Double.parseDouble(prop.getProperty("ea.xoverProb", "0.8"));
        mutProb = Double.parseDouble(prop.getProperty("ea.mutProb", "0.05"));
        mutProbPerBit = Double.parseDouble(prop.getProperty("ea.mutProbPerBit", "0.04"));

        dimension = Integer.parseInt(prop.getProperty("prob.dimension", "25"));

        repeats = Integer.parseInt(prop.getProperty("xset.repeats", "10"));

        outputDirectory = prop.getProperty("xlog.outputDirectory", "sga");
        logFilePrefix = prop.getProperty("xlog.filePrefix", "log");
        String path = outputDirectory + System.getProperty("file.separator") + logFilePrefix;
        objectiveFilePrefix = path + ".objective";
        objectiveStatsFile = path + ".objective_stats";
        bestPrefix = path + ".best";
        fitnessFilePrefix = path + ".fitness";
        fitnessStatsFile = path + ".fitness_stats";
        detailsLogPrefix = path + ".details";

        File outDir = new File(outputDirectory);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        for (int i = 0; i < repeats; i++) {
            run(i);
        }

        StatsLogger.processResults(fitnessFilePrefix, fitnessStatsFile, repeats, maxGen, popSize);
        StatsLogger.processResults(objectiveFilePrefix, objectiveStatsFile, repeats, maxGen, popSize);

    }

    public static void run(int number) {

        DetailsLogger.startNewLog(detailsLogPrefix + "." + number + ".xml");
        DetailsLogger.logParams(prop);

        RandomNumberGenerator.getInstance().reseed(number);

        Population pop = new Population();
        pop.setPopulationSize(popSize);
        pop.setSampleIndividual(new BooleanIndividual(dimension));
        pop.createRandomInitialPopulation();

        EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();

        ea.setFitnessFunction(new ExampleFitnessFunction());

        ea.addMatingSelector(new RouletteWheelSelector());
        ea.addOperator(new OnePtXOver(xoverProb));
        ea.addOperator(new BitFlipMutation(mutProb, mutProbPerBit));
        ea.addEnvironmentalSelector(new RouletteWheelSelector());

        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fitnessFilePrefix + "." + number));
            OutputStreamWriter progOut = new OutputStreamWriter(new FileOutputStream(objectiveFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                ArrayList<Individual> sorted = pop.getSortedIndividuals();
                System.err.println("fitness: " + sorted.get(0).getFitnessValue() + " " + sorted.get(0));

                StatsLogger.logFitness(pop, out);
                StatsLogger.logObjective(pop, progOut);
            }

            OutputStreamWriter bestOut = new OutputStreamWriter(new FileOutputStream(bestPrefix + "." + number));

            Individual bestInd = pop.getSortedIndividuals().get(0);
            bestOut.write(bestInd.toString());

            out.close();
            progOut.close();
            bestOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DetailsLogger.writeLog();

    }
}
