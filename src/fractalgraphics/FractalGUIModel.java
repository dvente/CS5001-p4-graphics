package fractalgraphics;

import java.util.Observable;

public class FractalGUIModel extends Observable {

    MandelbrotCalculator mandelCalc = new MandelbrotCalculator();
    // Initial parameter values
    private final double INITIAL_MIN_REAL = -2.0;
    private final double INITIAL_MAX_REAL = 0.7;
    private final double INITIAL_MIN_IMAGINARY = -1.25;
    private final double INITIAL_MAX_IMAGINARY = 1.25;
    private final int INITIAL_MAX_ITERATIONS = 50;

    // Default parameter values
    private final double DEFAULT_RADIUS_SQUARED = 4.0;

}
