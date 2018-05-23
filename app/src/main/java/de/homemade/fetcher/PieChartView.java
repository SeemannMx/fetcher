package de.homemade.fetcher;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.HashMap;

public class PieChartView extends GraphicalView {

    String TAG = "FETCHER ";
    String CLASS = "PIE CHART ";

    public static final int COLOR_GREEN = Color.parseColor("#62c51a");
    public static final int COLOR_ORANGE = Color.parseColor("#ff6c0a");
    public static final int COLOR_BLUE = Color.parseColor("#23bae9");
    public static final int COLOR_PETROL = Color.parseColor("#61e2d1");
    public static final int COLOR_VIOLETT = Color.parseColor("#ac82bc");
    public static final int COLOR_PLUM = Color.parseColor("#9999cc");
    public static final int COLOR_ULTRA_VIOLETT = Color.parseColor("#b26490");
    public static final int COLOR_DEEP_PETROL = Color.parseColor("#577681");

    DatabaseHelper databaseHelper;



    /**
         * Constructor that only calls the super method. It is not used to instantiate the object from outside of this
         * class.
         *
         * @param context
         * @param arg1
         */
	private PieChartView(Context context, AbstractChart arg1, DatabaseHelper dbHelper) {
            super(context, arg1);
        }

    /**
     * This method returns a new graphical view as a pie chart view. It uses the income and costs and the static color
     * constants of the class to create the chart.
     *
     * @param context
     *            the context
     * @return a GraphicalView object as a pie chart
     */
    public static GraphicalView getNewInstance(Context context, HashMap<String, Double> portfolio) {
        return ChartFactory.getPieChartView(context, getDataSet(context,portfolio), getRenderer());
    }

    /**
     * Creates the renderer for the pie chart and sets the basic color scheme and hides labels and legend.
     *
     * @return a renderer for the pie chart
     */
    private static DefaultRenderer getRenderer() {

        // count of color has to be same as count of input values for the diagramms
        int[] colors = new int[] {
                                    COLOR_BLUE,
                                    COLOR_ULTRA_VIOLETT,
                                    COLOR_DEEP_PETROL,
                                    COLOR_VIOLETT,
                                    COLOR_PETROL,
                                    COLOR_GREEN
        };

        // int[] colors = new int[] { COLOR_GREEN, COLOR_ORANGE, COLOR_BLUE};

        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer simpleRenderer = new SimpleSeriesRenderer();
            simpleRenderer.setColor(color);
            defaultRenderer.addSeriesRenderer(simpleRenderer);
        }
        defaultRenderer.setShowLabels(false);
        defaultRenderer.setShowLegend(false);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setZoomEnabled(false);

        return defaultRenderer;
    }

    /**
     * Creates the data set used by the piechart by adding the string represantation and it's integer value to a
     * CategorySeries object. Note that the string representations are hard coded.
     *
     * @param context the context
     * @return a CategorySeries instance with the data supplied
     */
    private static CategorySeries getDataSet(Context context, HashMap<String, Double> sumOfItems) {

        CategorySeries series = new CategorySeries("Chart");
        series.add(context.getString(R.string.Gold),sumOfItems.get("sumGold"));
        series.add(context.getString(R.string.Silber), sumOfItems.get("sumSilber"));
        series.add(context.getString(R.string.Palladium), sumOfItems.get("sumPalladium"));
        series.add(context.getString(R.string.Platin), sumOfItems.get("sumPlatin"));
        series.add(context.getString(R.string.Rhodium), sumOfItems.get("sumRhodium"));
        series.add(context.getString(R.string.Coins), sumOfItems.get("sumCoins"));

        return series;
    }

}
