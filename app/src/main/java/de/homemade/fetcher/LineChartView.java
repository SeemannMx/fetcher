package de.homemade.fetcher;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class LineChartView {

    public static final int COLOR_GREEN = Color.parseColor("#62c51a");
    public static final int COLOR_ORANGE = Color.parseColor("#ff6c0a");
    public static final int COLOR_BLUE = Color.parseColor("#23bae9");
    public static final int COLOR_PETROL = Color.parseColor("#61e2d1");
    public static final int COLOR_VIOLETT = Color.parseColor("#ac82bc");
    public static final int COLOR_PLUM = Color.parseColor("#9999cc");
    public static final int COLOR_ULTRA_VIOLETT = Color.parseColor("#b26490");
    public static final int COLOR_DEEP_PETROL = Color.parseColor("#577681");

    public GraphicalView lineDiagramm(Context context){

        ArrayList<Double> myList = new ArrayList<>();

        myList.add(0, -1.0);
        myList.add(1,4.0);
        myList.add(2,6.0);
        myList.add(3,-8.0);
        myList.add(4,10.0);
        myList.add(5, 12.0);

        // populate the series
        XYSeries series = new XYSeries("diagramm of things");

        for(int i = 0;i < myList.size(); i++){
            Double value = myList.get(i);
            series.add(i,value);
        }

        // create renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(COLOR_PLUM);

        // create low max value
        renderer.setDisplayBoundingPoints(true);

        // add oint markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);

        // create renderer that controls each single renderer for each series
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        // layout diagramm
        // transparent margins
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));

        // disable pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        // show grid
        mRenderer.setShowGrid(true); // we show the grid

        return ChartFactory.getLineChartView(context,dataset,mRenderer);

    }

}
