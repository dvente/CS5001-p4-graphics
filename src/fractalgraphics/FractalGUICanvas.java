package fractalgraphics;

import java.awt.Graphics;

import javax.swing.JPanel;

public class FractalGUICanvas extends JPanel {

    public FractalGUICanvas(int[][] factalData, ColorMapping colorMapping) {
        super();
        this.fractalData = factalData;
        this.colorMapping = colorMapping;
    }

    private int[][] fractalData;
    private ColorMapping colorMapping;

    public void setData(int[][] factalData, ColorMapping colorMapping) {

        this.fractalData = factalData;
        this.colorMapping = colorMapping;
    }

    @Override
    public void paint(Graphics g) {

        for (int i = 0; i < fractalData.length; i++) {
            for (int j = 0; j < fractalData[i].length; j++) {
                g.setColor(colorMapping.getColorFromValue(fractalData[i][j]));
                g.drawLine(j, i, j, i);
            }
        }

    }

}
