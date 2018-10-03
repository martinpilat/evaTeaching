package evolution.binPacking;

import evolution.*;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;
import evolution.operators.IntegerMutation;
import evolution.operators.OnePtXOver;
import evolution.selectors.RouletteWheelSelector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class BinPacking {

    static int maxGen;
    static int popSize;
    static String logFilePrefix;
    static int repeats;
    static int K;
    static Vector<Double> weights;
    static String bestPrefix;
    private static double xoverProb;
    private static double mutProb;
    private static double mutProbPerBit;
    private static String outputDirectory;
    private static String objectiveFilePrefix;
    private static String objectiveStatsFile;
    private static String fitnessFilePrefix;
    private static String fitnessStatsFile;
    private static String detailsLogPrefix;
    private static String enableDetailsLog;
    private static Properties prop;
    static int cpu_cores;

    /**
     * @param args
     */

    public static void main(String[] args) {

        String propertiesFile = "properties/ga-binPacking.properties";

        prop = new Properties();
        try {
            InputStream propIn = new FileInputStream(propertiesFile);
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("ea.maxGenerations", "20"));
        popSize = Integer.parseInt(prop.getProperty("ea.popSize", "30"));
        xoverProb = Double.parseDouble(prop.getProperty("ea.xoverProb", "0.8"));
        mutProb = Double.parseDouble(prop.getProperty("ea.mutProb", "0.05"));
        mutProbPerBit = Double.parseDouble(prop.getProperty("ea.mutProbPerBit", "0.04"));

        String inputFile = prop.getProperty("prob.inputFile", "resources/packingInput-easier.txt");
        K = Integer.parseInt(prop.getProperty("prob.numBins", "10"));

        repeats = Integer.parseInt(prop.getProperty("xset.repeats", "10"));
        cpu_cores = Integer.parseInt(prop.getProperty("xset.cpu_cores", "1"));


        enableDetailsLog = prop.getProperty("xlog.detailsLog", "enabled");

        if (!enableDetailsLog.equals("enabled")) {
            DetailsLogger.disableLog();
        }

        outputDirectory = prop.getProperty("xlog.outputDirectory", "binPacking");
        logFilePrefix = prop.getProperty("xlog.filePrefix", "log");
        String path = outputDirectory + System.getProperty("file.separator") + logFilePrefix;
        objectiveFilePrefix = path + ".objective";
        objectiveStatsFile = path + ".objective_stats";
        bestPrefix = path + ".best";
        fitnessFilePrefix = path + ".fitness";
        fitnessStatsFile = path + ".fitness_stats";
        detailsLogPrefix = path + ".details";

        File output = new File(outputDirectory);
        output.mkdirs();

        try {
            Files.copy(new File(propertiesFile).toPath(), new File(path + ".properties").toPath(),
                       StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        weights = new Vector<Double>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = in.readLine()) != null) {
                weights.add(Double.parseDouble(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        List<Individual> bestInds = new ArrayList<Individual>();

        for (int i = 0; i < repeats; i++) {
            Individual best = run(i);
            bestInds.add(best);
        }

        BinPackingFitness fitness = new BinPackingFitness(weights, K);

        for (int i = 0; i < bestInds.size(); i++) {
            System.out.println("run " + i + ": best objective=" + bestInds.get(i).getObjectiveValue() +
                    " weights:" + Arrays.toString(fitness.getBinWeights(bestInds.get(i))));
        }

        StatsLogger.processResults(fitnessFilePrefix, fitnessStatsFile, repeats, maxGen, popSize);
        StatsLogger.processResults(objectiveFilePrefix, objectiveStatsFile, repeats, maxGen, popSize);

    }

    static Individual run(int number) {

        try {

            DetailsLogger.startNewLog(detailsLogPrefix + "." + number + ".xml");
            DetailsLogger.logParams(prop);

            RandomNumberGenerator.getInstance().reseed(number);

            Individual sampleIndividual = new IntegerIndividual(weights.size(), 0, K);

            Population pop = new Population();
            pop.setPopulationSize(popSize);
            pop.setSampleIndividual(sampleIndividual);
            pop.createRandomInitialPopulation();

            BinPackingFitness fitness = new BinPackingFitness(weights, K);
            EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
            ea.setCPUCores(cpu_cores);
            ea.setFitnessFunction(fitness);
            ea.addMatingSelector(new RouletteWheelSelector());
            ea.addOperator(new OnePtXOver(xoverProb));
            ea.addOperator(new IntegerMutation(mutProb, mutProbPerBit));
            ea.addEnvironmentalSelector(new RouletteWheelSelector());

            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fitnessFilePrefix + "." + number));
            OutputStreamWriter progOut = new OutputStreamWriter(new FileOutputStream(objectiveFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                IntegerIndividual bestInd = (IntegerIndividual) pop.getSortedIndividuals().get(0);
                double diff = bestInd.getObjectiveValue();
                System.out.println("Generation " + i + ": " + diff + " " + Arrays.toString(fitness.getBinWeights(bestInd)));

                StatsLogger.logFitness(pop, out);
                StatsLogger.logObjective(pop, progOut);

            }

            OutputStreamWriter bestOut = new OutputStreamWriter(new FileOutputStream(bestPrefix + "." + number));

            IntegerIndividual bestInd = (IntegerIndividual) pop.getSortedIndividuals().get(0);

            for (int i = 0; i < bestInd.toIntArray().length; i++) {
                bestOut.write(weights.get(i) + " " + bestInd.toIntArray()[i] + System.getProperty("line.separator"));
            }

            out.close();
            progOut.close();
            bestOut.close();

            DetailsLogger.writeLog();

            return bestInd;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
