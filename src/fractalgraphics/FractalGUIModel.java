package fractalgraphics;

import java.util.Observable;

/**
 * The Class FractalGUIModel. Acts as a adaptor to the provided model
 *
 * @author 170008773
 */
public class FractalGUIModel extends Observable {

    /** The mandel calc. */
    private final MandelbrotCalculator mandelCalc = new MandelbrotCalculator();

    /** The current config. */
    private FractalGUIConfig currentConfig = null;

    /**
     * Instantiates a new fractal GUI model.
     *
     * @param currentConfig
     *            the current config
     */
    public FractalGUIModel(FractalGUIConfig currentConfig) {
        super();
        this.currentConfig = currentConfig;
    }

    /**
     * Recalculate the with current configuration model.
     *
     * @return the int[][]
     */
    public int[][] calcModel() {

        return calcModel(currentConfig);
    }

    /**
     * Calculate model based on a given configuration.
     *
     * @param config
     *            the config to base the calculations on
     * @return the array containing the results of the calculations
     */
    public int[][] calcModel(FractalGUIConfig config) {

        System.out.println(config.toString());

        return mandelCalc.calcMandelbrotSet(config.getxResolution(), config.getyResolution(), config.getMinReal(),
                config.getMaxReal(), config.getMinImaginary(), config.getMaxImaginary(), config.getMaxIterations(),
                config.getRadiusSquared());
    }

    /**
     * Sets the current config.
     *
     * @param config
     *            the new current config
     */
    public void setCurrentConfig(FractalGUIConfig config) {

        this.currentConfig = config;
        setChanged();
        notifyObservers(calcModel(config));

    }

}
