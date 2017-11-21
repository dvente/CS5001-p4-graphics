package fractalgraphics;

import java.util.Observable;

public class FractalGUIModel extends Observable {

    public FractalGUIModel(FractalGUIConfig currentConfig) {
        super();
        this.currentConfig = currentConfig;
    }

    MandelbrotCalculator mandelCalc = new MandelbrotCalculator();

    private FractalGUIConfig currentConfig;

    public void setCurrentConfig(FractalGUIConfig config) {

        this.currentConfig = config;
        setChanged();
        notifyObservers();
    }

    public int[][] calcModel() {

        return calcModel(currentConfig);
    }

    public int[][] calcModel(FractalGUIConfig config) {

        System.out.println(config.toString());
        return mandelCalc.calcMandelbrotSet(config.getxResolution(), config.getyResolution(), config.getMinReal(),
                config.getMaxReal(), config.getMinImaginary(), config.getMaxImaginary(), config.getMaxIterations(),
                config.getRadiusSquared());
    }

}
