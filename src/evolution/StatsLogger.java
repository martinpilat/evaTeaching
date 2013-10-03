package evolution;

import evolution.individuals.Individual;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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

}