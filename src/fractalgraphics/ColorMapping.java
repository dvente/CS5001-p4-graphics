package fractalgraphics;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;

/**
 * The Class responsible for the ColorMapping.
 *
 * @author 170008773
 */
public class ColorMapping implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8706102165613147679L;

    /** The min value. */
    private final int minValue = 0;

    /** The maximum number of iterations. */
    private int maxValue;

    /**
     * The length of segments, used to deterime between which color we sould.
     * interpolate
     */
    private int lengthOfSegments;

    /** The value segment borders. */
    private int[] valueSegmentBorders;

    /** The colors to display. */
    private final Color[] colorValues;

    /**
     * Instantiates a new standard black and white color mapping.
     */
    public ColorMapping() {
        colorValues = new Color[] {Color.WHITE, Color.BLACK };
        setMaxValue(MandelbrotCalculator.INITIAL_MAX_ITERATIONS);
    }

    /**
     * Instantiates a new color mapping.
     *
     * @param maxValue
     *            the max number of iterations
     * @param colorValues
     *            the colors to display
     */
    public ColorMapping(int maxValue, Color[] colorValues) {
        this.colorValues = colorValues;
        setMaxValue(maxValue);

    }

    /**
     * Gets the color associated with the number of iterations.
     *
     * @param value
     *            the number of iterations
     * @return the color cooresopnding to the value
     */
    public Color getColorFromValue(int value) {

        if (colorValues.length == 2) {
            if (value >= maxValue) {
                return colorValues[1];
            } else {
                return colorValues[0];
            }
        } else {
            if (value == maxValue) {
                return colorValues[colorValues.length - 1];
            } else {
                final int index = getSortedIndex(valueSegmentBorders, value);

                final float percentage = ((float) value) / (((index + 1) * (lengthOfSegments)));
                if (index >= colorValues.length - 1) {
                    return interpolateColors(colorValues[index - 1], colorValues[index], percentage);
                } else {
                    return interpolateColors(colorValues[index], colorValues[index + 1], percentage);
                }

            }
        }
    }

    /**
     * Gets the colors to display.
     *
     * @return the colors to display
     */
    public Color[] getColorValues() {

        return colorValues;
    }

    /**
     * returns the index of the place where a values houls be inserted into a
     * sorted array.
     *
     * @param array
     *            the array to insert into
     * @param value
     *            the value you want to insert
     * @return the index of the place to keep the list sorted
     */
    public int getSortedIndex(int[] array, int value) {

        int i = 0;
        while (value > array[i]) {
            i++;
        }
        return i - 1;
    }

    /**
     * Interpolate colors.
     *
     * @param first
     *            the first color
     * @param second
     *            the second color
     * @param p
     *            the percentage of interpolation
     * @return the color that is p way between the first and second color
     */
    public Color interpolateColors(Color first, Color second, float p) {

        final int red = (int) (first.getRed() * p + second.getRed() * (1 - p));
        final int green = (int) (first.getGreen() * p + second.getGreen() * (1 - p));
        final int blue = (int) (first.getBlue() * p + second.getBlue() * (1 - p));
        return new Color(red, green, blue);
    }

    /**
     * Recalcluate ranges for color interpolation.
     *
     * @param newMaxValue
     *            the new max value
     */
    private void reCalcRanges(int newMaxValue) {

        lengthOfSegments = maxValue / (colorValues.length - 1);
        valueSegmentBorders = new int[colorValues.length + 1];
        for (int i = 0; i < valueSegmentBorders.length; i++) {
            valueSegmentBorders[i] = i * lengthOfSegments;
        }

    }

    /**
     * Sets the maximum number of iterations.
     *
     * @param newMaxValue
     *            the new maximum number of iterations
     */
    private void setMaxValue(int newMaxValue) {

        maxValue = newMaxValue;
        reCalcRanges(newMaxValue);
    }

    @Override
    public String toString() {

        return "ColorMapping [minValue=" + minValue + ", maxValue=" + maxValue + ", colorValues="
                + Arrays.toString(colorValues) + "]";
    }

}
