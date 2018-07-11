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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    NewsExtractor newsExtractor;

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

    String newsEquity = "";

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

    RelativeLayout layoutNews;
    ScrollView scrollViewNews;
    TextView newsText;

    ImageButton callImageButton;

    Parser parser;
    MockClass test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity);

        context = getApplicationContext();
        dbHelper = DatabaseHelper.getInstance(context);
        newsExtractor = new NewsExtractor();
        parser = new Parser();
        portfolio = new HashMap<>();
        test = new MockClass(dbHelper);

        initAllViews();
        createPortfolio();
        getSumOfEquity();
        setContentinProzent();
        setTextColor();
        showDiagramm();
        makeCallToESG();
        callRssFeed();

    }


    // create portfolio
    private void createPortfolio(){

        // todo remove reducer in productive stage
        double reducer = 1;

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
        purchasedValue = "37332,75 €";
        dataPurchased.setText(purchasedValue);
        tax = "3918,47 €";
        dataTax.setText(tax);

        // todo remove in productive stage
        // String fakevalue = "11.11 €";
        // dataPurchased.setText(fakevalue);
        // String fakeTax = "3.14€";
        // dataTax.setText(fakeTax);

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

        layoutNews =  findViewById(R.id.layoutNews);
        scrollViewNews = findViewById(R.id.scrollViewNews);
        newsText = findViewById(R.id.newsText);

        callImageButton = findViewById(R.id.callImageButton);

        // init status of views
        layouTable.setClickable(false);

    }

    // get total sum of Equity
    private void getSumOfEquity() {

        Cursor cursor = dbHelper.getAllDataFromDatabase("price_table");

        if (!dbHelper.tableIsEmpty("price_table") && cursor.getCount() > 0) {

            // declare format
            DecimalFormat df = new DecimalFormat("##.##");

            cursor.moveToLast();
            // Value of Ingots
            double goldPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_1)));
            double goldTresure = goldPerGramm * portfolio.get("Gold");

            String tempGl = df.format(goldTresure);
            Log.i(TAG, "Gold Tresure:               " + tempGl);

            double silberPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_2)));
            double silberTresure = silberPerGramm * portfolio.get("Silber");
            Log.i(TAG, "Silber Tresure:             " + silberTresure);

            double palladiumPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_3)));
            double palladiumTresure = palladiumPerGramm * portfolio.get("Palladium");
            Log.i(TAG, "Palladium Tresure:          " + palladiumTresure);

            double platinPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_4)));
            double platinTresure = platinPerGramm * portfolio.get("Platin");

            String tempPL = df.format(platinTresure);
            Log.i(TAG, "Platin Tresure:             " + tempPL);

            double rhodiumPerGramm = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_5)));
            double rhodiumTresure = rhodiumPerGramm * portfolio.get("Rhodium");

            String tempRh = df.format(rhodiumTresure);
            Log.i(TAG, "Rhodium Tresure:            " + tempRh);

            // Value of Coins
            double goldmarkPerPiece = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_6)));
            double goldmarkTresure = goldmarkPerPiece * portfolio.get("Goldmark");

            String tempGlmrk = df.format(goldmarkTresure);
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

            String tempTotalEquity = df.format(sumEquity);

            totalEquity = tempTotalEquity + " €";
            Log.i(TAG, "TOTAL EQUITY:               " + totalEquity);

            dataPresentValue.setText(totalEquity);
            layouTable.setClickable(true);

            // mock data Todo test data
            // test.mockDataPortfolio();

            Date today = Calendar.getInstance().getTime();
            String date = new SimpleDateFormat("dd.MM.yyyy").format(today);

            // set test data
            dbHelper.insertDataIntoPortfolioTable(totalEquity, date);

        } else {
            Log.i(TAG, CLASS + " Database is empty");
            layouTable.setClickable(false);
        }
        cursor.close();

    }

    // find and replace komme with dot
    private String setDot(String stringWithKomma){
        return stringWithKomma.replace(",",".");
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
        // declare format
        DecimalFormat df = new DecimalFormat("##.#");

        if( cursor.getCount() > 0) {

            cursor.moveToLast();

            // calculate total value from weight
            Double total = portfolio.get("Gold") + portfolio.get("Silber") +
                    portfolio.get("Palladium") + portfolio.get("Platin") +
                    portfolio.get("Rhodium");

            // get value
            Double au = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_1)));   // gold
            Double countAu = portfolio.get("Gold");
            String goldProzentString = String.valueOf(df.format((100 * countAu) / total));

            Double ag = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_2))); // silber
            Double countAg = portfolio.get("Silber");
            String silberProzentString = String.valueOf(df.format((100 * countAg) / total));

            Double pld = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_3)));    // palladium
            Double countPld = portfolio.get("Palladium");
            String pldProzentString = String.valueOf(df.format((100 * countPld) / total));

            Double pt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_4)));     // platin
            Double countPt = portfolio.get("Platin");
            String ptProzentString = String.valueOf(df.format((100 * countPt) / total));

            Double rhd = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_5)));    // rhodium
            Double countRh = portfolio.get("Rhodium");
            String rhdProzentString = String.valueOf(df.format((100 * countRh) / total));

            Double auMk = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_6)));    // goldmark
            Double auMz = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_7)));    // goldmuenze
            Double agMz = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_8)));    // silbermuenze
            Double pldMz = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_9)));   // pallidiummuenze
            Double ptMz = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_10)));   // platinmuenze

            Double coins = portfolio.get("Goldmark") * auMk +
                    portfolio.get("Goldmuenze") * auMz +
                    portfolio.get("Silbermuenze") * agMz+
                    portfolio.get("Palladiummuenze") * pldMz+
                    portfolio.get("Platinmuenze") * ptMz;

            // calculate total value from value
            Double totalValue = portfolio.get("Gold") * au +
                    portfolio.get("Silber") * ag +
                    portfolio.get("Palladium") * pld +
                    portfolio.get("Platin") * pt +
                    portfolio.get("Rhodium") * rhd;


            Log.i(TAG, CLASS + "\n" +
                    goldProzentString + " \n" +
                    silberProzentString + " \n" +
                    pldProzentString + " \n" +
                    ptProzentString + " \n" +
                    rhdProzentString + " \n");

            Double t1 = total;
            Double t2 = countAu + countAg + countPld + countPt + countRh;
            Double t3 = totalValue + coins;
            Log.i(TAG, CLASS + t1 + " = " + t2 + " = " + " " + t3 + " = " + coins);

            // set in view
            equityGoldProzent.setText(goldProzentString);
            equitySilberProzent.setText(silberProzentString);
            equityPalladiumProzent.setText(pldProzentString);
            equityPlatinProzent.setText(ptProzentString);
            equityRhodiumProzent.setText(rhdProzentString);

        }

        cursor.close();

    }

    // make call to ESG Rheinstetten
    private void makeCallToESG(){

        callImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate);

            // listener to create order on actions for animation
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // show animation an nothing else
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    // ESG Rheinstetten
                    String phoneNr = "+4972429535177";

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNr, null));
                    startActivity(intent);

                    Log.i(TAG, CLASS + " make phone call to " + phoneNr);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            callImageButton.startAnimation(anim);

            }
        });

    }

    // call rss feed
    private void callRssFeed(){

        if(isOnline()) {
            // include RSS reader
            String urlToRssFeed = "https://www.boerse-online.de/rss/maerkte";
            parser.execute(urlToRssFeed);

            Log.i(TAG, CLASS + " call rss feed " + urlToRssFeed);

            parser.onFinish(new Parser.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(ArrayList<Article> list) {
                    Date date;
                    String news = "";

                    // refactor all unicode to regular text
                    // list = newsExtractor.convertUnicodeToString(list);

                    // create the news String to show in view
                    newsEquity = newsExtractor.createNewsString(list);
                    newsText.setText(newsEquity);

                }

                @Override
                public void onError() {
                    Log.i(TAG, CLASS + " parser fail");
                }
            });

        } else {
            Log.i(TAG, CLASS + " device is not online - no rss can be fetched");

        }

    }

    // check if device has internet connection or if is filghtmode
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
