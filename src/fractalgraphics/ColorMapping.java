package fractalgraphics;

import java.awt.Color;
import java.util.Arrays;

public class ColorMapping {

    private int minValue = 0;
    private int maxValue;
    private int lengthOfSegments;
    private int[] valueSegmentBorders;

    Color[] colorValues;

    public ColorMapping(int maxValue, Color[] colorValues) {
        this.maxValue = maxValue;
        this.colorValues = colorValues;
        lengthOfSegments = maxValue / (colorValues.length - 1);
        valueSegmentBorders = new int[colorValues.length + 1];
        for (int i = 0; i < valueSegmentBorders.length; i++) {
            valueSegmentBorders[i] = i * lengthOfSegments;
        }
    }

    public ColorMapping() {
        this.maxValue = MandelbrotCalculator.INITIAL_MAX_ITERATIONS;
        colorValues = new Color[] { Color.WHITE, Color.BLACK };
        lengthOfSegments = maxValue / (colorValues.length - 1);
    }

    @Override
    public String toString() {

        return "ColorMapping [minValue=" + minValue + ", maxValue=" + maxValue + ", colorValues="
                + Arrays.toString(colorValues) + "]";
    }

    public int getMaxValue() {

        return maxValue;
    }

    public void setMaxValue(int maxValue) {

        this.maxValue = maxValue;
    }

    public Color interpolateColors(Color first, Color second, float p) {

        int red = (int) (first.getRed() * p + second.getRed() * (1 - p));
        int green = (int) (first.getGreen() * p + second.getGreen() * (1 - p));
        int blue = (int) (first.getBlue() * p + second.getBlue() * (1 - p));
        return new Color(red, green, blue);
    }

    //taken and addapted from https://en.wikipedia.org/wiki/Smoothstep
    public float Smoothstep(int leftEdge, int rightEdge, float input) {

        float x = clamp((input - leftEdge) / (rightEdge - leftEdge), 0.0f, 1.0f);
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

        //        return (float) (1 / (1 + Math.exp(-1 * x)));
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
                int index = getSortedIndex(valueSegmentBorders, value);
                //                System.out.println(Arrays.toString(valueSegmentBorders));
                //                System.out.println(value);
                //                System.out.println(index);
                //                System.out.println();

                float percentage = ((float) value) / (index * (lengthOfSegments + 1));
                //                return interpolateColors(colorValues[index], colorValues[index + 1], Smoothstep(0, 1, percentage));
                if (index >= colorValues.length - 1) {
                    return interpolateColors(colorValues[index - 1], colorValues[index], sigmoid(value));
                } else {
                    return interpolateColors(colorValues[index], colorValues[index + 1], sigmoid(value));
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
