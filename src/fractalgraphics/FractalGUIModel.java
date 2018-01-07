package fractalgraphics;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Class FractalGUIModel. Acts as a adaptor to the provided model
 *
 * @author 170008773
 */
public class FractalGUIModel extends Observable {

    /** The mandel calc. */
    private final MandelbrotCalculator mandelCalc;

    /** The current config. */
    private FractalGUIConfig currentConfig = null;

	
    /**
     * Instantiates a new fractal GUI model.
     *
     * @param currentConfig
     *            the current config
     */
    public FractalGUIModel(FractalGUIConfig currentConfig, int numbOfThreads) {
        super();
        this.currentConfig = currentConfig;
        mandelCalc = new MandelbrotCalculator(numbOfThreads);
    }

    /**
     * Recalculate the with current configuration model.
     *
     * @return the int[][]
     */
    public int[][] calcModel() {

        return calcModelThreaded(currentConfig);
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
    
    public int[][] calcModelThreaded(FractalGUIConfig config){
    
    	System.out.println(config.toString());
    	int[][] parallelData = mandelCalc.calcMandelbrotSetThreaded(config.getxResolution(), config.getyResolution(), config.getMinReal(),
                config.getMaxReal(), config.getMinImaginary(), config.getMaxImaginary(), config.getMaxIterations(),
                config.getRadiusSquared());
    	int[][] seqData = mandelCalc.calcMandelbrotSet(config.getxResolution(), config.getyResolution(), config.getMinReal(),
                config.getMaxReal(), config.getMinImaginary(), config.getMaxImaginary(), config.getMaxIterations(),
                config.getRadiusSquared());
    	for (int i = 0; i < seqData.length; i++) {
			for (int j = 0; j < seqData[0].length; j++) {
				assert seqData[i][j] == parallelData[i][j] : String.format("(i%d,r%d) : s = %d, p = %d", i,j,seqData[i][j],parallelData[i][j]) ;
			}
		}
    	
    	
        return mandelCalc.calcMandelbrotSetThreaded(config.getxResolution(), config.getyResolution(), config.getMinReal(),
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
        notifyObservers(calcModelThreaded(config));

    }

}
