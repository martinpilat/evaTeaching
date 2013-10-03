package evolution.cv2;

import evolution.EvolutionaryAlgorithm;
import evolution.Population;
import evolution.StatsLogger;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;
import evolution.operators.IntegerMutation;
import evolution.operators.OnePtXOver;
import evolution.selectors.RouletteWheelSelector;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

public class Hromadky {

    /**
     * @param args
     */
    static int maxGen;
    static int popSize;
    static String logFilePrefix;
    static String resultsFile;
    static int repeats;
    static int K;
    static Vector<Double> weights;
    static String progFilePrefix;
    static String progFile;
    static String bestPrefix;
    static String outputDir;

    public static void main(String[] args) {

        Properties prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("ga-cv2.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("max_generations", "20"));
        K = Integer.parseInt(prop.getProperty("bins", "10"));
        popSize = Integer.parseInt(prop.getProperty("population_size", "30"));
        repeats = Integer.parseInt(prop.getProperty("repeats", "10"));


        progFilePrefix = prop.getProperty("prog_file_prefix", "prog.log");
        progFile = prop.getProperty("prog_file_results", "progress.log");
        bestPrefix = prop.getProperty("best_ind_prefix", "best");
        logFilePrefix = prop.getProperty("log_filename_prefix", "ga.log");
        resultsFile = prop.getProperty("results_filename", "ga.log");
        outputDir = prop.getProperty("output_dir", "cv2");

        File output = new File(outputDir);
        output.mkdirs();

        String inputFile = prop.getProperty("input_file");

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

            Individual sampleIndividual = new IntegerIndividual(weights.size(), 0, K);

            Population pop = new Population();
            pop.setPopulationSize(popSize);
            pop.setSampleIndividual(sampleIndividual);
            pop.createRandomInitialPopulation();

            EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
            HromadkyFitness fitness = new HromadkyFitness(weights, K);
            ea.setFitnessFunction(fitness);
            ea.addMatingSelector(new RouletteWheelSelector());
            ea.addOperator(new OnePtXOver(0.8));
            ea.addOperator(new IntegerMutation(0.2, 0.05));
            ea.addEnvironmentalSelector(new RouletteWheelSelector());

            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outputDir + System.getProperty("file.separator") + logFilePrefix + "." + number));

            OutputStreamWriter progOut = new OutputStreamWriter(new FileOutputStream(outputDir + System.getProperty("file.separator") + progFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                IntegerIndividual bestInd = (IntegerIndividual) pop.getSortedIndividuals().get(0);
                double diff = bestInd.getObjectiveValue();
                System.out.println("Generation " + i + ": " + diff + " " + Arrays.toString(fitness.getBinWeights(bestInd)));

                StatsLogger.logFitness(pop, out);
                StatsLogger.logObjective(pop, progOut);

            }

            OutputStreamWriter bestOut = new OutputStreamWriter(
                    new FileOutputStream(outputDir + System.getProperty("file.separator") + bestPrefix + "." + number));

            IntegerIndividual bestInd = (IntegerIndividual) pop.getSortedIndividuals().get(0);

            for (int i = 0; i < bestInd.toIntArray().length; i++) {
                bestOut.write(weights.get(i) + " " + bestInd.toIntArray()[i] + System.getProperty("line.separator"));
            }

            out.close();
            progOut.close();
            bestOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
