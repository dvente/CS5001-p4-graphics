package fractalgraphics;

import java.awt.Color;

public class FractalGUIConfig {
	private final int xResolution;
	private final int yResolution;
	
	private final double maxReal;
	private final double maxImaginary;
	private final double minReal;
	private final double minImaginary; 
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


	public FractalGUIConfig(int xResolution, int yResolution, double maxReal, double maxImaginary, double minReal,
			double minImaginary, int maxIterations, double radiusSquared, Color startingColor, Color endColor) {
		super();
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
