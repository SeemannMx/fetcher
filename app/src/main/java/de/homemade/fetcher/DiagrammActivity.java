package de.homemade.fetcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.achartengine.GraphicalView;

import java.util.HashMap;

public class DiagrammActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "DIAGRAM ACTY ";

    Context context;
    DatabaseHelper dbHelper;
    HashMap<String, Double >portfolio;

    LinearLayout main_diagramm_layout;
    LinearLayout chartPie;
    LinearLayout chartLayout;
    GraphicalView chartView;
    GraphicalView chartViewPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagramm);

        context = getApplicationContext();
        dbHelper = DatabaseHelper.getInstance(context);

        prepareForDiagramm();
        fillPieChart();

        main_diagramm_layout = findViewById(R.id.main_diagramm_layout);
        chartLayout = findViewById(R.id.chart);
        chartPie = findViewById(R.id.chartPie);


        LineChartView lcv = new LineChartView();
        chartView = lcv.lineDiagramm(context);
        chartLayout.addView(chartView);
    }

    // prepera and log for diagramm
    private void prepareForDiagramm(){
        Intent intent = getIntent();
        portfolio = (HashMap<String, Double>) intent.getSerializableExtra("portfolio");
        Gson g = new Gson();
        g.toJson(portfolio);
    }

    // fill pie chart from database
    private void fillPieChart(){

        chartViewPie = PieChartView.getNewInstance(context, portfolio);
        chartPie.addView(chartViewPie);

    }

}
