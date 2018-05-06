package de.homemade.fetcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "MAIN ";

    String gold = "";
    String silber = "";
    String palladium = "";
    String platin = "";
    String rhodium = "";

    String goldMark = "";
    String silberMark = "";
    String palladiumMunze = "";
    String platinMunze = "";

    RelativeLayout mainRelativLayout;
    ScrollView scrollView;

    ImageView imageGold;
    TextView dataGold;

    ImageView imageSilber;
    TextView dataSilber;

    ImageView imagePalladium;
    TextView dataPalladium;

    ImageView imagePlatin;
    TextView dataPlatin;

    ImageView imageRhodium;
    TextView dataRhodium;

    ImageView imageSAP;
    TextView dataSAP;

    TextView textGoldmark;
    TextView dataGoldmark;

    TextView textSilbermark;
    TextView dataSilberMark;

    TextView textPalladiummark;
    TextView dataPalladiummark;

    TextView textPlatinmark;
    TextView dataPlatinmark;

    LinearLayout bottomLine;
    Button getEquityButtom;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        initAllViews();
        fetchDataFromESG();

        getEquity();

    }

    // initalize all views
    private void initAllViews(){

        mainRelativLayout = findViewById(R.id.mainRelativLayout);
        scrollView = findViewById(R.id.scrollView);

        imageGold = findViewById(R.id.imageGold);
        dataGold = findViewById(R.id.dataGold);

        imageSilber = findViewById(R.id.imageSilber);
        dataSilber = findViewById(R.id.dataSilber);

        imagePalladium = findViewById(R.id.imagePalladium);
        dataPalladium = findViewById(R.id.dataPalladium);

        imagePlatin = findViewById(R.id.imagePlatin);
        dataPlatin = findViewById(R.id.dataPlatin);

        imageRhodium = findViewById(R.id.imageRhodium);
        dataRhodium = findViewById(R.id.dataRhodium);

        imageSAP = findViewById(R.id.imageSAP);
        dataSAP = findViewById(R.id.dataSAP);

        textGoldmark = findViewById(R.id.textGoldmark);
        dataGoldmark = findViewById(R.id.dataGoldmark);

        textSilbermark = findViewById(R.id.textSilbermark);
        dataSilberMark = findViewById(R.id.dataSilbermark);

        textPalladiummark = findViewById(R.id.textPalladiummark);
        dataPalladiummark = findViewById(R.id.dataPalladiummark);

        textPlatinmark = findViewById(R.id.textPlatinmark);
        dataPlatinmark = findViewById(R.id.dataPlatinmark);

        bottomLine = findViewById(R.id.bottomLine);
        getEquityButtom = findViewById(R.id.getEquityButtom);

    }



    //Every 43200000 ms = 12hrs
    private void fetchDataFromESG() {

        // Start timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {

                try{

                    // fetch data via async task
                    new FetchAsyncTask().execute();

                }
                catch (Exception e) {

                    e.printStackTrace();

                }

                // 43200000 = 12hrs
            }
        }, 0, 43200000);
    }



    // inner class to fetch data
    public class FetchAsyncTask extends AsyncTask {

        Activity activity;

        @Override
        protected Object doInBackground(Object[] objects) {


            try {
                Document document = Jsoup.connect("https://www.scheideanstalt.de/gold-ankaufskurse/").get();
                Log.i("DATA Title", document.title());

                // 1g handelsfaehiges Gold
                Elements eGold = document.select("[title=00001000-mail]");

                // 1 x Goldmark
                Document documentEgold = Jsoup.connect("https://www.scheideanstalt.de/ankaufspreis-goldmuenzen/").get();
                Elements eGoldMark = documentEgold.select("[title=10000101-otc]");

                // 1g handelsfaehiges Silber
                Document documentEsilber = Jsoup.connect("https://www.scheideanstalt.de/silber-ankaufskurse/").get();
                Elements eSilber = documentEsilber.select("[title=10201714-otc]");

                // 1 x Silbermuenze 1oz = 31,10gr (Feinunze) <span title=10202339-otc">13,75</span>
                Document documentEsilberMunzen = Jsoup.connect("https://www.scheideanstalt.de/ankaufspreise-silbermuenzen/").get();
                Elements eSilberMunze = documentEsilberMunzen.select("[title=10202339-otc]");

                // 1g Palladium
                Document documentEpalladium = Jsoup.connect("https://www.scheideanstalt.de/palladium-ankaufskurse/").get();
                Elements ePalladium = documentEpalladium.select("[title=00001300-mail]");

                // 1 x Palladium Muenze 1oz = 31,10gr (Feinunze)
                Document documentEpalladiumMunze= Jsoup.connect("https://www.scheideanstalt.de/ankaufspreise-palladiummuenzen/").get();
                Elements ePalladiumMunze = documentEpalladiumMunze.select("[title=04020120-otc]");

                // 1 x Platin
                Document documentEplatin= Jsoup.connect("https://www.scheideanstalt.de/platin-ankaufskurse/").get();
                Elements ePlatin = documentEplatin.select("[title=00001200-otc]");

                // 1 x Platin Muenze 1oz = 31,10gr (Feinunze)
                Document documentEplatinMunze= Jsoup.connect("https://www.scheideanstalt.de/ankaufspreise-platinmuenzen/").get();
                Elements ePlatinMunze = documentEplatinMunze.select("[title=10000068-otc]");

                // 1g Rhodium 1oz = 31,10gr (Feinunze)
                Document documentErhodium= Jsoup.connect("https://www.scheideanstalt.de/rhodium-ankaufskurse/").get();
                Elements eRhodium = documentErhodium.select("[title=00001600-mail]");

                gold = eGold.text();
                silber = eSilber.text();
                palladium = ePalladium.text();
                platin = ePlatin.text();
                rhodium = eRhodium.text();

                goldMark = eGoldMark.text();
                silberMark = eSilberMunze.text();
                palladiumMunze = ePalladiumMunze.text();
                platinMunze = ePlatinMunze.text();


            } catch (IOException e) {
                e.printStackTrace();
            }


            return gold;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // goldmark title exist twice string needs to be corrected
            String correctedGoldMark = goldMark.substring(0, Math.min(goldMark.length(), 7));
            goldMark = correctedGoldMark;

            // Barren Preis pro gramm
            dataGold.setText(gold + " €");
            dataSilber.setText(silber + " €");
            dataPalladium.setText(palladium + " €");
            dataPlatin.setText(platin + " €");
            dataRhodium.setText(rhodium + " €");

            // Muenzen
            dataGoldmark.setText(goldMark + " €");
            dataSilberMark.setText(silberMark + " €");
            dataPalladiummark.setText(palladiumMunze + " €");
            dataPlatinmark.setText(platinMunze + " €");

            Log.i(TAG, CLASS + " \n" +
                    "Gold:          " + gold + " "              + "\n" +
                    "Silber:        " + silber + " "            + "\n" +
                    "Palladium:     " + palladium + " "         + "\n" +
                    "Platin:        " + platin + " "            + "\n" +
                    "Rhodium:       " + rhodium + " "           + "\n" +
                    "Goldmark:      " + goldMark + " "          + "\n" +
                    "Silbermark:    " + silberMark + " "        + "\n" +
                    "Palladiummark: " + palladiumMunze + " "    + "\n" +
                    "Platinmark:    " + platinMunze);

            String goldPerKroegerRand = calcKroegerRand(gold);
            String silberPerKroegerRand = calcKroegerRand(silber);
            String palladiumPerKroegerRand = calcKroegerRand(palladium);
            String platinPerKroegerRand = calcKroegerRand(platin);
            String rhodiumPerKroegerRand = calcKroegerRand(rhodium);

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
    }

    // calculate equity by fetched data and portfolio
    private void getEquity(){

        getEquityButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EquityActivity.class);
                startActivity(intent);

            }
        });

    }
}
