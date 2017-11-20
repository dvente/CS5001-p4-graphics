package fractalgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class FractalGUIView extends JPanel implements Observer {

    FractalGUIModel model = new FractalGUIModel();
    MandelbrotCalculator mandelCalc = new MandelbrotCalculator();

    private int[][] madelbrotData;
    private int xRes = 800;
    private int yRes = 800;

    public FractalGUIView() {
        madelbrotData = mandelCalc.calcMandelbrotSet(xRes, yRes, MandelbrotCalculator.INITIAL_MIN_REAL,
                MandelbrotCalculator.INITIAL_MAX_REAL, MandelbrotCalculator.INITIAL_MIN_IMAGINARY,
                MandelbrotCalculator.INITIAL_MAX_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_ITERATIONS,
                MandelbrotCalculator.DEFAULT_RADIUS_SQUARED);
        printMandelbrotData();

    }

    public void printMandelbrotData() {

        for (int i = 0; i < madelbrotData.length; i++) {
            for (int j = 0; j < madelbrotData[i].length; j++) {
                System.out.print(String.format("%4d", madelbrotData[i][j]));
            }
            System.out.println();
        }

    }

    @Override
    public void paint(Graphics g) {

        for (int i = 0; i < madelbrotData.length; i++) {
            for (int j = 0; j < madelbrotData[i].length; j++) {
                if (madelbrotData[i][j] >= MandelbrotCalculator.INITIAL_MAX_ITERATIONS) {
                    g.setColor(new Color(0, 0, 0));
                } else {
                    g.setColor(new Color(255, 255, 255));
                }
                g.drawLine(j, i, j, i);
            }
        }

    }

    @Override
    public void update(Observable o, Object arg) {

        // TODO Auto-generated method stub

    }

}
