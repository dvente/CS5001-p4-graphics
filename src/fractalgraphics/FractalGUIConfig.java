package fractalgraphics;

import java.io.Serializable;

public class FractalGUIConfig implements Serializable{

    @Override
    public String toString() {

        return "FractalGUIConfig [xResolution=" + xResolution + ", yResolution=" + yResolution + ", minReal=" + minReal
                + ", maxReal=" + maxReal + ", minImaginary=" + minImaginary + ", maxImaginary=" + maxImaginary
                + ", maxIterations=" + maxIterations + ", radiusSquared=" + radiusSquared + ", colorMapping= "
                + colorMaping + "]";
    }

    private final int xResolution;
    private final int yResolution;
    private final double minReal;
    private final double maxReal;
    private final double minImaginary;
    private final double maxImaginary;
    private final int maxIterations;
    private final double radiusSquared;
    private final ColorMapping colorMaping;
    //    private final Color startingColor;
    //    private final Color endColor;

    public int getxResolution() {

        return xResolution;
    }

    public int getyResolution() {

        return yResolution;
    }

    public FractalGUIConfig(int xResolution, int yResolution, double minReal, double maxReal, double minImaginary,
            double maxImaginary, int maxIterations, double radiusSquared, ColorMapping colorMapping) {
        super();
        System.out.println("maxReal: " + maxReal);
        System.out.println("minReal: " + minReal);
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

    public double getMaxReal() {

        return maxReal;
    }

    public double getMaxImaginary() {

        return maxImaginary;
    }

    public double getMinReal() {

        return minReal;
    }

    public double getMinImaginary() {

        return minImaginary;
    }

    public int getMaxIterations() {

        return maxIterations;
    }

    public double getRadiusSquared() {

        return radiusSquared;
    }

    public ColorMapping getColorMapping() {

        return colorMaping;
    }
}
