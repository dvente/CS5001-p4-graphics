package fractalgraphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FractalGUICanvas extends JPanel {

    public FractalGUICanvas(int[][] factalData) {
        super();
        this.factalData = factalData;
        //        printMandelbrotData();
    }

    private int[][] factalData;

    public void setFactalData(int[][] factalData) {

        this.factalData = factalData;
        //        printMandelbrotData();
    }

    @Override
    public void paint(Graphics g) {

        for (int i = 0; i < factalData.length; i++) {
            for (int j = 0; j < factalData[i].length; j++) {
                if (factalData[i][j] >= MandelbrotCalculator.INITIAL_MAX_ITERATIONS) {
                    g.setColor(new Color(0, 0, 0));
                } else {
                    g.setColor(new Color(255, 255, 255));
                }
                g.drawLine(j, i, j, i);
            }
        }

    }

    public void printMandelbrotData() {

        for (int i = 0; i < factalData.length; i++) {
            for (int j = 0; j < factalData[i].length; j++) {
                System.out.print(String.format("%4d", factalData[i][j]));
            }
            System.out.println();
        }

    }

}
