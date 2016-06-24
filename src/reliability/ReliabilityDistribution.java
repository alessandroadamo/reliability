package reliability;

import java.io.Serializable;

/**
 * Reliability distribution class
 *
 */
public abstract class ReliabilityDistribution implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7245887703531993664L;

    /**
     * @brief Probability density function
     *
     * Evaluate the probability density function
     *
     * @param x point
     *
     * @return the evaluated probability distribution function
     *
     */
    public abstract double pdf(double x);

    /**
     * @brief Generate random sample distributed
     *
     * Generate random sample distributed as the distribution as described by
     * the class
     *
     * @return the vector of random samples
     *
     */
    public abstract double random();

    /**
     * @brief Probability density function
     *
     * Evaluate the probability density function for a vector
     *
     * @param x vector points
     *
     * @return the evaluated probability distribution function
     *
     */
    public double[] pdf(double[] x) {

        double[] val = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            val[i] = pdf(x[i]);
        }

        return val;

    }

    /**
     * @brief Cumulative distribution function
     *
     * Evaluate the cumulative distribution function
     *
     * @param x point
     *
     * @return the evaluated cumulative distribution function
     *
     */
    public abstract double cdf(double x);

    /**
     * @brief Cumulative distribution function
     *
     * Evaluate the cumulative distribution function for a vector
     *
     * @param x vector points
     *
     * @return the evaluated cumulative distribution function
     *
     */
    public double[] cdf(double[] x) {

        double[] val = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            val[i] = cdf(x[i]);
        }

        return val;

    }

    /**
     * @brief Generate random samples distributed
     *
     * Generate random samples vector distributed as the distribution as
     * described by the class
     *
     * @param n
     *
     * @return the vector of random samples
     *
     * @exception IllegalArgumentException number of samples must be greater
     * than 0
     *
     */
    public double[] random(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("Number of samples must be greater than 0!");
        }

        double[] val = new double[n];

        for (int i = 0; i < n; i++) {
            val[i] = random();
        }

        return val;

    }

    /**
     * @brief Reliability function
     *
     * Evaluate the reliability function
     *
     * @param x point
     *
     * @return the evaluated reliability function
     *
     */
    public double reliability(double x) {

        return 1.0 - cdf(x);

    }

    /**
     * @brief Reliability function
     *
     * Evaluate the reliability function for a vector
     *
     * @param x vector points
     *
     * @return the evaluated reliability function
     *
     */
    public double[] reliability(double[] x) {

        double[] val = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            val[i] = reliability(x[i]);
        }

        return val;

    }

    /**
     * @brief Conditional Reliability function
     *
     * Evaluate the conditional reliability function. The Conditional
     * reliability function is defined as the probability P(X+x | X)
     *
     * @param x point
     *
     * @param X point
     *
     * @return the evaluated reliability function
     *
     */
    public double conditionalReliability(double x, double X) {

        return reliability(X + x) / reliability(X);

    }

    /**
     * @brief Conditional Reliability function
     *
     * Evaluate the conditional reliability function for a vector.
     *
     * @param x vector points
     *
     * @param X vector points
     *
     * @return the evaluated reliability function
     *
     */
    public double[] conditionalReliability(double[] x, double[] X) {

        double[] val = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            val[i] = conditionalReliability(x[i], X[i]);
        }

        return val;

    }

    /**
     * @brief Hazard function
     *
     * Evaluate the hazard function. The hazard function is defined as the ratio
     * between the probability density function and the reliability function.
     *
     * @param x point
     *
     * @return the evaluated hazard function
     *
     */
    public double hazard(double x) {

        return pdf(x) / reliability(x);

    }

    /**
     * @brief Hazard function
     *
     * Evaluate the hazard function for a vector. The hazard function is defined
     * as the ratio between the probability density function and the reliability
     * function.
     *
     * @param x vector points point
     *
     * @return the evaluated hazard function
     *
     */
    public double[] hazard(double[] x) {

        double[] val = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            val[i] = hazard(x[i]);
        }

        return val;

    }

}
