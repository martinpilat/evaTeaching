package evolution.cv1;

import evolution.EvolutionaryAlgorithm;
import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.StatsLogger;
import evolution.individuals.BooleanIndividual;
import evolution.individuals.Individual;
import evolution.operators.BitFlipMutation;
import evolution.operators.OnePtXOver;
import evolution.selectors.RouletteWheelSelector;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

/**
 * @author Martin Pilat
 */
public class Main {

    static int maxGen;
    static int popSize;
    static String fitnessFilePrefix;
    static String fitnessStatsFile;
    static String objectiveFilePrefix;
    static String objectiveStatsFile;
    static String bestPrefix;
    static String outputDirectory;
    static int repeats;

    public static void main(String[] args) {

        Properties prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("properties/ga-cv1.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("max_generations", "20"));
        popSize = Integer.parseInt(prop.getProperty("population_size", "30"));
        outputDirectory = prop.getProperty("output_directory", "cv1");
        objectiveFilePrefix = outputDirectory + System.getProperty("file.separator") + prop.getProperty("objective_file_prefix", "objective.log");
        objectiveStatsFile = outputDirectory + System.getProperty("file.separator") + prop.getProperty("objective_stats_file", "objective_stats.log");
        bestPrefix = outputDirectory + System.getProperty("file.separator") + prop.getProperty("best_ind_prefix", "best");
        fitnessFilePrefix = outputDirectory + System.getProperty("file.separator") + prop.getProperty("fitness_log_prefix", "fitness.log");
        fitnessStatsFile = outputDirectory + System.getProperty("file.separator") + prop.getProperty("fitness_stats_file", "fitness_stats.log");
        repeats = Integer.parseInt(prop.getProperty("repeats", "10"));

        File outDir = new File(outputDirectory);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        for (int i = 0; i < repeats; i++) {
            run(i);
        }

        processResults(fitnessFilePrefix, fitnessStatsFile);
        processResults(objectiveFilePrefix, objectiveStatsFile);

    }

    static void processResults(String logPrefix, String resultsName) {
        Vector<Vector<Double>> bestFitnesses = new Vector<Vector<Double>>();
        try {
            for (int i = 0; i < repeats; i++) {
                Vector<Double> column = new Vector<Double>();

                BufferedReader in = new BufferedReader(new FileReader(
                        logPrefix + "." + i));
                String line = null;
                while ((line = in.readLine()) != null) {
                    double best = Double.parseDouble(line.split(" ")[0]);
                    column.add(best);
                }

                bestFitnesses.add(column);
            }

            FileWriter out = new FileWriter(resultsName);

            for (int j = 0; j < maxGen; j++) {
                double min = Double.MAX_VALUE;
                double max = -Double.MAX_VALUE;
                double sum = 0;
                for (int i = 0; i < repeats; i++) {
                    double current = bestFitnesses.get(i).get(j);

                    min = Math.min(current, min);
                    max = Math.max(current, max);
                    sum += current;
                }

                double avg = sum / repeats;

                out.write(min + " " + avg + " " + max
                        + System.getProperty("line.separator"));

            }
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run(int number) {

        RandomNumberGenerator.getInstance().reseed(number);

        Population pop = new Population();
        pop.setPopulationSize(popSize);
        pop.setSampleIndividual(new BooleanIndividual(25));
        pop.createRandomInitialPopulation();

        EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();

        ea.setFitnessFunction(new ExampleFitnessFunction());

        ea.addMatingSelector(new RouletteWheelSelector());
        ea.addOperator(new OnePtXOver(0.8));
        ea.addOperator(new BitFlipMutation(0.2, 0.04));
        ea.addEnvironmentalSelector(new RouletteWheelSelector());

        // System.out.println("Generation -1: " + pop.getSortedIndividuals().get(0).toString());

        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fitnessFilePrefix + "." + number));
            OutputStreamWriter progOut = new OutputStreamWriter(new FileOutputStream(objectiveFilePrefix + "." + number));


            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                ArrayList<Individual> sorted = pop.getSortedIndividuals();
                System.err.println(sorted.get(0));

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
    }
}
