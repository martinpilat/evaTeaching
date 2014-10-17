package evolution.multi;

import evolution.*;
import evolution.individuals.MultiRealIndividual;
import evolution.individuals.RealIndividual;
import evolution.multi.functions.*;
import evolution.operators.AveragingCrossoverOperator;
import evolution.operators.GaussianMutationOperator;

import java.io.*;
import java.util.*;

public class Multi {

    static int maxGen;
    static int popSize;
    static int dimension;
    static double xoverProb;
    static double mutProb;
    static double mutSigma;
    static String logFilePrefix;
    static int repeats;
    static String bestPrefix;
    static Properties prop;
    static String outputDirectory;
    static String objectiveFilePrefix;
    static String objectiveStatsFile;
    static String fitnessFilePrefix;
    static String fitnessStatsFile;
    static String detailsLogPrefix;

    public static void main(String[] args) {

        prop = new Properties();
        try {
            InputStream propIn = new FileInputStream("properties/ga-multi.properties");
            prop.load(propIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxGen = Integer.parseInt(prop.getProperty("ea.maxGenerations", "20"));
        popSize = Integer.parseInt(prop.getProperty("ea.popSize", "30"));
        xoverProb = Double.parseDouble(prop.getProperty("ea.xoverProb", "0.8"));
        mutProb = Double.parseDouble(prop.getProperty("ea.mutProb", "0.05"));
        mutSigma = Double.parseDouble(prop.getProperty("ea.mutSigma", "0.04"));

        dimension = Integer.parseInt(prop.getProperty("prob.dimension", "25"));
        repeats = Integer.parseInt(prop.getProperty("xset.repeats", "10"));

        DetailsLogger.disableLog();

        ArrayList<MultiObjectiveFunction> mofs = new ArrayList<MultiObjectiveFunction>();
        mofs.add(new ZDT1());
        mofs.add(new ZDT2());
        mofs.add(new ZDT3());
        mofs.add(new ZDT4());
        mofs.add(new ZDT6());


        for (MultiObjectiveFunction mof : mofs) {

            outputDirectory = prop.getProperty("xlog.outputDirectory", "multi") + System.getProperty("file.separator") + mof.getClass().getSimpleName();
            logFilePrefix = prop.getProperty("xlog.filePrefix", "log");
            String path = outputDirectory  + System.getProperty("file.separator") + logFilePrefix;
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
                RandomNumberGenerator.getInstance().reseed(i);
                run(i, mof);
            }

            StatsLogger.processResults(fitnessFilePrefix, fitnessStatsFile, repeats, maxGen, popSize);
            StatsLogger.processResults(objectiveFilePrefix, objectiveStatsFile, repeats, maxGen, popSize);
        }
    }

    static void run(int number, MultiObjectiveFunction mof) {

        try {

            RealIndividual sample = new MultiRealIndividual(dimension, 0.0, 1.0);

            Population pop = new Population();
            pop.setPopulationSize(popSize);
            pop.setSampleIndividual(sample);
            pop.createRandomInitialPopulation();

            EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
            ea.addOperator(new AveragingCrossoverOperator(xoverProb));
            ea.addOperator(new GaussianMutationOperator(mutProb, mutSigma));
            ea.setFitnessFunction(new MultiObjectiveFitnessFunction(mof));
            ea.addEnvironmentalSelector(new NSGA2Selector());
            ea.setReplacement(new MergingReplacement());

            OutputStreamWriter fitnessOut = new OutputStreamWriter(new FileOutputStream(fitnessFilePrefix + "." + number));
            OutputStreamWriter objectiveOut = new OutputStreamWriter(new FileOutputStream(objectiveFilePrefix + "." + number));

            for (int i = 0; i < maxGen; i++) {
                ea.evolve(pop);
                MultiRealIndividual mri = (MultiRealIndividual)pop.getSortedIndividuals().get(0);
                double hypervolume = mof.getOptimalHypervolume() - MultiObjectiveUtils.calculateHypervolume(pop, mof.getReferencePoint());
                mri.setObjectiveValue(hypervolume);
                if (i % 100 == 0) {
                    System.out.println("Generation " + i + ": "  + hypervolume);
                    }
                StatsLogger.logFitness(pop, fitnessOut);
                StatsLogger.logObjective(pop, objectiveOut);
            }

            double hypervolume = mof.getOptimalHypervolume() - MultiObjectiveUtils.calculateHypervolume(pop, mof.getReferencePoint());
            System.out.println("End: "  + hypervolume);

            OutputStreamWriter bestOut = new OutputStreamWriter(new FileOutputStream(bestPrefix + "." + number));

            for (int i = 0; i < pop.getPopulationSize(); i++) {
                MultiObjectiveUtils.printIndividual((MultiRealIndividual)pop.get(i), bestOut);
            }

            fitnessOut.close();
            objectiveOut.close();
            bestOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
