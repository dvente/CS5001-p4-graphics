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

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class FractalGUIController {

	private final FractalGUIConfig defaultConfig;

	private final FractalGUIModel model;
	private final FractalGUIView view;
	private FractalGUIConfig currentConfig;
	private Stack<FractalGUIConfig> configHistory;
	private Stack<FractalGUIConfig> configUndoneHistory;

	public boolean hasConfigHistory() {
		return !configHistory.isEmpty();
	}

	public boolean hasConfigUndoneHistory() {
		return !configUndoneHistory.isEmpty();
	}

	public static void main(String[] args) {
		new FractalGUIController();
	}

	public FractalGUIController() {
		defaultConfig = new FractalGUIConfig(FractalGUIView.DEFAULT_X_RESOLUTION, FractalGUIView.DEFAULT_Y_RESOLUTION,
				MandelbrotCalculator.INITIAL_MIN_REAL, MandelbrotCalculator.INITIAL_MAX_REAL,
				MandelbrotCalculator.INITIAL_MIN_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
				MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED, Color.WHITE,
				Color.BLACK);

		currentConfig = defaultConfig;
		model = new FractalGUIModel(defaultConfig);
		view = new FractalGUIView(this, model.calcModel());
		model.addObserver(view);

		configHistory = new Stack<FractalGUIConfig>();
		configUndoneHistory = new Stack<FractalGUIConfig>();

	}

	public double realFromScreenX(int x) {

		return currentConfig.getMinReal() + (((double) x / currentConfig.getxResolution())
				* (currentConfig.getMaxReal() - currentConfig.getMinReal()));
	}

	public double imaginaryFromScreenY(int y) {
		return currentConfig.getMinImaginary() + (((double) y / currentConfig.getxResolution())
				* (currentConfig.getMaxImaginary() - currentConfig.getMinImaginary()));
	}

	public void reset() {
		applyNewConfig(defaultConfig, true);

	}

	public void redo() {

		configHistory.push(currentConfig);
		applyNewConfig(configUndoneHistory.pop(), true);

	}

	public void undo() {
		configUndoneHistory.push(currentConfig);
		applyNewConfig(configHistory.pop(), false);

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
		view.setInputFieldText(newConfig.getMaxIterations());
	}

	/**
	 * @param upperLeftX
	 * @param upperLeftY
	 * @param lowerRightX
	 * @param lowerRightY
	 */
	public void recentre(int leftX, int upperY, int rightX, int lowerY) {
		assert leftX < rightX;
		assert upperY > lowerY;
		System.out.println("minReal=" + realFromScreenX(leftX) + ", maxReal=" + realFromScreenX(rightX)
				+ ", minImaginary=" + imaginaryFromScreenY(lowerY) + ", maxImaginary=" + imaginaryFromScreenY(upperY));
		
		applyNewConfig(new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(), realFromScreenX(leftX),
				realFromScreenX(rightX), imaginaryFromScreenY(lowerY), imaginaryFromScreenY(upperY),
				currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getStartingColor(),
				currentConfig.getEndColor()), true);

	}

	public int getMaxIterations() {
		return currentConfig.getMaxIterations();
	}

	public void setMaxIterations(int newMaxIterations) {
		applyNewConfig(new FractalGUIConfig(currentConfig.getxResolution(), currentConfig.getyResolution(),
				currentConfig.getMinReal(), currentConfig.getMaxReal(), currentConfig.getMinImaginary(),
				currentConfig.getMaxImaginary(), newMaxIterations, currentConfig.getRadiusSquared(),
				currentConfig.getStartingColor(), currentConfig.getEndColor()), true);

	}

	public void saveConfigHistory(File selectedFile) throws FileNotFoundException, IOException {
		try(FileOutputStream fos = new FileOutputStream (selectedFile);
				ObjectOutputStream oos = new ObjectOutputStream (fos);){
			oos.writeObject(configHistory);
			oos.writeObject(configUndoneHistory);
			oos.writeObject(currentConfig);
			
		}
		
	}
	
	public void loadConfigHistory(File selectedFile) throws FileNotFoundException, IOException, ClassNotFoundException {
		try(FileInputStream fis = new FileInputStream (selectedFile);
				ObjectInputStream ois = new ObjectInputStream (fis);){
			configHistory = (Stack<FractalGUIConfig>) ois.readObject();
			configUndoneHistory = (Stack<FractalGUIConfig>) ois.readObject();
			currentConfig = (FractalGUIConfig) ois.readObject();
			
		}
		applyNewConfig(currentConfig, false);
		
	}

	public void PNGImageFromCurrentConfig(File selectedFile) throws IOException {
		ImageIO.write(view.getBufferedImageOfCanvas(),"png",selectedFile);
		
	}

}
