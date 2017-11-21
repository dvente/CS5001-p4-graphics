package fractalgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class FractalGUICanvas extends JPanel {

    private int[][] factalData;

    public void setFactalData(int[][] factalData) {
		this.factalData = factalData;
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

}
