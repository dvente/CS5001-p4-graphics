package fractalgraphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FractalGUICanvas extends JPanel {

    public FractalGUICanvas(int[][] factalData) {
        super();
        this.fractalData = factalData;
    }
    
    private int[][] fractalData;

    public void setFactalData(int[][] factalData) {

        this.fractalData = factalData;
    }

    @Override
    public void paint(Graphics g) {
    	
        for (int i = 0; i < fractalData.length; i++) {
            for (int j = 0; j < fractalData[i].length; j++) {
                if (fractalData[i][j] >= MandelbrotCalculator.INITIAL_MAX_ITERATIONS) {
                    g.setColor(new Color(0, 0, 0));
                } else {
                    g.setColor(new Color(255, 255, 255));
                }
                g.drawLine(j, i, j, i);
            }
        }

    }

    public void printMandelbrotData() {

        for (int i = 0; i < fractalData.length; i++) {
            for (int j = 0; j < fractalData[i].length; j++) {
                System.out.print(String.format("%4d", fractalData[i][j]));
            }
            System.out.println();
        }

    }

}
