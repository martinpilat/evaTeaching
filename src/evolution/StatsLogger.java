package evolution;

import evolution.individuals.Individual;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * This class serves as a logging interface for logging of the progress of fitness
 * and the objective of the individual.
 *
 * @author Martin Pilat
 */

public class StatsLogger {

    /**
     * Logs the information on fitness in the population. Writes one line to the
     * out stream. This line contains two numbers: the fitness of the best individual
     * in the population and the average fitness of the individuals in the population.
     *
     * @param pop The population from which the statistics are computed.
     * @param out The output stream to which the results are appended.
     */

    public static void logFitness(Population pop, OutputStreamWriter out) {

        ArrayList<Individual> sortedIndividuals = pop.getSortedIndividuals();
        double bestFitness = sortedIndividuals.get(0).getFitnessValue();

        double fitnessSum = 0;
        for (Individual ch : sortedIndividuals) {
            fitnessSum += ch.getFitnessValue();
        }

        double averageFitness = fitnessSum / pop.getPopulationSize();

        try {
            out.write("" + bestFitness + " " + averageFitness + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Logs the information on the objective values in the population. Writes one line to the
     * out stream. This line contains two numbers: the objective value of the individual with best
     * fitness the average objective value of the individuals in the population.
     *
     * @param pop The population from which the statistics are computed.
     * @param out The output stream to which the results are appended.
     */

    public static void logObjective(Population pop, OutputStreamWriter out) {

        ArrayList<Individual> sortedIndividuals = pop.getSortedIndividuals();
        double bestFitness = sortedIndividuals.get(0).getObjectiveValue();

        double fitnessSum = 0;
        for (Individual ch : sortedIndividuals) {
            fitnessSum += ch.getObjectiveValue();
        }

        double averageFitness = fitnessSum / pop.getPopulationSize();

        try {
            out.write("" + bestFitness + " " + averageFitness + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void processResults(String logPrefix, String resultsName, int repeats, int maxGen, int popSize) {
        Vector<Vector<Double>> bestFitnesses = new Vector<Vector<Double>>();
        try {
            for (int i = 0; i < repeats; i++) {
                Vector<Double> column = new Vector<Double>();

                BufferedReader in = new BufferedReader(new FileReader(logPrefix + "." + i));

                String line;
                while ((line = in.readLine()) != null) {
                    double best = Double.parseDouble(line.split(" ")[0]);
                    column.add(best);
                }
                in.close();

                bestFitnesses.add(column);
            }

            FileWriter out = new FileWriter(resultsName);

            for (int j = 0; j < maxGen; j++) {
                double sum = 0;
                Vector<Double> nums = new Vector<Double>();
                for (int i = 0; i < repeats; i++) {
                    double current = bestFitnesses.get(i).get(j);
                    nums.add(current);
                    sum += current;
                }

                double avg = sum / repeats;

                Collections.sort(nums);
                double min = nums.get(0);
                double max = nums.get(nums.size()-1);
                double q1 = nums.get(nums.size()/4);
                double q3 = nums.get(3*nums.size()/4);

                out.write((j+2)*popSize + " " + min + " " + q1 + " " + avg + " " + q3 + " " + max
                        + System.getProperty("line.separator"));

            }
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}