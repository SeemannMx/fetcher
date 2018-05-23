package de.homemade.fetcher;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.achartengine.GraphicalView;

import java.text.DecimalFormat;
import java.util.HashMap;

import static de.homemade.fetcher.DatabaseHelper.COLUMN_1;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_10;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_2;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_3;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_4;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_5;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_6;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_7;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_8;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_9;

public class DiagrammActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "DIAGRAM ACTY ";

    Context context;
    DatabaseHelper dbHelper;
    HashMap<String, Double >portfolio;
    HashMap<String, Double >sumOfItems;

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

        // fill ie chart
        fillPieChart();

        // fill line chart
        fillLineChart();

    }

    // prepera and log for diagramm
    private void prepareForDiagramm(){
        Intent intent = getIntent();
        portfolio = (HashMap<String, Double>) intent.getSerializableExtra("portfolio");
        Gson g = new Gson();
        g.toJson(portfolio);

        sumOfItems = new HashMap<String, Double>();

        // fill textviews and create new hashmap for pie chart
        setTotalPerItem();

    }

    // fill pie chart from database
    private void fillPieChart(){

        chartViewPie = PieChartView.getNewInstance(context, sumOfItems);
        chartPie.addView(chartViewPie);

    }

    // fill line chart from database
    private void fillLineChart(){
        // get and set line chart diagramm

        LineChartView lcv = new LineChartView();
        chartView = lcv.lineDiagramm(context);
        chartLayout.addView(chartView);

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



    }

    // set total equity per item
    private void setTotalPerItem(){

        // get data from portfolio
        Double totalGold = portfolio.get("Gold");                   // 176
        Double totalSilber = portfolio.get("Silber");               // 376
        Double totalPlatin = portfolio.get("Platin");               // 326
        Double totalPalladium = portfolio.get("Palladium");         // 226
        Double totalRhodium = portfolio.get("Rhodium");             // 31.1
        Double totalGoldmark = portfolio.get("Goldmark");           // 10
        Double totalGoldmuenze = portfolio.get("Goldmuenze");       // 5
        Double totalSilberMuenze = portfolio.get("Silbermuenze");   // 61
        Double totalPldMuenze = portfolio.get("Palladiummuenze");   // 1
        Double totalPtMuenze = portfolio.get("Platinmuenze");       // 1

        // prepere database table
        Cursor cursor = dbHelper.getAllDataFromDatabase("price_table");
        cursor.moveToLast();

        // sum of gold
        double presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_1)));
        double result = totalGold * presentPrice;

        // add to Hash map, for a later use in pie chart
        sumOfItems.put("sumGold", result);

        // convert double in doubledigit number
        String goldToView = String.valueOf(new DecimalFormat("##.##").format(result));

        // set data in view
        diagDataGold.setText(goldToView);

        // sum of silber
        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_2)));
        result = totalSilber * presentPrice;

        // add to Hash map, for a later use in pie chart
        sumOfItems.put("sumSilber", result);

        // convert double in doubledigit number
        String silberToView = String.valueOf(new DecimalFormat("##.##").format(result));

        // sum of palladium
        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_3)));
        result = totalPalladium * presentPrice;

        // add to Hash map, for a later use in pie chart
        sumOfItems.put("sumPalladium", result);

        // convert double in doubledigit number
        String pldToView = String.valueOf(new DecimalFormat("##.##").format(result));

        // sum of platin
        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_4)));
        result = totalPlatin * presentPrice;

        // add to Hash map, for a later use in pie chart
        sumOfItems.put("sumPlatin", result);

        // convert double in doubledigit number
        String ptToView = String.valueOf(new DecimalFormat("##.##").format(result));

        // sum of rhodium
        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_5)));
        result = totalRhodium * presentPrice;

        // add to Hash map, for a later use in pie chart
        sumOfItems.put("sumRhodium", result);

        // convert double in doubledigit number
        String rhdToView = String.valueOf(new DecimalFormat("##.##").format(result));

        // sum of coins
        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_6)));
        result = totalGoldmark * presentPrice;

        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_7)));
        result = result + totalGoldmuenze * presentPrice;

        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_8)));
        result = result + totalSilberMuenze * presentPrice;

        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_9)));
        result = result + totalPldMuenze * presentPrice;

        presentPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_10)));
        result = result + totalPtMuenze * presentPrice;

        // add to Hash map, for a later use in pie chart
        sumOfItems.put("sumCoins", result);

        // convert double in doubledigit number
        String coinToView = String.valueOf(new DecimalFormat("##.##").format(result));

        // set data in view
        diagDataGold.setText(goldToView);
        diagDataSilber.setText(silberToView);
        diagDataPalladium.setText(pldToView);
        diagDataPlatin.setText(ptToView);
        diagDataRhodium.setText(rhdToView);
        diagDataCoin.setText(coinToView);

        Gson g = new Gson();
        Log.i(TAG, CLASS + "sumOfItems: \n" + g.toJson(sumOfItems));

        cursor.close();
    }
}
