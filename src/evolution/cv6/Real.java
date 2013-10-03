package evolution.cv6;

import evolution.EvolutionaryAlgorithm;
import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.StatsLogger;
import evolution.individuals.Individual;
import evolution.individuals.RealIndividual;
import evolution.operators.AveragingCrossoverOperator;
import evolution.operators.GaussianMutationOperator;
import evolution.selectors.RouletteWheelSelector;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class Real {

    /**
     * @param args
     */
    static int maxGen;
    static int popSize;
    static int dimension;
    static String logFilePrefix;
    static String resultsFile;
    static int repeats;
    static Vector<Double> weights;
    static String progFilePrefix;
    static String progFile;
    static String bestPrefix;
    static double eliteSize;
    static String outputDir;

    public static void main(String[] args) {

        Properties prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("ga-cv6.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dimension = Integer.parseInt(prop.getProperty("dimension", "50"));
        maxGen = Integer.parseInt(prop.getProperty("max_generations", "2000"));
        popSize = Integer.parseInt(prop.getProperty("population_size", "50"));
        progFilePrefix = prop.getProperty("prog_file_prefix", "prog.log");
        progFile = prop.getProperty("prog_file_results", "progress.log");
        bestPrefix = prop.getProperty("best_ind_prefix", "best");
        logFilePrefix = prop.getProperty("log_filename_prefix", "ga.log");
        resultsFile = prop.getProperty("results_filename", "ga.log");
        repeats = Integer.parseInt(prop.getProperty("repeats", "10"));
        eliteSize = Double.parseDouble(prop.getProperty("elite_size", "0.1"));
        outputDir = prop.getProperty("output_dir", "cv6");

        File output = new File(outputDir);
        output.mkdirs();

        for (int i = 0; i < repeats; i++) {
            run(i);
        }

        processResults(logFilePrefix, resultsFile);
        processResults(progFilePrefix, progFile);

    }

    static void processResults(String logPrefix, String resultsName) {
        Vector<Vector<Double>> bestFitnesses = new Vector<Vector<Double>>();
        try {
            for (int i = 0; i < repeats; i++) {
                Vector<Double> column = new Vector<Double>();

                BufferedReader in = new BufferedReader(new FileReader(
                        outputDir + System.getProperty("file.separator") + logPrefix + "." + i));
                String line = null;
                while ((line = in.readLine()) != null) {
                    double best = Double.parseDouble(line.split(" ")[0]);
                    column.add(best);
                }

                bestFitnesses.add(column);
            }

            FileWriter out = new FileWriter(outputDir + System.getProperty("file.separator") + resultsName);

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

    static void run(int number) {

        try {

            RandomNumberGenerator.getInstance().reseed(number);
            RealIndividual sample = new RealIndividual(dimension, -5.0, 5.0);

            Population pop = new Population();
            pop.setPopulationSize(popSize);
            pop.setSampleIndividual(sample);
            pop.createRandomInitialPopulation();

            EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
            ea.addMatingSelector(new MySelector());
            ea.addOperator(new AveragingCrossoverOperator(0.8));
            ea.addOperator(new GaussianMutationOperator(0.1, 0.05));
            ea.setFitnessFunction(new RealFitnessFunction(new ShiftedRastriginFunction()));
            ea.addEnvironmentalSelector(new RouletteWheelSelector());
            ea.setElite(eliteSize);

            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outputDir + System.getProperty("file.separator") + logFilePrefix + "." + number));

            OutputStreamWriter progOut = new OutputStreamWriter(new FileOutputStream(outputDir + System.getProperty("file.separator") + progFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                Double diff = (Double) pop.getSortedIndividuals().get(0).getObjectiveValue();
                System.out.println("Generation " + i + ": " + diff);

                StatsLogger.logFitness(pop, out);
                StatsLogger.logObjective(pop, progOut);


            }

            OutputStreamWriter bestOut = new OutputStreamWriter(
                    new FileOutputStream(outputDir + System.getProperty("file.separator") + bestPrefix + "." + number));

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
