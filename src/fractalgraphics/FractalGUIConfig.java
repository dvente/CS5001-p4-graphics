package fractalgraphics;

import java.io.Serializable;
import java.util.Observable;

/**
 * The Class FractalGUIConfig. Contaings all parameters needed to draw a frame
 *
 * @author 170008773
 */
public class FractalGUIConfig extends Observable implements Serializable {

    private static final double HALF_FACTOR = 0.5;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8773640144745735110L;

    /** The x resolution. */
    private final int xResolution;

    /** The y resolution. */
    private final int yResolution;

    /** The min real. */
    private final double minReal;

    /** The max real. */
    private final double maxReal;

    /** The min imaginary. */
    private final double minImaginary;

    /** The max imaginary. */
    private final double maxImaginary;

    /** The max iterations. */
    private final int maxIterations;

    /** The radius squared. */
    private final double radiusSquared;

    /** The color maping. */
    private final ColorMapping colorMaping;

    /**
     * Instantiates a new fractal GUI config.
     *
     * @param xResolution
     *            the x resolution
     * @param yResolution
     *            the y resolution
     * @param minReal
     *            the min real
     * @param maxReal
     *            the max real
     * @param minImaginary
     *            the min imaginary
     * @param maxImaginary
     *            the max imaginary
     * @param maxIterations
     *            the max iterations
     * @param radiusSquared
     *            the radius squared
     * @param colorMapping
     *            the color mapping
     */
    public FractalGUIConfig(int xResolution, int yResolution, double minReal, double maxReal, double minImaginary,
            double maxImaginary, int maxIterations, double radiusSquared, ColorMapping colorMapping) {
        super();
        assert maxReal > minReal;
        assert maxImaginary > minImaginary;
        assert xResolution > 1;
        assert yResolution > 1;
        assert maxIterations > 2;
        this.xResolution = xResolution;
        this.yResolution = yResolution;
        this.maxReal = maxReal;
        this.maxImaginary = maxImaginary;
        this.minReal = minReal;
        this.minImaginary = minImaginary;
        this.maxIterations = maxIterations;
        this.radiusSquared = radiusSquared;
        this.colorMaping = colorMapping;
    }

    /**
     * Gets the centre imaginary.
     *
     * @return the centre imaginary
     */
    public double getCentreImaginary() {

        return (HALF_FACTOR * (maxImaginary - minImaginary)) + minImaginary;
    }

    /**
     * Gets the centre real.
     *
     * @return the centre real
     */
    public double getCentreReal() {

        return (HALF_FACTOR * (maxReal - minReal)) + minReal;
    }

    /**
     * Gets the color mapping.
     *
     * @return the color mapping
     */
    public ColorMapping getColorMapping() {

        return colorMaping;
    }

    /**
     * Gets the max imaginary.
     *
     * @return the max imaginary
     */
    public double getMaxImaginary() {

        return maxImaginary;
    }

    /**
     * Gets the max iterations.
     *
     * @return the max iterations
     */
    public int getMaxIterations() {

        return maxIterations;
    }

    /**
     * Gets the max real.
     *
     * @return the max real
     */
    public double getMaxReal() {

        return maxReal;
    }

    /**
     * Gets the min imaginary.
     *
     * @return the min imaginary
     */
    public double getMinImaginary() {

        return minImaginary;
    }

    /**
     * Gets the min real.
     *
     * @return the min real
     */
    public double getMinReal() {

        return minReal;
    }

    /**
     * Gets the radius squared.
     *
     * @return the radius squared
     */
    public double getRadiusSquared() {

        return radiusSquared;
    }

    /**
     * Gets the x resolution.
     *
     * @return the x resolution
     */
    public int getxResolution() {

        return xResolution;
    }

    /**
     * Gets the y resolution.
     *
     * @return the y resolution
     */
    public int getyResolution() {

        return yResolution;
    }

    @Override
    public String toString() {

        return "FractalGUIConfig [xResolution=" + xResolution + ", yResolution=" + yResolution + ", minReal=" + minReal
                + ", maxReal=" + maxReal + ", minImaginary=" + minImaginary + ", maxImaginary=" + maxImaginary
                + ", maxIterations=" + maxIterations + ", radiusSquared=" + radiusSquared + ", colorMapping= "
                + colorMaping + "]";
    }
}
