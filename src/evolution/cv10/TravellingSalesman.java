package evolution.cv10;

import evolution.EvolutionaryAlgorithm;
import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.StatsLogger;
import evolution.individuals.IntegerIndividual;
import evolution.operators.SwappingMutationOperator;
import evolution.selectors.TournamentSelector;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Vector;

public class TravellingSalesman {

    static int maxGen;
    static int popSize;
    static String logFilePrefix;
    static String resultsFile;
    static int repeats;
    static int K;
    static Vector<Coordinates> coords;
    static String progFilePrefix;
    static String progFile;
    static String bestPrefix;
    static double eliteSize;

    public static void main(String[] args) {

        Properties prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("ga-cv11.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("max_generations", "20"));
        popSize = Integer.parseInt(prop.getProperty("population_size", "30"));
        progFilePrefix = prop.getProperty("prog_file_prefix", "objective.log");
        progFile = prop.getProperty("prog_file_results", "objective_stats.log");
        bestPrefix = prop.getProperty("best_ind_prefix", "best");
        logFilePrefix = prop.getProperty("log_filename_prefix", "fitness.log");
        resultsFile = prop.getProperty("results_filename", "fitness_stats.log");
        repeats = Integer.parseInt(prop.getProperty("repeats", "10"));
        eliteSize = Double.parseDouble(prop.getProperty("elite_size", "0.1"));

        String inputFile = prop.getProperty("input_file", "tsp_evropa.in");

        coords = new Vector<Coordinates>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = in.readLine()) != null) {
                String[] c = line.split(" ");
                coords.add(new Coordinates(Double.parseDouble(c[0]), Double.parseDouble(c[1])));
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

    static void run(int number) {

        RandomNumberGenerator.getInstance().reseed(number);

        try {

            IntegerIndividual sampleIndividual = new IntegerIndividual(coords.size(), 0, coords.size());

            Population pop = new Population();
            pop.setSampleIndividual(sampleIndividual);
            pop.setPopulationSize(popSize);

            EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
            ea.setFitnessFunction(new TSPFitness(coords));
            ea.addOperator(new SwappingMutationOperator(0.8, 0.05));
            ea.addEnvironmentalSelector(new TournamentSelector());
            ea.setElite(0.05);

            pop.createRandomInitialPopulation();

            for (int i = 0; i < pop.getPopulationSize(); i++) {

                ArrayList rand = new ArrayList();

                for (int j = 0; j < coords.size(); j++) {
                    rand.add(j);
                }

                Collections.shuffle(rand);

                IntegerIndividual tmp = (IntegerIndividual) pop.get(i);
                for (int j = 0; j < tmp.length(); j++) {
                    tmp.set(j, rand.get(j));
                }

            }

            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(logFilePrefix + "." + number));

            OutputStreamWriter progOut = new OutputStreamWriter(
                    new FileOutputStream(progFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);

                IntegerIndividual ch = (IntegerIndividual) pop.getSortedIndividuals().get(0);

                boolean[] used = new boolean[ch.length()];
                for (int g : ch.toIntArray()) {
                    if (used[g]) {
                        throw new RuntimeException("The city with id " + g + " found multiple times");
                    }
                    used[g] = true;
                }

                Double diff = (Double) pop.getSortedIndividuals().get(0).getObjectiveValue();
                System.out.println("Generation " + i + ": " + diff);


                StatsLogger.logFitness(pop, out);
                StatsLogger.logObjective(pop, progOut);

            }

            OutputStreamWriter bestOut = new OutputStreamWriter(
                    new FileOutputStream(bestPrefix + "." + number + ".kml"));


            FileReader in = new FileReader("kmlstart");
            BufferedReader rin = new BufferedReader(in);

            String line = "";

            while ((line = rin.readLine()) != null) {
                bestOut.write(line + System.getProperty("line.separator"));
            }

            in.close();
            rin.close();

            IntegerIndividual bestInd = (IntegerIndividual) pop.getSortedIndividuals().get(0);

            for (int i = 0; i < bestInd.length(); i++) {
                bestOut.write(" " + coords.get((Integer) bestInd.get(i)).toString() + System.getProperty("line.separator"));
            }

            bestOut.write(" " + coords.get((Integer) bestInd.get(0)).toString() + System.getProperty("line.separator"));

            in = new FileReader("kmlend");
            rin = new BufferedReader(in);

            line = "";

            while ((line = rin.readLine()) != null) {
                bestOut.write(line);
            }

            in.close();
            rin.close();
            out.close();
            progOut.close();
            bestOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
