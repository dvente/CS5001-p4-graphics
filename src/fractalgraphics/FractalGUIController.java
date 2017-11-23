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
import javax.swing.Timer;

public class FractalGUIController {

    private final FractalGUIConfig defaultConfig;

    private final FractalGUIModel model;
    private final FractalGUIView view;
    private FractalGUIConfig currentConfig;
    private Stack<FractalGUIConfig> configHistory;
    private Stack<FractalGUIConfig> configUndoneHistory;
    private List<FractalGUIConfig> animationHistory;
    private final int frameRate = 24;
    private final List<Color[]> colorMappingValues = new ArrayList<Color[]>();
    private int currentColorMappingIndex = 0;
    private Timer animationTimer;

    public static void main(String[] args) {

        new FractalGUIController();
    }

    public FractalGUIController() {
        colorMappingValues.add(new Color[] { Color.WHITE, Color.BLACK });
        colorMappingValues.add(new Color[] { Color.WHITE, new Color(191, 0, 0), Color.BLACK });
        colorMappingValues.add(new Color[] { Color.WHITE, new Color(32, 107, 203), Color.BLACK });
        colorMappingValues.add(new Color[] { Color.WHITE, new Color(0, 131, 31), Color.BLACK });
        colorMappingValues.add(new Color[] {
                Color.WHITE,
                new Color(255, 170, 0),
                new Color(191, 0, 0),
                new Color(0, 131, 31),
                Color.BLACK });
        colorMappingValues.add(new Color[] {
                new Color(0, 7, 100),
                new Color(32, 107, 203),
                new Color(237, 255, 255),
                new Color(255, 170, 0),
                new Color(0, 2, 0) });
        defaultConfig = new FractalGUIConfig(FractalGUIView.DEFAULT_X_RESOLUTION, FractalGUIView.DEFAULT_Y_RESOLUTION,
                MandelbrotCalculator.INITIAL_MIN_REAL, MandelbrotCalculator.INITIAL_MAX_REAL,
                MandelbrotCalculator.INITIAL_MIN_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
                MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED,
                new ColorMapping(MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
                        colorMappingValues.get(currentColorMappingIndex)));

        currentConfig = defaultConfig;
        model = new FractalGUIModel(defaultConfig);
        view = new FractalGUIView(this, model.calcModel());
        model.addObserver(view);

        configHistory = new Stack<FractalGUIConfig>();
        configUndoneHistory = new Stack<FractalGUIConfig>();
        animationHistory = new LinkedList<FractalGUIConfig>();
        animationTimer = new Timer(1000 / (frameRate * 10), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                applyNewConfig(centreScale(currentConfig, 0.9f), false);
                setMaxIterations(currentConfig.getMaxIterations() + 5);
                animationHistory.add(currentConfig);
                view.repaint();
            }

        });

    }

    public void toggleAnimation() {

        if (animationTimer.isRunning()) {
            animationTimer.stop();
        } else {
            animationHistory.clear();
            animationTimer.start();
        }

    }

    public void applyNextColorMapping() {

        currentColorMappingIndex = (currentColorMappingIndex + 1) % colorMappingValues.size();
        ColorMapping newColorMapping = new ColorMapping(currentConfig.getMaxIterations(),
                colorMappingValues.get(currentColorMappingIndex));

        applyNewConfig(new FractalGUIConfig(currentConfig.getxResolution(), currentConfig.getyResolution(),
                currentConfig.getMinReal(), currentConfig.getMaxReal(), currentConfig.getMinImaginary(),
                currentConfig.getMaxImaginary(), currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(),
                newColorMapping), true);

    }

    public boolean hasConfigHistory() {

        return !configHistory.isEmpty();
    }

    public boolean hasConfigUndoneHistory() {

        return configHistory != null && !configUndoneHistory.isEmpty();
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

    public void applyTranslateScreen(int x, int y) {

        double realRange = currentConfig.getMaxReal() - currentConfig.getMinReal();
        double imaginaryRange = currentConfig.getMaxImaginary() - currentConfig.getMinImaginary();

        applyTranslate(0.001 * x * realRange, 0.001 * y * imaginaryRange);

    }

    public FractalGUIConfig translate(double real, double im) {

        return new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(), currentConfig.getMinReal() - real,
                currentConfig.getMaxReal() - real, currentConfig.getMinImaginary() - im,
                currentConfig.getMaxImaginary() - im, currentConfig.getMaxIterations(),
                currentConfig.getRadiusSquared(), currentConfig.getColorMapping());

    }

    public void applyTranslate(double real, double im) {

        applyNewConfig(translate(real, im), true);

    }

    public FractalGUIConfig recentre(int leftX, int upperY, int rightX, int lowerY) {

        return new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(), realFromScreenX(leftX),
                realFromScreenX(rightX), imaginaryFromScreenY(lowerY), imaginaryFromScreenY(upperY),
                currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getColorMapping());

    }

    public void applyRecentre(int leftX, int upperY, int rightX, int lowerY) {

        assert leftX < rightX;
        assert upperY > lowerY;
        assert screenXFromReal(realFromScreenX(leftX)) == leftX;
        assert screenYFromImaginary(imaginaryFromScreenY(upperY)) == upperY;
        assert screenXFromReal(realFromScreenX(rightX)) == rightX;
        assert screenYFromImaginary(imaginaryFromScreenY(lowerY)) == lowerY;
        applyNewConfig(recentre(leftX, upperY, rightX, lowerY), true);

    }

    public FractalGUIConfig centreScale(FractalGUIConfig config, double d) {

        assert d > 0;
        double width = config.getMaxReal() - config.getMinReal();
        double height = config.getMaxImaginary() - config.getMinImaginary();

        return new FractalGUIConfig(view.getCurrentXSize(), view.getCurrentYSize(),
                (config.getCentreReal() - (width * 0.5 * d)), (config.getCentreReal() + (width * 0.5 * d)),
                (config.getCentreImaginary() - (height * 0.5 * d)), (config.getCentreImaginary() + (height * 0.5 * d)),
                config.getMaxIterations(), config.getRadiusSquared(), config.getColorMapping());

    }

    public void applyCentreScale(double d) {

        applyNewConfig(centreScale(currentConfig, d), true);

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
