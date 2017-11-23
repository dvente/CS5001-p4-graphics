package fractalgraphics;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;

public class ColorMapping implements Serializable {

    private final int minValue = 0;
    private int maxValue;
    private int lengthOfSegments;
    private int[] valueSegmentBorders;
    private final Color[] colorValues;

    public ColorMapping(int maxValue, Color[] colorValues) {
        this.colorValues = colorValues;
        setMaxValue(maxValue);

    }

    private void setMaxValue(int newMaxValue) {

        maxValue = newMaxValue;
        reCalcRanges(newMaxValue);
    }

    private void reCalcRanges(int newMaxValue) {

        lengthOfSegments = maxValue / (colorValues.length - 1);
        valueSegmentBorders = new int[colorValues.length + 1];
        for (int i = 0; i < valueSegmentBorders.length; i++) {
            valueSegmentBorders[i] = i * lengthOfSegments;
        }

    }

    public ColorMapping() {
        colorValues = new Color[] { Color.WHITE, Color.BLACK };
        setMaxValue(MandelbrotCalculator.INITIAL_MAX_ITERATIONS);
    }

    public Color[] getColorValues() {

        return colorValues;
    }

    @Override
    public String toString() {

        return "ColorMapping [minValue=" + minValue + ", maxValue=" + maxValue + ", colorValues="
                + Arrays.toString(colorValues) + "]";
    }

    public Color interpolateColors(Color first, Color second, float p) {

        final int red = (int) (first.getRed() * p + second.getRed() * (1 - p));
        final int green = (int) (first.getGreen() * p + second.getGreen() * (1 - p));
        final int blue = (int) (first.getBlue() * p + second.getBlue() * (1 - p));
        return new Color(red, green, blue);
    }

    // taken and addapted from https://en.wikipedia.org/wiki/Smoothstep
    public float Smoothstep(int leftEdge, int rightEdge, float input) {

        final float x = clamp((input - leftEdge) / (rightEdge - leftEdge), 0.0f, 1.0f);
        return x * x * x * (x * (x * 6 - 15) + 10);
    }

    public float clamp(float input, float lowerlimit, float upperlimit) {

        if (input < lowerlimit) {
            return lowerlimit;
        }
        if (input > upperlimit) {
            return upperlimit;
        }
        return input;
    }

    public float sigmoid(int x) {

        // return (float) (1 / (1 + Math.exp(-1 * x)));
        return x / (1 + Math.abs((float) x));
    }

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

    public int getSortedIndex(int[] array, int value) {

        int i = 0;
        while (value > array[i]) {
            i++;
        }
        return i - 1;
    }

}
