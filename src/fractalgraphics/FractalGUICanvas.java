package fractalgraphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FractalGUICanvas extends JPanel {

    public FractalGUICanvas(int[][] factalData, int maxIterations) {
        super();
        this.fractalData = factalData;
        this.maxIterations = maxIterations;
    }
    
    private int[][] fractalData;
    private int maxIterations;

    public void setData(int[][] factalData, int maxIterations) {

        this.fractalData = factalData;
        this.maxIterations = maxIterations;
    }

    @Override
    public void paint(Graphics g) {
    	
        for (int i = 0; i < fractalData.length; i++) {
            for (int j = 0; j < fractalData[i].length; j++) {
                if (fractalData[i][j] >= maxIterations) {
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
