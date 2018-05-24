package de.homemade.fetcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_1;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_10;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_2;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_3;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_4;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_5;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_6;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_8;
import static de.homemade.fetcher.DatabaseHelper.COLUMN_9;


public class MainActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "MAIN ";

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

    String newsMain;

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

    LinearLayout mainLayoutStatus;
    TextView textDateTime;
    TextView dataDateTime;
    TextView textStatus;
    TextView dataStatus;

    Context context;
    DatabaseHelper dbHelper;
    FetchAsyncTask task;
    Timestamp timestamp;
    Parser parser;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        dbHelper = DatabaseHelper.getInstance(context);
        timestamp = new Timestamp();
        parser = new Parser();

        initAllViews();
        fetchDataFromESG();
        getEquity();
        setDataFromTableIfAny();
        callRssFeed();

        setStatus(context);
        setDate(context);

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

        mainLayoutStatus = findViewById(R.id.mainLayoutStatus);
        textDateTime = findViewById(R.id.textDateTime);
        dataDateTime = findViewById(R.id.dataDateTime);
        textStatus = findViewById(R.id.textStatus);
        dataStatus = findViewById(R.id.dataStatus);

    }



    //fetch data from website every 43200000 ms = 12hrs and online check
    private void fetchDataFromESG() {

        Long timeSinceLastTask = (System.currentTimeMillis()/1000) - timestamp.callLastTimeStamp(context);
        Log.i(TAG, CLASS + "  << time since last task " + "[ "+ timeSinceLastTask +" ] >>");

        // exceute task only if last task has been exceuted min 12hr before
        if(true/*timeSinceLastTask < 43200*/) {

            if (isOnline()) {
                // Start timer
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        try {

                            timestamp.saveNewTimeStamp(timestamp.createTimeStamp(), context);
                            // fetch data via async task
                            task = (FetchAsyncTask) new FetchAsyncTask().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 43200000 = 12hrs
                    }
                }, 0, 43200000);
            } else {
                Log.i(TAG, "device is not online");
                Toast toast = Toast.makeText(context, "device is not online / flightmode", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else {

            Log.i(TAG, CLASS + " time since last task is less than 12hrs" + "[ "+ timeSinceLastTask +" ]");

        }
    }

    // check if device has internet connection and if is filghtmode
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // calculate equity by fetched data and portfolio
    private void getEquity() {

        getEquityButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // refernce check if async task is finished
                // task.getStatus() == AsyncTask.Status.FINISHED && task.getStatus() != AsyncTask.Status.PENDING

                if (isOnline()) {
                    isValueEmpty();
                    Intent intent = new Intent(MainActivity.this, EquityActivity.class);

                    if(newsMain == null){
                        newsMain = "no news received ";
                    }

                    intent.putExtra("news", newsMain);
                    startActivity(intent);

                } else {
                    Log.i(TAG, "data NOT ready");
                    Toast toast = Toast.makeText(context, "data not ready", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    gold = "0";
                    silber = "0";
                    palladium = "0";
                    platin = "0";
                    rhodium = "0";
                    goldMark = "0";
                    goldMunze = "0";
                    silberMark = "0";
                    palladiumMunze = "0";
                    platinMunze = "0";
                }
            }
        });
    }

    // check if value is empty
    private void isValueEmpty(){
        if(gold == null) {
            gold = "0";
        } else if(silber == null){
            silber = "0";
        } else if(palladium == null){
            palladium = "0";
        } else if(platin == null){
            platin = "0";
        } else if(rhodium == null){
            rhodium = "0";
        } else if(goldMark == null){
            goldMark = "0";
        } else if(goldMunze == null){
            goldMunze = "150";
        } else if(silberMark == null){
            silberMark = "0";
        } else if(palladiumMunze == null){
            palladiumMunze = "0";
        } else if(platinMunze == null){
            platinMunze = "0";
        }
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

                // 1 x Goldmunze !!not valid
                // Document documentEgoldmuenze = Jsoup.connect("https://www.scheideanstalt.de/ankaufspreis-goldmuenzen/").get();
                // Elements eGoldMuenze = documentEgoldmuenze.select("[title=10000101-otc]");

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
                Document documentEpalladiumMunze = Jsoup.connect("https://www.scheideanstalt.de/ankaufspreise-palladiummuenzen/").get();
                Elements ePalladiumMunze = documentEpalladiumMunze.select("[title=04020120-otc]");

                // 1 x Platin
                Document documentEplatin = Jsoup.connect("https://www.scheideanstalt.de/platin-ankaufskurse/").get();
                Elements ePlatin = documentEplatin.select("[title=00001200-otc]");

                // 1 x Platin Muenze 1oz = 31,10gr (Feinunze)
                Document documentEplatinMunze = Jsoup.connect("https://www.scheideanstalt.de/ankaufspreise-platinmuenzen/").get();
                Elements ePlatinMunze = documentEplatinMunze.select("[title=10000068-otc]");

                // 1g Rhodium 1oz = 31,10gr (Feinunze)
                Document documentErhodium = Jsoup.connect("https://www.scheideanstalt.de/rhodium-ankaufskurse/").get();
                Elements eRhodium = documentErhodium.select("[title=00001600-mail]");

                gold = eGold.text();
                silber = eSilber.text();
                palladium = ePalladium.text();
                platin = ePlatin.text();
                rhodium = eRhodium.text();

                goldMark = eGoldMark.text();
                goldMunze = "150.00";
                silberMark = eSilberMunze.text();
                palladiumMunze = ePalladiumMunze.text();
                platinMunze = ePlatinMunze.text();

                /*
                try {

                    Document doc = Jsoup.connect("http://www.boerse-frankfurt.de/rohstoffe/nachrichten/").get();
                    Elements links = doc.select("tbody");
                    newsMain = links.text();

                    // Log.i(TAG,CLASS + " News Link Text  " + links.text());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

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
            dataGold.setText(gold  + " €");
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
                    "Gold:          " + gold + " " + "\n" +
                    "Silber:        " + silber + " " + "\n" +
                    "Palladium:     " + palladium + " " + "\n" +
                    "Platin:        " + platin + " " + "\n" +
                    "Rhodium:       " + rhodium + " " + "\n" +
                    "Goldmark:      " + goldMark + " " + "\n" +
                    "Goldmuenze:    " + goldMunze + " " + "\n" +
                    "Silbermark:    " + silberMark + " " + "\n" +
                    "Palladiummark: " + palladiumMunze + " " + "\n" +
                    "Platinmark:    " + platinMunze);

            // Create an instance of SimpleDateFormat used for formatting
            // the string representation of date (month/day/year)
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            // Get the date today using Calendar object.
            Date today = Calendar.getInstance().getTime();
            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            date = df.format(today);

            dbHelper.insertDataIntoPriceTable(
                    setDot(gold),
                    setDot(silber),
                    setDot(palladium),
                    setDot(platin),
                    setDot(rhodium),
                    setDot(goldMark),
                    setDot(goldMunze),
                    setDot(silberMark),
                    setDot(palladiumMunze),
                    setDot(platinMunze),
                    date);

            Toast toast = Toast.makeText(context, "async task completed", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
        // find and replace komme with dot
        private String setDot(String stringWithKomma){
            return stringWithKomma = stringWithKomma.replace(",",".");
        }
    }

    // call rss feed
    private void callRssFeed(){

        // include RSS reader
        String urlToRssFeed = "https://www.boerse-online.de/rss/maerkte";
        parser.execute(urlToRssFeed);

        Log.i(TAG,CLASS + " call rss feed " + urlToRssFeed);

        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {

                for(int i = 0; i < list.size(); i++){
                    String date = String.valueOf(list.get(i) .getPubDate());
                    String content = list.get(i) .getDescription();

                    Log.i(TAG,CLASS + " >>>>>>>>>>>>>>>>>>>>>>>> date   : " + date);
                    Log.i(TAG,CLASS + " >>>>>>>>>>>>>>>>>>>>>>>> content: " + content);
                }
            }

            @Override
            public void onError() {
                Log.i(TAG,CLASS + " parser fail");
            }
        });

    }

    // get data from db table if any and set them in designated views
    private void setDataFromTableIfAny(){

        // db table is not empty or device is not online
        if(!dbHelper.isTableEmpty(DatabaseHelper.TABLE_NAME) || !isOnline()){
            // table is not empty
            Cursor cursor = dbHelper.getAllDataFromDatabase(DatabaseHelper.TABLE_NAME);
            cursor.moveToLast();

            gold = cursor.getString(cursor.getColumnIndex(COLUMN_1));
            silber = cursor.getString(cursor.getColumnIndex(COLUMN_2));
            palladium = cursor.getString(cursor.getColumnIndex(COLUMN_3));
            platin = cursor.getString(cursor.getColumnIndex(COLUMN_4));
            rhodium = cursor.getString(cursor.getColumnIndex(COLUMN_5));

            goldMark = cursor.getString(cursor.getColumnIndex(COLUMN_6));
            silberMark = cursor.getString(cursor.getColumnIndex(COLUMN_8));
            palladiumMunze = cursor.getString(cursor.getColumnIndex(COLUMN_9));
            platinMunze = cursor.getString(cursor.getColumnIndex(COLUMN_10));

            // goldmark title exist twice string needs to be corrected
            // String correctedGoldMark = goldMark.substring(0, Math.min(goldMark.length(), 7));
            // goldMark = correctedGoldMark;

            // Barren Preis pro gramm
            dataGold.setText(gold  + " €");
            dataSilber.setText(silber  + " €");
            dataPalladium.setText(palladium  + " €");
            dataPlatin.setText(platin  + " €");
            dataRhodium.setText(rhodium  + " €");

            // Muenzen
            dataGoldmark.setText(goldMark  + " €");
            dataSilberMark.setText(silberMark  + " €");
            dataPalladiummark.setText(palladiumMunze  + " €");
            dataPlatinmark.setText(platinMunze  + " €");


        } else {

            Log.i(TAG,CLASS + " database is empty or offline" );

        }
    }

    // get / set status of device
    private void setStatus(Context context){

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        String status = netInfo.getDetailedState().toString();

        dataStatus.setText(status);

        Log.i(TAG, CLASS + " status:             " + status);

        if(status.trim().equals("CONNECTED")){
            dataStatus.setTextColor(GREEN);

        } else {
            dataStatus.setTextColor(RED);

        }
    }

    // get  / set last received data
    private void setDate(Context context){
        Long timeStampLong = (timestamp.callLastTimeStamp(context)) * 1000;
        String timeStampString = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date(timeStampLong));

        Log.i(TAG, CLASS + " time / date:        " + timeStampString);

        dataDateTime.setText(timeStampString);

    }
}
