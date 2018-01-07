/*
 *
 */
package fractalgraphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * The Class FractalGUIController.
 *
 * @author 170008773
 */
public class FractalGUIController {

    /** The Constant HALF_FACTOR. */
    private static final double HALF_FACTOR = 0.5;

    /** The Constant ALMOST_BLACK. */
    private static final Color ALMOST_BLACK = new Color(0, 2, 0);

    /** The Constant GOLD. */
    private static final Color GOLD = new Color(237, 255, 255);

    /** The Constant DARK_BLUE. */
    private static final Color DARK_BLUE = new Color(0, 7, 100);

    /** The Constant DARK_YELLOW. */
    private static final Color DARK_YELLOW = new Color(255, 170, 0);

    /** The Constant DARK_GREEN. */
    private static final Color DARK_GREEN = new Color(0, 131, 31);

    /** The Constant LIGHT_BLUE. */
    private static final Color LIGHT_BLUE = new Color(32, 107, 203);

    /** The Constant MILLISECONDS_IN_SECONDS. */
    private static final int MILLISECONDS_IN_SECONDS = 1000;

    /** The Constant ANIMATION_ZOOM_FACTOR. */
    private static final float ANIMATION_ZOOM_FACTOR = 0.9f;

    /** The Constant DARK_RED. */
    private static final Color DARK_RED = new Color(191, 0, 0);

    /**
     * The main method.
     *
     * @param args
     *            the arguments from the command lines
     */
    public static void main(String[] args) {
    	
        new FractalGUIController(Integer.parseInt(args[0]));
    }

    /** The default config. */
    private final FractalGUIConfig defaultConfig;

    /** The model. */
    private final FractalGUIModel model;

    /** The view. */
    private final FractalGUIView view;

    /** The current config. */
    private FractalGUIConfig currentConfig;

    /** The config history. */
    private Stack<FractalGUIConfig> configHistory;

    /** The config undone history. */
    private Stack<FractalGUIConfig> configUndoneHistory;

    /** The animation history. */
    private List<FractalGUIConfig> animationHistory;

    /** The frame rate. */
    private final int frameRate = 240;

    /** The color mapping values. */
    private final List<Color[]> colorMappingValues = new ArrayList<Color[]>();

    /** The current color mapping index. */
    private int currentColorMappingIndex = 0;

    /** The animation timer. */
    private Timer animationTimer;

    /**
     * Instantiates a new fractal GUI controller.
     */
    public FractalGUIController(int numbOfThreads) {
        colorMappingValues.add(new Color[] {Color.WHITE, Color.BLACK });
        colorMappingValues.add(new Color[] {Color.WHITE, DARK_RED, Color.BLACK });
        colorMappingValues.add(new Color[] {Color.WHITE, LIGHT_BLUE, Color.BLACK });
        colorMappingValues.add(new Color[] {Color.WHITE, DARK_GREEN, Color.BLACK });
        colorMappingValues.add(new Color[] {Color.WHITE, DARK_YELLOW, DARK_RED, DARK_GREEN, Color.BLACK });
        colorMappingValues.add(new Color[] {DARK_BLUE, LIGHT_BLUE, GOLD, DARK_YELLOW, ALMOST_BLACK });
        defaultConfig = new FractalGUIConfig(FractalGUIView.DEFAULT_X_RESOLUTION, FractalGUIView.DEFAULT_Y_RESOLUTION,
                MandelbrotCalculator.INITIAL_MIN_REAL, MandelbrotCalculator.INITIAL_MAX_REAL,
                MandelbrotCalculator.INITIAL_MIN_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
                MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED,
                new ColorMapping(MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
                        colorMappingValues.get(currentColorMappingIndex)));

        currentConfig = defaultConfig;
        model = new FractalGUIModel(defaultConfig, numbOfThreads);
        view = new FractalGUIView(this, model.calcModel());
        model.addObserver(view);

        configHistory = new Stack<FractalGUIConfig>();
        configUndoneHistory = new Stack<FractalGUIConfig>();
        animationHistory = new LinkedList<FractalGUIConfig>();
        animationTimer = new Timer(MILLISECONDS_IN_SECONDS / (frameRate), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                applyNewConfig(centreScale(currentConfig, ANIMATION_ZOOM_FACTOR), false);
                animationHistory.add(currentConfig);
                view.repaint();
            }

        });

    }

    /**
     * Apply centre scale.
     *
     * @param d
     *            the d
     */
    public void applyCentreScale(double d) {

        applyNewConfig(centreScale(currentConfig, d), true);

    }

    /**
     * Apply new config.
     *
     * @param newConfig
     *            the new config
     * @param record
     *            the record
     */
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

    /**
     * Apply next color mapping.
     */
    public void applyNextColorMapping() {

        currentColorMappingIndex = (currentColorMappingIndex + 1) % colorMappingValues.size();
        ColorMapping newColorMapping = new ColorMapping(currentConfig.getMaxIterations(),
                colorMappingValues.get(currentColorMappingIndex));

        applyNewConfig(new FractalGUIConfig(currentConfig.getxResolution(), currentConfig.getyResolution(),
                currentConfig.getMinReal(), currentConfig.getMaxReal(), currentConfig.getMinImaginary(),
                currentConfig.getMaxImaginary(), currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(),
                newColorMapping), true);

    }

    /**
     * Apply translate.
     *
     * @param real
     *            the real
     * @param im
     *            the im
     */
    public void applyTranslate(double real, double im) {

        applyNewConfig(translate(real, im), true);

    }

    /**
     * Apply translate screen.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void applyTranslateScreen(int x, int y) {

        double realRange = currentConfig.getMaxReal() - currentConfig.getMinReal();
        double imaginaryRange = currentConfig.getMaxImaginary() - currentConfig.getMinImaginary();
        applyTranslate(x * realRange / currentConfig.getxResolution(),
                y * imaginaryRange / currentConfig.getyResolution());

    }

    /**
     * Centre scale.
     *
     * @param config
     *            the config
     * @param d
     *            the d
     * @return the fractal GUI config
     */
    public FractalGUIConfig centreScale(FractalGUIConfig config, double d) {

        assert d > 0;
        double width = config.getMaxReal() - config.getMinReal();
        double height = config.getMaxImaginary() - config.getMinImaginary();

        return new FractalGUIConfig(view.getXSize(), view.getYSize(),
                (config.getCentreReal() - (width * HALF_FACTOR * d)),
                (config.getCentreReal() + (width * HALF_FACTOR * d)),
                (config.getCentreImaginary() - (height * HALF_FACTOR * d)),
                (config.getCentreImaginary() + (height * HALF_FACTOR * d)), config.getMaxIterations(),
                config.getRadiusSquared(), config.getColorMapping());

    }

    /**
     * Export to PNG.
     *
     * @param file
     *            the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void exportToPNG(File file) throws IOException {

        ImageIO.write(view.getBufferedImageFromCanvas(), "png", file);

    }

    /**
     * Gets the color mapping.
     *
     * @return the color mapping
     */
    public ColorMapping getColorMapping() {

        return currentConfig.getColorMapping();
    }

    /**
     * Gets the current config.
     *
     * @return the current config
     */
    public FractalGUIConfig getCurrentConfig() {

        return currentConfig;
    }

    /**
     * Gets the max iterations.
     *
     * @return the max iterations
     */
    public int getMaxIterations() {

        return currentConfig.getMaxIterations();
    }

    /**
     * Checks for config history.
     *
     * @return true, if successful
     */
    public boolean hasConfigHistory() {

        return !configHistory.isEmpty();
    }

    /**
     * Checks for config undone history.
     *
     * @return true, if successful
     */
    public boolean hasConfigUndoneHistory() {

        return configHistory != null && !configUndoneHistory.isEmpty();
    }

    /**
     * Converst a screen y coordinate to the corresponding imaginary coordinate.
     *
     * @param y
     *            the y
     * @return the corresponding y coordinate
     */
    public double imaginaryFromScreenY(int y) {

        return currentConfig.getMinImaginary()
                + ((y * (currentConfig.getMaxImaginary() - currentConfig.getMinImaginary()))
                        / currentConfig.getxResolution());
    }

    /**
     * Load.
     *
     * @param file
     *            the file
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    public void load(File file) throws FileNotFoundException, IOException, ClassNotFoundException {

        try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis);) {
            currentConfig = (FractalGUIConfig) ois.readObject();
            configHistory = (Stack<FractalGUIConfig>) ois.readObject();
            configUndoneHistory = (Stack<FractalGUIConfig>) ois.readObject();
            applyNewConfig(currentConfig, false);
        }

    }

    /**
     * Redo reaply the most recently undone configuration.
     */
    public void redo() {

        configHistory.push(currentConfig);
        applyNewConfig(configUndoneHistory.pop(), false);

    }

    /**
     * Reset to the default configuration.
     */
    public void reset() {

        applyNewConfig(defaultConfig, true);

    }

    /**
     * Save the current configuration to a file.
     *
     * @param file
     *            the file
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void save(File file) throws FileNotFoundException, IOException {

        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(currentConfig);
            oos.writeObject(configHistory);
            oos.writeObject(configUndoneHistory);

        }

    }

    /**
     * Sets the max iterations.
     *
     * @param newMaxIterations
     *            the new max iterations
     */
    public void setMaxIterations(int newMaxIterations) {

        ColorMapping newColorMapping = new ColorMapping(newMaxIterations,
                currentConfig.getColorMapping().getColorValues());

        applyNewConfig(new FractalGUIConfig(currentConfig.getxResolution(), currentConfig.getyResolution(),
                currentConfig.getMinReal(), currentConfig.getMaxReal(), currentConfig.getMinImaginary(),
                currentConfig.getMaxImaginary(), newMaxIterations, currentConfig.getRadiusSquared(), newColorMapping),
                true);

    }

    /**
     * Toggle zoom animation.
     */
    public void toggleAnimation() {

        if (animationTimer.isRunning()) {
            animationTimer.stop();
            applyNewConfig(currentConfig, true);
        } else {
            animationHistory.clear();
            animationTimer.start();
        }

    }

    /**
     * Translate.
     *
     * @param real
     *            the real
     * @param im
     *            the im
     * @return the fractal GUI config
     */
    public FractalGUIConfig translate(double real, double im) {

        return new FractalGUIConfig(view.getXSize(), view.getYSize(), currentConfig.getMinReal() - real,
                currentConfig.getMaxReal() - real, currentConfig.getMinImaginary() - im,
                currentConfig.getMaxImaginary() - im, currentConfig.getMaxIterations(),
                currentConfig.getRadiusSquared(), currentConfig.getColorMapping());

    }

    /**
     * revert to the previous configuration.
     */
    public void undo() {

        configUndoneHistory.push(currentConfig);
        applyNewConfig(configHistory.pop(), false);

    }

	public void close() {
	int result = JOptionPane.showConfirmDialog(
            view,
            "Are you sure you want to exit the application?",
            "Exit Application",
            JOptionPane.YES_NO_OPTION);
 
        if (result == JOptionPane.YES_OPTION)
            view.dispose();
	
	
}
    
    // The following commented functions are now depricated, but are left in for reference
    //    public double realFromScreenX(int x) {
    //
    //        return currentConfig.getMinReal()
    //                + (((x * (currentConfig.getMaxReal() - currentConfig.getMinReal())) / currentConfig.getxResolution()));
    //    }
    //    public FractalGUIConfig recentre(int leftX, int upperY, int rightX, int lowerY) {
    //
    //        return new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(), realFromScreenX(leftX),
    //                realFromScreenX(rightX), imaginaryFromScreenY(lowerY), imaginaryFromScreenY(upperY),
    //                currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getColorMapping());
    //
    //    }

    //    /**
    //     * Apply recentre.
    //     *
    //     * @param leftX
    //     *            the left X
    //     * @param upperY
    //     *            the upper Y
    //     * @param rightX
    //     *            the right X
    //     * @param lowerY
    //     *            the lower Y
    //     */
    //    public void applyRecentre(int leftX, int upperY, int rightX, int lowerY) {
    //
    //        //        assert leftX < rightX;
    //        //        assert upperY > lowerY;
    //        //        assert screenXFromReal(realFromScreenX(leftX)) == leftX;
    //        //        assert screenYFromImaginary(imaginaryFromScreenY(upperY)) == upperY;
    //        //        assert screenXFromReal(realFromScreenX(rightX)) == rightX;
    //        //        assert screenYFromImaginary(imaginaryFromScreenY(lowerY)) == lowerY;
    //        applyNewConfig(recentre(leftX, upperY, rightX, lowerY), true);
    //
    //    }

}
