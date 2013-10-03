package evolution.cv5.simulation;

import evolution.EvolutionaryAlgorithm;
import evolution.Population;
import evolution.cv4.Strategy;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;
import evolution.selectors.TournamentSelector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PrisonerSimulation {

    static int maxGen;
    static int popSize;
    static String logFilePrefix;
    static String resultsFile;
    static String progFilePrefix;
    static String progFile;
    static String bestPrefix;
    static int repeats = 1;
    static int maxEncounters;

    static ArrayList<Strategy> strategies;
    static ArrayList<Integer> strategyCounts;

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        Properties prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("ga-cv5-simulation.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("max_generations", "20"));
        //popSize = Integer.parseInt(prop.getProperty("population_size", "30"));
        progFilePrefix = prop.getProperty("prog_file_prefix", "prog.log");
        progFile = prop.getProperty("prog_file_results", "progress.log");
        bestPrefix = prop.getProperty("best_ind_prefix", "best");
        logFilePrefix = prop.getProperty("log_filename_prefix", "ga.log");
        resultsFile = prop.getProperty("results_filename", "ga.log");
        maxEncounters = Integer.parseInt(prop.getProperty("max_encounters", "20"));

        String inputFile = prop.getProperty("input_file", "input-simulation.txt");

        strategies = new ArrayList<Strategy>();
        strategyCounts = new ArrayList<Integer>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = in.readLine()) != null) {
                System.err.println(line);

                String[] parts = line.split("[ ]+");
                String name = parts[0];
                int count = Integer.parseInt(parts[1]);
                Strategy s = (Strategy) Class.forName("evolution.cv4.strategies." + name).newInstance();
                strategies.add(s);
                strategyCounts.add(count);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        popSize = 0;
        for (int i = 0; i < strategyCounts.size(); i++) {
            popSize += strategyCounts.get(i);
        }

        run(0);

    }


    static void run(int number) {

        Individual sampleChromosome = new IntegerIndividual(1, 0, strategies.size());
        Population pop = new Population();
        pop.setSampleIndividual(sampleChromosome);
        pop.setPopulationSize(popSize);

        EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
        ea.setFitnessFunction(new PrisonerSimulationFitness(strategies, maxEncounters));
        ea.addEnvironmentalSelector(new TournamentSelector());
        ea.addOperator(new DummyOperator());

        List chromosomes = new ArrayList();

        for (int i = 0; i < strategies.size(); i++) {
            for (int j = 0; j < strategyCounts.get(i); j++) {

                IntegerIndividual ind = (IntegerIndividual) sampleChromosome.clone();
                ind.set(0, i);
                pop.add(ind);

            }
        }

        int[] lastCounts = new int[strategyCounts.size()];
        for (int i = 0; i < strategyCounts.size(); i++) {
            lastCounts[i] = strategyCounts.get(i);
        }

        for (int i = 0; i < maxGen; i++) {
            ea.evolve(pop);

            int[] counts = new int[strategies.size()];

            for (Individual ind : pop.getSortedIndividuals()) {
                counts[(Integer) ((IntegerIndividual) ind).get(0)]++;
            }

            System.out.println("Generation " + (i + 1));

            for (int j = 0; j < lastCounts.length; j++) {
                if (lastCounts[j] > 0 && counts[j] == 0) {
                    System.out.println("Strategy \"" + strategies.get(j).getName() + "\" died out");
                }
            }

            lastCounts = counts;
        }

        for (int i = 0; i < lastCounts.length; i++) {
            if (lastCounts[i] > 0) {
                System.out.println("Strategy \"" + strategies.get(i).getName() + "\" survived with " + lastCounts[i] + " individuals");
            }

        }
    }

}
