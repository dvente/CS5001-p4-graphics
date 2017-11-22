package fractalgraphics;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import sun.misc.Queue;

public class FractalGUIController {

	private final FractalGUIConfig defaultConfig;

	private final FractalGUIModel model;
	private final FractalGUIView view;
	private FractalGUIConfig currentConfig;
	private Stack<FractalGUIConfig> configHistory;
	private Stack<FractalGUIConfig> configUndoneHistory;
	private Queue<FractalGUIConfig> animationQueue;
	private final int animationSeconds = 2;
	private final int frameRate = 24;
	private final int animationFrames = 2 * frameRate;

	public static void main(String[] args) {

		new FractalGUIController();
	}

	public FractalGUIController() {
//		defaultConfig = new FractalGUIConfig(FractalGUIView.DEFAULT_X_RESOLUTION, FractalGUIView.DEFAULT_Y_RESOLUTION,
//				MandelbrotCalculator.INITIAL_MIN_REAL, MandelbrotCalculator.INITIAL_MAX_REAL,
//				MandelbrotCalculator.INITIAL_MIN_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
//				MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED,
//				new ColorMapping(MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
//						new Color[] { new Color(0, 7, 100), new Color(32, 107, 203), new Color(237, 255, 255),
//								new Color(255, 170, 0), new Color(0, 2, 0) }));
		
		
//		defaultConfig = new FractalGUIConfig(FractalGUIView.DEFAULT_X_RESOLUTION, FractalGUIView.DEFAULT_Y_RESOLUTION,
//				1, 7,
//				1,7,
//				MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED,
//				new ColorMapping(MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
//						new Color[] { new Color(0, 7, 100), new Color(32, 107, 203), new Color(237, 255, 255),
//								new Color(255, 170, 0), new Color(0, 2, 0) }));
		defaultConfig = new FractalGUIConfig(FractalGUIView.DEFAULT_X_RESOLUTION, FractalGUIView.DEFAULT_Y_RESOLUTION,
				MandelbrotCalculator.INITIAL_MIN_REAL, MandelbrotCalculator.INITIAL_MAX_REAL,
				MandelbrotCalculator.INITIAL_MIN_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
				MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED,
				new ColorMapping(MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
						new Color[] { Color.WHITE, Color.BLACK}));

		currentConfig = defaultConfig;
		model = new FractalGUIModel(defaultConfig);
		view = new FractalGUIView(this, model.calcModel());
		model.addObserver(view);

		configHistory = new Stack<FractalGUIConfig>();
		configUndoneHistory = new Stack<FractalGUIConfig>();
		animationQueue = new Queue<FractalGUIConfig>();

	}
	
	public void playZoomAnimation() {
		for (int i = 0; i < animationFrames; i++) {
			animationQueue.enqueue(centreScale(0.6f));
		}
		
		while(!animationQueue.isEmpty()) {
			try {
				applyNewConfig(animationQueue.dequeue(), false);
				view.repaint();
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	public boolean hasConfigHistory() {

		return !configHistory.isEmpty();
	}

	public boolean hasConfigUndoneHistory() {

		return configHistory!= null && !configUndoneHistory.isEmpty();
	}

	public double realFromScreenX(int x) {

		return currentConfig.getMinReal()
				+ (((x * (currentConfig.getMaxReal() - currentConfig.getMinReal())) / currentConfig.getxResolution()));
	}

	public double imaginaryFromScreenY(int y) {

		return currentConfig.getMinImaginary()
				+ ((y * (currentConfig.getMaxImaginary() - currentConfig.getMinImaginary()))
						/ currentConfig.getxResolution());
	}

	public int screenYFromImaginary(double im) {
		return (int) Math.round((currentConfig.getyResolution() * (im - currentConfig.getMinImaginary()))
				/ (currentConfig.getMaxImaginary() - currentConfig.getMinImaginary()));
	}

	public int screenXFromReal(double real) {
		return (int) Math.round((currentConfig.getxResolution() * (real - currentConfig.getMinReal()))
				/ (currentConfig.getMaxReal() - currentConfig.getMinReal()));
	}

	public void reset() {

		applyNewConfig(defaultConfig, true);

	}

	public void redo() {

		configHistory.push(currentConfig);
		applyNewConfig(configUndoneHistory.pop(), false);

	}

	public void undo() {

		configUndoneHistory.push(currentConfig);
		applyNewConfig(configHistory.pop(), false);

	}

	public void save(File file) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
			oos.writeObject(currentConfig);
			oos.writeObject(configHistory);
			oos.writeObject(configUndoneHistory);

		}

	}

	public void load(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis);) {
			currentConfig = (FractalGUIConfig) ois.readObject();
			configHistory = (Stack<FractalGUIConfig>) ois.readObject();
			configUndoneHistory = (Stack<FractalGUIConfig>) ois.readObject();
			applyNewConfig(currentConfig, false);
		}

	}

	public void applyNewConfig(FractalGUIConfig newConfig, boolean record) {

		// Store config for undo
		if (record) {
			configHistory.push(currentConfig);
		}

		// reset to default
		currentConfig = newConfig;
		// update model
		model.setCurrentConfig(newConfig);
		// reset window
		view.setSize(newConfig.getxResolution(), newConfig.getyResolution());
		view.setMaxIterationsText(newConfig.getMaxIterations());

	}

	public FractalGUIConfig recentre(int leftX, int upperY, int rightX, int lowerY) {

		return new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(), realFromScreenX(leftX),
				realFromScreenX(rightX), imaginaryFromScreenY(lowerY), imaginaryFromScreenY(upperY),
				currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getColorMapping());

	}

	public FractalGUIConfig centreScale(float factor) {

		assert factor > 0;
		double width = currentConfig.getMaxReal() - currentConfig.getMinReal();
		double height = currentConfig.getMaxImaginary() - currentConfig.getMinImaginary(); 
		
		return new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(),
				(currentConfig.getCentreReal()-(width*0.5*factor)), 
				(currentConfig.getCentreReal()+(width*0.5*factor)),
				(currentConfig.getCentreImaginary()-(height*0.5*factor)), 
				(currentConfig.getCentreImaginary()+(height*0.5*factor)),
				currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getColorMapping());

	}
	
	public void applyRecentre(int leftX, int upperY, int rightX, int lowerY) {

		assert leftX < rightX;
		assert upperY > lowerY;
		assert screenXFromReal(realFromScreenX(leftX)) == leftX;
		assert screenYFromImaginary(imaginaryFromScreenY(upperY)) == upperY;
		assert screenXFromReal(realFromScreenX(rightX)) == rightX;
		assert screenYFromImaginary(imaginaryFromScreenY(lowerY)) == lowerY;
		applyNewConfig(recentre(leftX, upperY, rightX, lowerY),true);

	}

	public void applyCentreScale(float factor) {
		
		applyNewConfig(centreScale(factor),	true);

	}

	public int getMaxIterations() {

		return currentConfig.getMaxIterations();
	}

	public void setMaxIterations(int newMaxIterations) {
		ColorMapping newColorMapping = new ColorMapping(newMaxIterations,
				currentConfig.getColorMapping().getColorValues());

		applyNewConfig(new FractalGUIConfig(currentConfig.getxResolution(), currentConfig.getyResolution(),
				currentConfig.getMinReal(), currentConfig.getMaxReal(), currentConfig.getMinImaginary(),
				currentConfig.getMaxImaginary(), newMaxIterations, currentConfig.getRadiusSquared(), newColorMapping),
				true);

	}

	public ColorMapping getColorMapping() {

		return currentConfig.getColorMapping();
	}

	public void exportToPNG(File file) throws IOException {

		ImageIO.write(view.getBufferedImageFromCanvas(), "png", file);

	}

	public FractalGUIConfig getCurrentConfig() {
		return currentConfig;
	}

}
