package fractalgraphics;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The Canvas to draw the calculated data on.
 *
 * @author 170008773
 */
public class FractalGUICanvas extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8024973385708779290L;

    /** The fractal data. */
    private int[][] fractalData;

    /** The color mapping. */
    private ColorMapping colorMapping;

    /**
     * Instantiates a new canvas.
     *
     * @param factalData
     *            the factal data to draw on the canvas
     * @param colorMapping
     *            the color mapping
     */
    public FractalGUICanvas(int[][] factalData, ColorMapping colorMapping) {
        super();
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

    /**
     * Sets the data.
     *
     * @param factalData
     *            the factal data
     * @param colorMapping
     *            the color mapping
     */
    public void setData(int[][] factalData, ColorMapping colorMapping) {

        this.fractalData = factalData;
        this.colorMapping = colorMapping;
    }

}
