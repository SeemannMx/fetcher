package de.homemade.fetcher;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

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
import static de.homemade.fetcher.DatabaseHelper.TABLE_NAME;
import static de.homemade.fetcher.PieChartView.COLOR_BLUE;
import static de.homemade.fetcher.PieChartView.COLOR_DEEP_PETROL;
import static de.homemade.fetcher.PieChartView.COLOR_PETROL;
import static de.homemade.fetcher.PieChartView.COLOR_ULTRA_VIOLETT;
import static de.homemade.fetcher.PieChartView.COLOR_VIOLETT;

public class EquityActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "EQUITY ";

    Context context;
    HashMap<String, Double > portfolio;
    DatabaseHelper dbHelper;

    String gold = "";
    String silber = "";
    String palladium = "";
    String platin = "";
    String rhodium = "";

    String goldMark = "";
    String goldMunze = "";
    String silberMark = "";
    String palladiumMunze = "";
    String platinMunze = "";
    String date = "";

    String purchasedValue;
    String tax;

    String goldPerKroegerRand;
    String silberPerKroegerRand;
    String palladiumPerKroegerRand;
    String platinPerKroegerRand;
    String rhodiumPerKroegerRand;

    String totalEquity = "";

    RelativeLayout equityRelativLayout;
    RelativeLayout layoutPurchased;
    RelativeLayout layoutPresent;
    RelativeLayout layoutTax;
    LinearLayout layouTable;

    TextView textPurchased;
    TextView dataPurchased;
    TextView textPresentValue;
    TextView dataPresentValue;
    TextView textTax;
    TextView dataTax;

    TextView equityGoldText;
    TextView equityGoldProzent;
    TextView equitySilberText;
    TextView equitySilberProzent;
    TextView equityPalladiumText;
    TextView equityPalladiumProzent;
    TextView equityPlatinText;
    TextView equityPlatinProzent;
    TextView equityRhodiumText;
    TextView equityRhodiumProzent;

    ImageButton callImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity);

        context = getApplicationContext();
        dbHelper = DatabaseHelper.getInstance(context);

        portfolio = new HashMap<>();

        initAllViews();
        createPortfolio();
        getSumOfEquity();
        setTextColor();
        setContentinProzent();
        showDiagramm();
        makeCallToESG();

    }


    // create portfolio
    private void createPortfolio(){

        // todo remove reducer in productive stage
        double reducer = 107.45;

        // Gold in Gramm 1 + 100 + 25 + 50
        portfolio.put("Gold", 176.00            /reducer);

        // Silber in Gramm 1 + 100 + 25 + 250
        portfolio.put("Silber", 376.00          /reducer);

        // Palladium in Gramm 1 + 100 + 100 + 25
        portfolio.put("Palladium", 226.00       /reducer);

        // Platin in Gramm 1 + 100 + 25 + 100 + 100
        portfolio.put("Platin", 326.00          /reducer);

        // Rhodium in Gramm 31.10
        portfolio.put("Rhodium", 31.10          /reducer);

        // Goldmark in Stueck 1 + 4 + 5
        portfolio.put("Goldmark", 10.00         /reducer);

        // Goldmuenze in Stueck
        // Fichte   1
        // Kiefer   1
        // Linde    3
        portfolio.put("Goldmuenze", 5.00        /reducer);

        // Silbermuenze in Stueck
        // Wiener Philharmoniker 20 + 20
        // Cook Island 1 + 20
        portfolio.put("Silbermuenze", 61.00     /reducer);

        // Palladiummuenze in Stueck
        // Cook Island 1
        portfolio.put("Palladiummuenze", 1.00   /reducer);

        // Platinmuenze in Stueck
        // Noble 1
        portfolio.put("Platinmuenze", 1.00      /reducer);

        // todo include purchased value in productive stage
        // purchasedValue = "37332,75 €";
        // dataPurchased.setText(purchasedValue);
        // tax = "3918,47 €";
        // dataTax.setText(tax);

        // todo remove in productive stage
        String fakevalue = "11.11 €";
        dataPurchased.setText(fakevalue);
        String fakeTax = "3.14€";
        dataTax.setText(fakeTax);

        // Log portfolio
        Gson g = new Gson();
        String json = g.toJson(portfolio);
        Log.i(TAG, "\n\nPortfolio:\n" + json + "\n\n");

    }

    // initalize all views
    private void initAllViews(){
        equityRelativLayout = findViewById(R.id.equityRelativLayout);
        layoutPurchased = findViewById(R.id.layoutPurchased);
        layoutPresent = findViewById(R.id.layoutPresent);
        layoutTax = findViewById(R.id.layoutTax);
        layouTable = findViewById(R.id.layouTable);

        textPurchased = findViewById(R.id.textPurchased);
        dataPurchased = findViewById(R.id.dataPurchased);

        textPresentValue = findViewById(R.id.textPresentValue);
        dataPresentValue = findViewById(R.id.dataPresentValue);

        textTax = findViewById(R.id.textTax);
        dataTax = findViewById(R.id.dataTax);

        equityGoldText = findViewById(R.id.equityGoldText);
        equityGoldProzent = findViewById(R.id.equityGoldProzent);

        equitySilberText = findViewById(R.id.equitySilberText);
        equitySilberProzent = findViewById(R.id.equitySilberProzent);

        equityPalladiumText = findViewById(R.id.equityPalladiumText);
        equityPalladiumProzent = findViewById(R.id.equityPalladiumProzent);

        equityPlatinText = findViewById(R.id.equityPlatinText );
        equityPlatinProzent = findViewById(R.id.equityPlatinProzent );

        equityRhodiumText = findViewById(R.id.equityRhodiumText);
        equityRhodiumProzent = findViewById(R.id.equityRhodiumProzent);

        // init status of views
        layouTable.setClickable(false);

        callImageButton = findViewById(R.id.callImageButton);

    }

    // get total sum of Equity
    private void getSumOfEquity(){

        Cursor cursor = dbHelper.getAllDataFromDatabase("price_table");

        if(dbHelper.tableIsEmpty("price_table")) {

            cursor.moveToFirst();
            // Value of Ingots
            double goldPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_1)));
            double goldTresure = goldPerGramm * portfolio.get("Gold");

            String tempGl = new DecimalFormat("##.##").format(goldTresure);
            Log.i(TAG, "Gold Tresure:               " + tempGl);

            double silberPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_2)));
            double silberTresure = silberPerGramm * portfolio.get("Silber");
            Log.i(TAG, "Silber Tresure:             " + silberTresure);

            double palladiumPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_3)));
            double palladiumTresure = palladiumPerGramm * portfolio.get("Palladium");
            Log.i(TAG, "Palladium Tresure:          " + palladiumTresure);

            double platinPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_4)));
            double platinTresure = platinPerGramm * portfolio.get("Platin");

            String tempPL = new DecimalFormat("##.##").format(platinTresure);
            Log.i(TAG, "Platin Tresure:             " + tempPL);

            double rhodiumPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_5)));
            double rhodiumTresure = rhodiumPerGramm * portfolio.get("Rhodium");

            String tempRh = new DecimalFormat("##.##").format(rhodiumTresure);
            Log.i(TAG, "Rhodium Tresure:            " + tempRh);

            // Value of Coins
            double goldmarkPerPiece = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_6)));
            double goldmarkTresure = goldmarkPerPiece * portfolio.get("Goldmark");

            String tempGlmrk = new DecimalFormat("##.##").format(goldmarkTresure);
            Log.i(TAG, "Goldmark Tresure:           " + tempGlmrk);

            double goldmuenzePerPiece = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_7)));
            double goldmuenzeTresure = goldmuenzePerPiece * portfolio.get("Goldmuenze");
            Log.i(TAG, "Goldmuenze Tresure:         " + goldmuenzeTresure);

            double silbermuenzePerPiece = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_8)));
            double silbermuenzeTresure = silbermuenzePerPiece * portfolio.get("Silbermuenze");
            Log.i(TAG, "Silbermuenze Tresure:       " + silbermuenzeTresure);

            double pldmuenzePerPiece = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_9)));
            double pldmuenzeTresure = pldmuenzePerPiece * portfolio.get("Palladiummuenze");
            Log.i(TAG, "Palladiummuenze Tresure:    " + pldmuenzeTresure);

            double ptmuenzePerPiece = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_10)));
            double ptmuenzeTresure = ptmuenzePerPiece * portfolio.get("Platinmuenze");
            Log.i(TAG, "Platinmuenze Tresure:       " + ptmuenzeTresure);

            double sumIgnots = goldTresure + silberTresure + palladiumTresure + platinTresure + rhodiumTresure;
            double sumCoins = goldmarkTresure + goldmuenzeTresure + silbermuenzeTresure + pldmuenzeTresure + ptmuenzeTresure;

            double sumEquity = sumIgnots + sumCoins;

            String tempTotalEquity = new DecimalFormat("##.##").format(sumEquity);

            totalEquity = tempTotalEquity + " €";
            Log.i(TAG, "TOTAL EQUITY:               " + totalEquity);


            dataPresentValue.setText(totalEquity);
            layouTable.setClickable(true);

            dbHelper.insertDataIntoPortfolioTable(tempTotalEquity,"10.05.1984");
            dbHelper.insertDataIntoPortfolioTable("20000","11.05.1984");
            dbHelper.insertDataIntoPortfolioTable("15000","12.05.1984");
            dbHelper.insertDataIntoPortfolioTable("15000","13.05.1984");
            dbHelper.insertDataIntoPortfolioTable("25000","14.05.1984");
            dbHelper.insertDataIntoPortfolioTable("30000","15.05.1984");

            cursor.close();


        } else {
            Log.i(TAG, CLASS + " Database is empty");
            layouTable.setClickable(false);

        }
    }


    // calculate kroegerrand
    private String calcKroegerRand(String valueProGramm){
        String result = "";

        valueProGramm = setDot(valueProGramm);
        double valuePerGroger = Double.parseDouble(valueProGramm) * 31.10;

        Log.i(TAG, "Krögerrand 31.10gr: " + valuePerGroger);

        // return string in double digit format
        return result = new DecimalFormat("##.##").format(valuePerGroger);
    }


    // find and replace komme with dot
    private String setDot(String stringWithKomma){
        return stringWithKomma = stringWithKomma.replace(",",".");
    }

    // shift to diagramm activoty on click of table
    private void showDiagramm(){
        layouTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DiagrammActivity.class);
                intent.putExtra("portfolio", portfolio);
                startActivity(intent);
            }
        });
    }

    // check if device has internet connection and if is filghtmode
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    // set color of first char in text
    private void setTextColor(){

        equityGoldText.setTextColor(COLOR_BLUE);
        equitySilberText.setTextColor(COLOR_ULTRA_VIOLETT);
        equityPalladiumText.setTextColor(COLOR_DEEP_PETROL);
        equityPlatinText.setTextColor(COLOR_VIOLETT);
        equityRhodiumText.setTextColor(COLOR_PETROL);

    }

    // set data in prozent per view
    private void setContentinProzent(){

        Cursor cursor = dbHelper.getAllDataFromDatabase(TABLE_NAME);
        cursor.moveToLast();

        String goldProzentString = cursor.getString(cursor.getColumnIndex(COLUMN_1)); // gold
        Double goldProzentDouble = Double.parseDouble(goldProzentString);

        String silberProzentString = cursor.getString(cursor.getColumnIndex(COLUMN_2)); // silber
        Double silberProzentDouble = Double.parseDouble(silberProzentString);

        String pldProzentString = cursor.getString(cursor.getColumnIndex(COLUMN_3)); // palladium
        Double pldProzentDouble = Double.parseDouble(pldProzentString);

        String ptProzentString = cursor.getString(cursor.getColumnIndex(COLUMN_4)); // platin
        Double ptProzentDouble = Double.parseDouble(ptProzentString);

        String rhdProzentString = cursor.getString(cursor.getColumnIndex(COLUMN_5)); // rhodium
        Double rhdProzentDouble = Double.parseDouble(rhdProzentString);

        Double total =  goldProzentDouble +
                        silberProzentDouble +
                        pldProzentDouble +
                        ptProzentDouble +
                        rhdProzentDouble;

        goldProzentString = String.valueOf(new DecimalFormat("##.#").format((100 * goldProzentDouble) / total)) + " %";
        silberProzentString = String.valueOf(new DecimalFormat("##.#").format((100 * silberProzentDouble) / total)) + " %";
        pldProzentString = String.valueOf(new DecimalFormat("##.#").format((100 * pldProzentDouble) / total)) + " %";
        ptProzentString = String.valueOf(new DecimalFormat("##.#").format((100 * ptProzentDouble) / total)) + " %";
        rhdProzentString = String.valueOf(new DecimalFormat("##.#").format((100 * rhdProzentDouble) / total)) + " %";

        Log.i(TAG,CLASS + "\n" +
                                goldProzentString + " \n" +
                                silberProzentString +  " \n" +
                                pldProzentString +  " \n" +
                                ptProzentString +  " \n" +
                                rhdProzentString +  " \n");

        equityGoldProzent.setText(goldProzentString);
        equitySilberProzent.setText(silberProzentString);
        equityPalladiumProzent.setText(pldProzentString);
        equityPlatinProzent.setText(ptProzentString);
        equityRhodiumProzent.setText(rhdProzentString);

    }

    // make call to ESG Rheinstetten
    private void makeCallToESG(){

        callImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ESG Rheinstetten
                String phoneNr = "+4972429535177";

                // Test Nummer
                String testPhoneNr = "+491729083152";

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", testPhoneNr, null));
                startActivity(intent);

                Log.i(TAG, CLASS + " make phone call to " + testPhoneNr);
            }
        });

    }
}
