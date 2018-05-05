package de.homemade.fetcher;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "MAIN ";

    TextView data;
    String gold = "";
    String silber = "";
    String palladium = "";
    String platin = "";
    String rhodium = "";

    String goldMark = "";
    String silberMark = "";
    String palladiumMunze = "";
    String platinMunze = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = findViewById(R.id.data);

        fetchDataFromESG();


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

            data.setText(gold + "€");

            // goldmark title exist twice string needs to be corrected
            String correctedGoldMark = goldMark.substring(0, Math.min(goldMark.length(), 7));
            goldMark = correctedGoldMark;

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

        }
    }
}
