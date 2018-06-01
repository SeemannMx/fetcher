package de.homemade.fetcher;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;

import static de.homemade.fetcher.DatabaseHelper.DATE;
import static de.homemade.fetcher.DatabaseHelper.PORTFOLIO_VALUE;

public class LineChartView {

    String TAG = "FETCHER ";
    String CLASS = "LINE DIAGR ";

    public static final int COLOR_GREEN = Color.parseColor("#62c51a");
    public static final int COLOR_ORANGE = Color.parseColor("#ff6c0a");
    public static final int COLOR_BLUE = Color.parseColor("#23bae9");
    public static final int COLOR_PETROL = Color.parseColor("#61e2d1");
    public static final int COLOR_VIOLETT = Color.parseColor("#ac82bc");
    public static final int COLOR_PLUM = Color.parseColor("#9999cc");
    public static final int COLOR_ULTRA_VIOLETT = Color.parseColor("#b26490");
    public static final int COLOR_DEEP_PETROL = Color.parseColor("#577681");

    DatabaseHelper dbHelper;

    public GraphicalView lineDiagramm(Context context) {

        dbHelper = DatabaseHelper.getInstance(context);
        Cursor cursor = dbHelper.getAllDataFromDatabase("portfolio_table");
        cursor.moveToFirst();

        // todo continuee
        // populate the series  with date and value
        TimeSeries seriesS = new TimeSeries("diagramm of portfolio value");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        while(cursor.moveToNext()){

            try{
                Double value = cursor.getDouble(cursor.getColumnIndex(PORTFOLIO_VALUE));

                String dateAsString = cursor.getString(cursor.getColumnIndex(DATE));
                Log.i( TAG, CLASS + " TEMP XXXXXXX DATE : " +  dateAsString);

                Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATE)));

                seriesS.add(date,value);

                Log.i( TAG, CLASS + "\n date: " + date + " value: " + value);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // create renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(COLOR_PLUM);

        // create low max value
        renderer.setDisplayBoundingPoints(true);

        // add oint markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(8);

        // create renderer that controls each single renderer for each series
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        // layout diagramm
        // transparent margins
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));

        // disable pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(50000);
        mRenderer.setYAxisMin(-5000);

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // dataset.addSeries(series);
        dataset.addSeries(seriesS);

        // show grid
        mRenderer.setShowGrid(true); // we show the grid

        cursor.close();

        return ChartFactory.getLineChartView(context,dataset,mRenderer);

    }

}
