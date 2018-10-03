package evolution.sga;

import evolution.*;
import evolution.individuals.BooleanIndividual;
import evolution.individuals.Individual;
import evolution.operators.BitFlipMutation;
import evolution.operators.OnePtXOver;
import evolution.selectors.RouletteWheelSelector;
import evolution.selectors.TournamentSelector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
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
    static int cpu_cores;
    static Properties prop;

    public static void main(String[] args) {

        prop = new Properties();
        String propertiesFile = "properties/ga-sga.properties";
        try {
            InputStream propIn = new FileInputStream(propertiesFile);
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read to properties from the file.

        maxGen = Integer.parseInt(prop.getProperty("ea.maxGenerations", "20"));
        popSize = Integer.parseInt(prop.getProperty("ea.popSize", "30"));
        xoverProb = Double.parseDouble(prop.getProperty("ea.xoverProb", "0.8"));
        mutProb = Double.parseDouble(prop.getProperty("ea.mutProb", "0.05"));
        mutProbPerBit = Double.parseDouble(prop.getProperty("ea.mutProbPerBit", "0.04"));

        dimension = Integer.parseInt(prop.getProperty("prob.dimension", "25"));

        repeats = Integer.parseInt(prop.getProperty("xset.repeats", "10"));
        cpu_cores = Integer.parseInt(prop.getProperty("xset.cpu_cores", "1"));

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

        try {
            Files.copy(new File(propertiesFile).toPath(), new File(path + ".properties").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Run the algorithm

        List<Individual> bestInds = new ArrayList<Individual>();

        for (int i = 0; i < repeats; i++) {
            Individual best = run(i);
            bestInds.add(best);
        }

        StatsLogger.processResults(fitnessFilePrefix, fitnessStatsFile, repeats, maxGen, popSize);
        StatsLogger.processResults(objectiveFilePrefix, objectiveStatsFile, repeats, maxGen, popSize);

        for (int i = 0; i < bestInds.size(); i++) {
            System.out.println("run " + i + ": best objective=" + bestInds.get(i).getObjectiveValue() + " ind:" + bestInds.get(i));
        }

    }

    /**
     * This is the main method, which executes one run of the evolutionary algorithm.
     *
     * @param number The number of this run, also used as seed for the random number generator.
     */


    public static Individual run(int number) {

        //Initialize logging of the run

        DetailsLogger.startNewLog(detailsLogPrefix + "." + number + ".xml");
        DetailsLogger.logParams(prop);

        //Set the rng seed

        RandomNumberGenerator.getInstance().reseed(number);

        //Create new population
        Population pop = new Population();
        pop.setPopulationSize(popSize);
        pop.setSampleIndividual(new BooleanIndividual(dimension));
        pop.createRandomInitialPopulation();


        //Set the options for the evolutionary algorithm
        EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
        ea.setCPUCores(cpu_cores);
        ea.setFitnessFunction(new ExampleFitnessFunction());
        ea.addMatingSelector(new RouletteWheelSelector());
        ea.addOperator(new OnePtXOver(xoverProb));
        ea.addOperator(new BitFlipMutation(mutProb, mutProbPerBit));

        //Run the algorithm

        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fitnessFilePrefix + "." + number));
            OutputStreamWriter progOut = new OutputStreamWriter(new FileOutputStream(objectiveFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {

                //Make one generation
                ea.evolve(pop);
                ArrayList<Individual> sorted = pop.getSortedIndividuals();
                //Log the best individual to console.
                System.out.println("fitness: " + sorted.get(0).getFitnessValue() + " " + sorted.get(0));

                //Add population statistics to the logs
                StatsLogger.logFitness(pop, out);
                StatsLogger.logObjective(pop, progOut);
            }

            OutputStreamWriter bestOut = new OutputStreamWriter(new FileOutputStream(bestPrefix + "." + number));

            Individual bestInd = pop.getSortedIndividuals().get(0);
            bestOut.write(bestInd.toString());

            out.close();
            progOut.close();
            bestOut.close();

            DetailsLogger.writeLog();

            return bestInd;
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;
    }
}
