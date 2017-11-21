package fractalgraphics;

import java.awt.Color;

public class FractalGUIConfig {



    @Override
	public String toString() {
		return "FractalGUIConfig [xResolution=" + xResolution + ", yResolution=" + yResolution + ", minReal=" + minReal
				+ ", maxReal=" + maxReal + ", minImaginary=" + minImaginary + ", maxImaginary=" + maxImaginary
				+ ", maxIterations=" + maxIterations + ", radiusSquared=" + radiusSquared + ", startingColor="
				+ startingColor + ", endColor=" + endColor + "]";
	}

	private final int xResolution;
    private final int yResolution;
    private final double minReal;
    private final double maxReal;
    private final double minImaginary;
    private final double maxImaginary;
    private final int maxIterations;
    private final double radiusSquared;
    private final Color startingColor;
    private final Color endColor;

    public int getxResolution() {

        return xResolution;
    }

    public int getyResolution() {

        return yResolution;
    }

    public FractalGUIConfig(int xResolution, int yResolution, double minReal, double maxReal, double minImaginary,
            double maxImaginary, int maxIterations, double radiusSquared, Color startingColor, Color endColor) {
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
        this.startingColor = startingColor;
        this.endColor = endColor;
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

    public Color getStartingColor() {

        return startingColor;
    }

    public Color getEndColor() {

        return endColor;
    }

}
