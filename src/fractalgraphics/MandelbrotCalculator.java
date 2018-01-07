package fractalgraphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The MandelbrotCalculator class contains methods which establish the
 * Mandelbrot set. The calcMandelbrotSet method below iterates over X,Y
 * positions and establishes for specified parameter values a 2-D array
 * containing for each [y][x] pixel an iteration value that may be mapped to
 * colour and used to visualise the Mandelbrot set. The calcMandelbrotSet method
 * iteratively calls for each X,Y coordinate, the calcMandel method (also
 * included here) which establishes the iteration value for a particular X,Y
 * coordinate. Example usage -- To obtain a 800x800 2-D array of Mandelbrot set
 * values for the initial parameter values MandelbrotCalculator mandelCalc = new
 * MandelbrotCalculator(); int[][] madelbrotData =
 * mandelCalc.calcMandelbrotSet(800, 800, MandelbrotCalculator.INITIAL_MIN_REAL,
 * MandelbrotCalculator.INITIAL_MAX_REAL,
 * MandelbrotCalculator.INITIAL_MIN_IMAGINARY,
 * MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
 * MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
 * MandelbrotCalculator.DEFAULT_RADIUS_SQUARED);
 *
 * @author jonl
 */
public class MandelbrotCalculator {

	// Initial parameter values
	protected static final double INITIAL_MIN_REAL = -2.0;
	protected static final double INITIAL_MAX_REAL = 0.5;
	protected static final double INITIAL_MIN_IMAGINARY = -1.25;
	protected static final double INITIAL_MAX_IMAGINARY = 1.25;
	protected static final int INITIAL_MAX_ITERATIONS = 50;

	// Default parameter values
	protected static final double DEFAULT_RADIUS_SQUARED = 4.0;
	

	private ThreadPoolExecutor executor;
	List<Future<int[][]>> renderedBands = new ArrayList<Future<int[][]>>();

	/**
	 * Method which calculates the number of iterations over which Z_n+1 = Z_n^2 + C
	 * can be applied for Z starting at the origin and a specific constant C (given
	 * by its Real and cImaginary components). If the square of the absolute value
	 * of Z is still inside the defined squaredRadius after maxIterations then we
	 * stop iterating and return maxIterations, implicitly assuming that Z will
	 * never escape the radius for the given setting of the constant C. In this
	 * case, the value of C is treated as being part of the Mandelbrot set, i.e. the
	 * set of starting constants C for which the value of Z remains bounded within
	 * the complex plane under iteration.
	 *
	 * @param cReal
	 *            the real component (akin to X component) of the constant C.
	 * @param cImaginary
	 *            the imaginary component (akin to Y component) of the constant C.
	 * @param maxIterations
	 *            the maximum number of iterations over which to iterate the
	 *            equation until assuming Z will remain bounded.
	 * @param radiusSquared
	 *            the squared of the radius to use when determining whether Z
	 *            escaped the circle in the complex plain or remained bounded. The
	 *            value used is commonly 4.0.
	 * @return the number iterations for the value of Z to grow outside of the
	 *         bounding radius, or maxIterations if it never escaped.
	 */
	public static int calcMandel(double cReal, double cImaginary, int maxIterations, double radiusSquared) {

		// To work out Z_n+1 = Z_n^2 + C and establish whether C is in the Mandelbrot
		// set or not
		// we need to
		// square the current value of Z
		// add C
		// and then check whether the square of the length of Z is outside the given
		// radiusSquared
		// Notes for squaring the complex number Z below
		// Z = zr + i*zi
		// C = cr + i*ci
		// Z^2 = zr^2 + 2zr*i*zi + i^2*zi^2 = (zr^2 - zi^2) + i*2zr*zi
		int iterations = 0;
		double zr = 0;
		double zi = 0;
		boolean outside = false;
		while (iterations < maxIterations && !outside) {
			double zr2 = zr * zr;
			double zi2 = zi * zi;
			double nzr = zr2 - zi2 + cReal;
			double nzi = 2 * zr * zi + cImaginary;
			zr = nzr;
			zi = nzi;
			if ((zr2 + zi2) > radiusSquared) {
				outside = true;
			}
			iterations++;
		}
		return iterations;
	}

	/**
	 * Method to calculate the Mandelbrot set for the given parameter settings.
	 * 
	 * @param xResolution
	 *            the number of pixels on the x-axis in your GUI display.
	 * @param yResolution
	 *            the number of pixels on the y-axis in your GUI display.
	 * @param minReal
	 *            the lower real bound for the complex constant C (equivalent to
	 *            lower bound X value in Mandelbrot set)
	 * @param maxReal
	 *            the upper real bound for the complex constant C (equivalent to
	 *            upper bound X value in Mandelbrot set)
	 * @param minImaginary
	 *            the lower imaginary bound for the complex constant C (equivalent
	 *            to lower bound Y value in Mandelbrot set)
	 * @param maxIterations
	 *            the maximum number of iterations to iterate the complex formula
	 * @param radiusSquared
	 *            the squared of the radius to use when determining whether Z
	 *            escaped the circle in the complex plain or remained bounded.
	 * @return the 2-D integer array mandelbrotData[yResolution][xResolution]
	 *         containing the for each [y][x] pixel the number of iterations needed
	 *         until Z escaped the bounding radius, or maxIterations otherwise.
	 */
	public int[][] calcMandelbrotSet(int xResolution, int yResolution, double minReal, double maxReal,
			double minImaginary, double maxImaginary, int maxIterations, double radiusSquared) {

		int[][] mandelbrotData = new int[yResolution][xResolution];
		double realRange = maxReal - minReal;

		double realStep = (maxReal - minReal) / xResolution;
		double imaginaryStep = (maxImaginary - minImaginary) / yResolution;
		System.out.print(String.format("%-15s", "sequential: "));
		for (int y = 0; y < yResolution; y++) {
			double cImaginary = minImaginary + y * imaginaryStep;
			for (int x = 0; x < xResolution; x++) {
				double cReal = minReal + x * realStep;
				System.out.print(String.format("(%f,%f)", cReal, cImaginary));
				mandelbrotData[y][x] = calcMandel(cReal, cImaginary, maxIterations, radiusSquared);
				cReal = cReal + realStep;
			}
		}
		return mandelbrotData;
	}
	
	/*
	 * following code written by Daniel Vente
	 */
	
	
	public int[][] calcMandelbrotSetThreaded(int xResolution, int yResolution, double minReal, double maxReal,
			double minImaginary, double maxImaginary, int maxIterations, double radiusSquared) {
		int[][] mandelbrotData = new int[yResolution][xResolution];
		int numbOfBands = executor.getMaximumPoolSize();
		double realStep = (maxReal - minReal) / xResolution;
		double imaginaryStep = (maxImaginary - minImaginary) / yResolution;
		double bandHeight = (maxImaginary - minImaginary) / numbOfBands;
		int bandRowCount = yResolution / numbOfBands;
		System.out.print(String.format("%15s", "Threaded: "));
		assert numbOfBands*(yResolution/numbOfBands) == yResolution;
		for (int i = 0; i < numbOfBands; i++){
//			System.out.println(String.format("Band %d ImRange: %f-%f",i,minImaginary + (i * bandRowCount * imaginaryStep),minImaginary + ((i+1) * bandRowCount * imaginaryStep)));
//			System.out.println(String.format("Band %d RealRange: %f-%f",i,minReal,minReal +(xResolution*realStep)));
			
            MandelbrotRenderTask band = new MandelbrotRenderTask(i,minReal, minImaginary + (i * bandRowCount * imaginaryStep),xResolution,yResolution/numbOfBands,realStep,imaginaryStep,maxIterations,radiusSquared);
            renderedBands.add(executor.submit(band));
        }
		System.out.println();
		
		for(int bandNumb = 0; bandNumb < numbOfBands; bandNumb++) {
			int[][] band = null;
			try {
				band = renderedBands.get(bandNumb).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < band.length; i++) {
				for (int j = 0; j < band[0].length; j++) {
					mandelbrotData[i + bandNumb * bandRowCount][j] = band[i][j];
				}
			}
		
//		for(int bandNumb = 0; bandNumb < numbOfBands; bandNumb++) {
//			int[][] band = calcMandelbrotBand(minReal, minImaginary + (bandNumb * bandHeight),
//											  xResolution,yResolution/numbOfBands,
//											  realStep, imaginaryStep,
//											  maxIterations, radiusSquared);
//		
//			for (int i = 0; i < band.length; i++) {
//				for (int j = 0; j < band[0].length; j++) {
//					mandelbrotData[i + bandNumb * bandRowCount][j] = band[i][j];
//				}
//			}
		}

		return mandelbrotData;
		
	}

	
	
	public int[][] calcMandelbrotBand(double minReal, double minIm, int numbOfRealSteps, int numbOfImSteps, double realStepLength,
			double imStepLength, int maxIterations, double radiusSquared) {

		int[][] band = new int[numbOfImSteps][numbOfRealSteps];

		for (int y = 0; y < numbOfImSteps; y++) {
			double cImaginary = minIm + y * imStepLength;
			for (int x = 0; x < numbOfRealSteps; x++) {
				double cReal = minReal + x * realStepLength;
				band[y][x] = calcMandel(cReal, cImaginary, maxIterations, radiusSquared);
				cReal = cReal + realStepLength;
			}
		}
	
		return band;
	}

	public MandelbrotCalculator(int numbOfThreads) {
		super();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numbOfThreads);
	}
	
	

}
