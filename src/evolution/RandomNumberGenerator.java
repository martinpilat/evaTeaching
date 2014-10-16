package org.pikater.core.utilities.evolution;

import java.util.Random;

/** Wrapper for the java.util.Random. Provides a singleton random number generator.
 *
 * @author Martin Pilat
 */
public class RandomNumberGenerator {

    private static Random rnd;
    private static RandomNumberGenerator theInstance = null;

    private RandomNumberGenerator() {
        rnd = new Random();
    }

    /**
     * Returns the only instance of random number generator.
     *
     * @return The random number generator.
     */

    public static RandomNumberGenerator getInstance() {

        if (theInstance == null) {
            theInstance = new RandomNumberGenerator();
        }
        return theInstance;

    }

    /**
     * Returns a random integer between 0 and n - 1 (inclusive).
     *
     * @param n The upper limit
     * @return Random integer between 0 and n - 1 drawn from a uniform distribution.
     */

    public int nextInt(int n) {
        return rnd.nextInt(n);
    }

    /**
     *
     * @return A random double from the interval [0, 1) drawn from a uniform distribution.
     */

    public double nextDouble() {
        return rnd.nextDouble();
    }

    /**
     * Returns the next pseudorandom, Gaussian ("normally") distributed double
     * value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
     *
     * @return The number from Gaussian distribution
     */
    public double nextGaussian() {
        return rnd.nextGaussian();
    }

    /* write to static field from instance method
     ** Sets a new seed for the random number generator.
     * @param seed The seed which shall be set.
    public void reseed(long seed) {
        rnd = new Random(seed);
    } */

    public Random getRandom() {
        return rnd;
    }

}
