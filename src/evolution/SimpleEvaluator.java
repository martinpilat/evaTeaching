package evolution;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * A simple instance of the FitnessEvaluator class. It takes as a parameter
 * a fitness function and uses it to assign the fitness to all individuals
 * in the population.
 *
 * @author Martin Pilat
 */
public class SimpleEvaluator implements FitnessEvaluator {

    FitnessFunction fitness;
    ForkJoinPool forkJoinPool;
    int cores;

    /**
     * Creates a SimpleEvaluator which uses the fitness specified and assigns
     * the value provided by this fitness to all the individuals in the population.
     *
     * @param fitness The fitness which shall be used during the evaluation.
     */

    public SimpleEvaluator(FitnessFunction fitness) {
        this(fitness, 1);
    }

    /**
     * Creates a SimpleEvaluator which uses the fitness specified and assigns
     * the value provided by this fitness to all the individuals in the population.
     * Uses parallel fitness evaluation with the specified number of threads.
     *
     * @param fitness The fitness which shall be used during the evaluation.
     * @param cores The number of CPU cores to use
     */

    public SimpleEvaluator(FitnessFunction fitness, int cores) {
        this.fitness = fitness;
        if (cores < -1 || cores == 0 )
            throw new InvalidParameterException("cores must be > 0, or -1");
        this.cores = cores;
        setCores(cores);
    }

    public void setCores(int cores) {
        this.cores = cores;
        if (cores > 1) {
            this.forkJoinPool = new ForkJoinPool(this.cores);
        }
        if (cores == -1) {
            this.forkJoinPool = new ForkJoinPool();
        }

    }

    public void evaluate(Population pop) {

        if (this.cores == 1)
            for (int i = 0; i < pop.getPopulationSize(); i++) {
                pop.get(i).setFitnessValue(fitness.evaluate(pop.get(i)));
            }
        else {
            List<Double> fits = null;
            try {
                fits = forkJoinPool.submit(() ->
                        pop.stream()
                                .parallel()
                                .map(fitness::evaluate)
                                .collect(Collectors.toList())
                ).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            int i = 0;
            for (double f: fits) {
                pop.get(i).setFitnessValue(f);
                i++;
            }
        }
    }

}
