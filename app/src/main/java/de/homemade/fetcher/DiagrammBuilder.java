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
import java.util.List;

public class DiagrammBuilder {

    String TAG = "FETCHER ";
    String CLASS = "DIAGRAMM CLASS";

    Context context;

    public DiagrammBuilder(Context context) {
        this.context = context;
    }


    public GraphicalView testDiagramm(Context context, GraphicalView chartView){

        List <Double> myList = new ArrayList<>();

        myList.add(1, -1.0);
        myList.add(2,4.0);
        myList.add(3,6.0);
        myList.add(4,-8.0);
        myList.add(5,10.0);
        myList.add(6, 12.0);

        // populate the series
        XYSeries series = new XYSeries("diagramm of things");

        for(int i = 0;i < myList.size(); i++){
            Double value = myList.get(i);
            series.add(i,value);
        }

        // create renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);

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

        return chartView = ChartFactory.getLineChartView(context,dataset,mRenderer);


    }

}
