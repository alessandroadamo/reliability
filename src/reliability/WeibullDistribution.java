package reliability;

/**
 * Weibull distribution class
 *
 */
public class WeibullDistribution extends ReliabilityDistribution {

    private static final long serialVersionUID = 3357607630022750212L;

    private static int DEFAULT_MAX_ITERATIONS = 100;
    private static double DEFAULT_MIN_TOL = 1e-6;
    private static double ILL_CONDITIONED_THRESHOLD = 1e-8;

    private double shape;
    private double scale;

    /**
     * @brief Weibull distribution default class constructor
     *
     * Weibull distribution default class constructor. The distribution has both
     * shape and scale parameters equal to 1.0.
     *
     */
    public WeibullDistribution() {

        this(1.0, 1.0);

    }

    /**
     * @brief Weibull distribution class constructor
     *
     * Weibull distribution class constructor
     *
     * @param shape shape parameter
     * @param scale scale parameter
     *
     * @exception IllegalArgumentException The shape parameter is less than 0.0
     * @exception IllegalArgumentException The scale parameter is less than 0.0
     *
     */
    public WeibullDistribution(double shape, double scale)
            throws IllegalArgumentException {

        if (shape <= 0.0) {
            throw new IllegalArgumentException(
                    "Shape parameter must be greater than 0.0");
        }
        if (scale <= 0.0) {
            throw new IllegalArgumentException(
                    "Scale parameter must be greater than 0.0");
        }

        this.scale = scale;
        this.shape = shape;

    }

    /**
     * @brief Get shape parameter
     *
     * Get shape parameter
     *
     * @return shape parameter
     *
     */
    public double getShape() {

        return shape;

    }

    /**
     * @brief Get scale parameter
     *
     * Get scale parameter
     *
     * @return scale parameter
     *
     */
    public double getScale() {

        return scale;

    }

    @Override
    public double pdf(double x) {

        double val = 0.0;

        if (x >= 0.0) {
            val = shape / scale * Math.pow(x / scale, shape - 1.0)
                    * Math.exp(-Math.pow(x / scale, shape));
        }

        return val;

    }

    @Override
    public double cdf(double x) {

        double val = 0.0;

        if (x > 0.0) {
            val = 1.0 - Math.exp(-Math.pow(x / scale, shape));
        }

        return val;

    }

    @Override
    public double random() {

        return scale * Math.pow(-Math.log(1.0 - Math.random()), 1.0 / shape);

    }

    @Override
    public String toString() {
        return "Weibull Distribution {\n\tshape = " + shape + "\n\tscale = " + scale + "\n}";
    }

    /**
     * @brief Estimate parameters
     *
     * Estimate shape and scale parameters given samples data
     *
     * @param data data vector
     * @param censored censoring vector, each element is true if the sample is
     * right censored, false otherwise.
     * @param maxIterations maximum number of iterations allowed
     * @param minTol minimum tolerance
     *
     * @return return a new Weibull distribution with estimated parameters
     *
     * @exception IllegalArgumentException data vector vector can not be null
     * @exception IllegalArgumentException data vector must have number of
     * elements greater than 0
     * @exception IllegalArgumentException type vector must be of the same
     * length than the data vector
     * @exception IllegalArgumentException maximum number of iterations must be
     * greater than 0
     * @exception IllegalArgumentException tolerance must be greater than 0.0
     * @exception ArithmeticException number of uncensored data must be greater
     * than 0
     * @exception ArithmeticException Newton method did not converge
     * @exception ArithmeticException Scale parameter estimation error
     *
     */
    public static ReliabilityDistribution estimate(double[] data, boolean[] censored,
            int maxIterations, double minTol) throws ArithmeticException,
            IllegalArgumentException {

        if (data == null) {
            throw new IllegalArgumentException(
                    "Data vector can not be null!");
        }

        if (data.length <= 0) {
            throw new IllegalArgumentException(
                    "Data vector must be of size greater 0!");
        }

        if (data.length != censored.length) {
            throw new IllegalArgumentException(
                    "Type vector must be of the same length than the data vector!");
        }

        if (maxIterations <= 0) {
            throw new IllegalArgumentException(
                    "Maximum number of iterations must be greater than 0!");
        }
        if (minTol <= 0.0) {
            throw new IllegalArgumentException(
                    "Tolerance must be greater than 0.0!");
        }

        int n = data.length;
        int r = 0;

        for (int i = 0; i < n; i++) {
            if (censored[i] == true) {
                r++;
            }
        }

        if (r == n) {
            throw new ArithmeticException(
                    "Number of uncensored data must be greater than 0!");
        }

        boolean conv = false; // convergence check

        double beta = 1.0;

        // Newton method
        for (int i = 0; i < maxIterations; i++) {

            double beta_old = beta;

            // Newton method
            beta = beta - g(data, censored, beta);

            if (Math.abs(beta - beta_old) / Math.abs(beta_old) <= minTol) {
                conv = true;
                break;
            }

        }

        if (conv == false) {
            throw new ArithmeticException(
                    "Newton method did not converge!");
        }

        double sum1 = 0.0, sum2 = 0.0;
        for (int i = 0; i < n; i++) {
            if (censored[i] == false) {
                sum1 += Math.pow(data[i], beta);
            } else {
                sum2 += Math.pow(data[i], beta);
            }
        }

        double alpha;   // estimate the shape value
        if (r == 0) {
            alpha = Math.pow(sum1 / (n - r), 1.0 / beta);
        } else {
            alpha = Math.pow(sum1 / (n - r) + sum2 / r, 1.0 / beta);
        }

        if (!Double.isFinite(beta)) {
            throw new ArithmeticException(
                    "Shape parameter estimation error!");
        }

        if (!Double.isFinite(alpha)) {
            throw new ArithmeticException(
                    "Scale parameter estimation error!");
        }

        return new WeibullDistribution(beta, alpha);

    }

    /**
     * @brief Estimate parameters
     *
     * Estimate shape and scale parameters given samples data
     *
     * @param data data vector
     * @param censored censoring vector, each element is true if the sample is
     * right censored, false otherwise.
     *
     * @return return a new Weibull distribution with estimated parameters
     *
     * @exception IllegalArgumentException data vector vector can not be null
     * @exception IllegalArgumentException data vector must have number of
     * elements greater than 0
     * @exception IllegalArgumentException type vector must be of the same
     * length than the data vector
     * @exception IllegalArgumentException maximum number of iterations must be
     * greater than 0
     * @exception IllegalArgumentException tolerance must be greater than 0.0
     * @exception ArithmeticException number of uncensored data must be greater
     * than 0
     * @exception ArithmeticException Newton method did not converge
     * @exception ArithmeticException Scale parameter estimation error
     *
     */
    public static ReliabilityDistribution estimate(double[] data, boolean[] censored) {

        return estimate(data, censored, DEFAULT_MAX_ITERATIONS, DEFAULT_MIN_TOL);

    }

    /**
     * @brief Estimate parameters
     *
     * Estimate shape and scale parameters given samples data
     *
     * @param data data vector
     *
     * @return return a new Weibull distribution with estimated parameters
     *
     * @exception IllegalArgumentException data vector vector can not be null
     * @exception IllegalArgumentException data vector must have number of
     * elements greater than 0
     * @exception IllegalArgumentException type vector must be of the same
     * length than the data vector
     * @exception IllegalArgumentException maximum number of iterations must be
     * greater than 0
     * @exception IllegalArgumentException tolerance must be greater than 0.0
     * @exception ArithmeticException number of uncensored data must be greater
     * than 0
     * @exception ArithmeticException Newton method did not converge
     * @exception ArithmeticException Scale parameter estimation error
     *
     */
    public static ReliabilityDistribution estimate(double[] data) {

        if (data == null) {
            throw new IllegalArgumentException(
                    "Data vector can not be null!!");
        }

        if (data.length <= 0) {
            throw new IllegalArgumentException(
                    "Data vector must be of size greater 0!");
        }

        boolean[] censored = new boolean[data.length];
        return estimate(data, censored, DEFAULT_MAX_ITERATIONS, DEFAULT_MIN_TOL);

    }

    public static ReliabilityDistribution estimate(double[] data, int maxIterations, double minTol)
            throws ArithmeticException, IllegalArgumentException {

        if (data == null) {
            throw new IllegalArgumentException(
                    "Data vector can not be null!!");
        }

        if (data.length <= 0) {
            throw new IllegalArgumentException(
                    "Data vector must be of size greater 0!");
        }

        boolean[] censored = new boolean[data.length];

        return estimate(data, censored, maxIterations, minTol);

    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////
    private static double g(double[] data, boolean[] censored, double beta) {

        int n = data.length;
        int r = 0;

        double logt = 0.0; // uncensored
        double tb = 0.0; // uncensored
        double tb_c = 0.0; // censored
        double tblogt = 0.0; // uncensored
        double tblogt_c = 0.0; // censored
        double tblog2t = 0.0; // uncensored
        double tblog2t_c = 0.0; // censored

        for (int i = 0; i < n; i++) {

            double app1 = Math.pow(data[i], beta);
            double app2 = Math.log(data[i]);

            if (censored[i] == false) {
                tb += app1;
                logt += app2;
                tblogt += app1 * app2;
                tblog2t += app1 * (app2 * app2);
            } else {
                ++r;
                tb_c += app1;
                tblogt_c += app1 * app2;
                tblog2t_c += app1 * (app2 * app2);
            }

        }

        double f;
        double df;
        if (r == 0) {

            f = n / beta + logt - (n * tblogt) / tb;

            df = -(n / (beta * beta))
                    - ((-(tblogt * tblogt) / (tb * tb))
                    + (tblog2t / tb));

        } else {

            f = (n - r) / beta + logt - ((n - r) * (tblogt + tblogt_c))
                    / (tb + tb_c);

            df = -((n - r) / (beta * beta))
                    - ((-((tblogt + tblogt_c) * (tblogt + tblogt_c)) / Math.pow(tb + tb_c, 2.0))
                    + ((tblog2t + tblog2t_c) / (tb + tb_c)));

        }

        return f / df;

    }

}
