package com.zizzle.cmpt370;

import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Used for displaying the data values in the Profile page Pie Chart properly.
 * Without this, the data values would only be floats.
 */
public class PieChartFormatter extends ValueFormatter {

    /**
     * Adds apercent symbol at the end
     * @param value float value for the pie chart data
     * @return String with float value (one decimal place) plus percent symbol at the end
     */
    @Override
    public String getFormattedValue(float value) {
        double rounded = Math.round(value * 10) / 10.0; //this converts float to double with one decimal place
        return rounded + "%";
    }
}
