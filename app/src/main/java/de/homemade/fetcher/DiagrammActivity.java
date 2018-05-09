package de.homemade.fetcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.achartengine.GraphicalView;

import java.util.HashMap;

public class DiagrammActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "DIAGRAM ACTY ";

    Context context;
    DatabaseHelper dbHelper;
    HashMap<String, Double >portfolio;

    // general views
    LinearLayout main_diagramm_layout;
    LinearLayout top_diagramm_layout;
    LinearLayout textChartPie;

    RelativeLayout layout_index00;
    TextView diagTextGold;
    TextView diagDataGold;

    RelativeLayout layout_index01;
    TextView diagTextSilber;
    TextView diagDataSilber;

    RelativeLayout layout_index02;
    TextView diagTextPalladium;
    TextView diagDataPalladium;

    RelativeLayout layout_index03;
    TextView diagTextPlatin;
    TextView diagDataPlatin;

    RelativeLayout layout_index04;
    TextView diagTextRhodium;
    TextView diagDataRhodium;

    RelativeLayout layout_index05;
    TextView diagTextCoin;
    TextView diagDataCoin;

    RelativeLayout bottom_text_layout;
    TextView diagTextPortfolio;
    TextView diagDataPortfolioValue;


    // diagramm view
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

        // prepare and init
        initAllViews();
        prepareForDiagramm();

        // get content
        fillPieChart();


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

    // initalize all views
    private void initAllViews(){

        // general views
        main_diagramm_layout = findViewById(R.id.main_diagramm_layout);
        layout_index00 = findViewById(R.id.layout_index00);
        diagTextGold = findViewById(R.id.diagTextGold);
        diagDataGold = findViewById(R.id.diagDataGold);

        layout_index01 = findViewById(R.id.layout_index01);
        diagTextSilber  = findViewById(R.id.diagTextSilber);
        diagDataSilber  = findViewById(R.id.diagDataSilber);

        layout_index02  = findViewById(R.id.layout_index02);;
        diagTextPalladium = findViewById(R.id.diagTextPalladium);
        diagDataPalladium = findViewById(R.id.diagDataPalladium);

        layout_index03  = findViewById(R.id.layout_index03);
        diagTextPlatin = findViewById(R.id.diagTextPlatin);
        diagDataPlatin = findViewById(R.id.diagDataPlatin);

        layout_index04 = findViewById(R.id.layout_index04);
        diagTextRhodium = findViewById(R.id.diagTextRhodium);
        diagDataRhodium = findViewById(R.id.diagDataRhodium);

        layout_index05 = findViewById(R.id.layout_index05);;
        diagTextCoin = findViewById(R.id.diagTextCoin);
        diagDataCoin = findViewById(R.id.diagDataCoin);;

        bottom_text_layout = findViewById(R.id.bottom_text_layout);
        diagTextPortfolio = findViewById(R.id.diagTextPortfolio);
        diagDataPortfolioValue = findViewById(R.id.diagDataPortfolioValue);

        // get and set pie chart diagramm
        chartLayout = findViewById(R.id.chart);
        chartPie = findViewById(R.id.chartPie);

        // get and set line chart diagramm
        LineChartView lcv = new LineChartView();
        chartView = lcv.lineDiagramm(context);
        chartLayout.addView(chartView);

    }
}
