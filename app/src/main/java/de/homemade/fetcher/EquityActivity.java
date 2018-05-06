package de.homemade.fetcher;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;

public class EquityActivity extends AppCompatActivity {

    String TAG = "FETCHER ";
    String CLASS = "EQUITY ";

    Context context;
    HashMap<String, Double > portfolio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity);

        context = getApplicationContext();

        portfolio = new HashMap<>();

        createPortfolio();

    }

    // create portfolio
    private void createPortfolio(){

        // Gold in Gramm 1 + 100 + 25 + 50
        portfolio.put("Gold", 176.00);

        // Silber in Gramm 1 + 100 + 25 + 250
        portfolio.put("Silber", 376.00);

        // Palladium in Gramm 1 + 100 + 100 + 25
        portfolio.put("Palladium", 226.00);

        // Platin in Gramm 1 + 100 + 25 + 100 + 100
        portfolio.put("Platin", 326.00);

        // Rhodium in Gramm 31.10
        portfolio.put("Rhodium", 31.10);

        // Goldmark in Stueck 1 + 4 + 5
        portfolio.put("Goldmark", 10.00);

        // Goldmuenze in Stueck
        // Fichte   1
        // Kiefer   1
        // Linde    3
        portfolio.put("Goldmuenze", 5.00);

        // Silbermuenze in Stueck
        // Wiener Philharmoniker 20 + 20
        // Cook Island 1 + 20
        portfolio.put("Silbermuenze", 61.00);

        // Palladiummuenze in Stueck
        // Cook Island 1
        portfolio.put("Palladiummuenze", 1.00);

        // Platinmuenze in Stueck
        // Noble 1
        portfolio.put("Platinmuenze", 1.00);

        // Log portfolio
        Gson g = new Gson();
        String json = g.toJson(portfolio);
        Log.i(TAG, "Portfolio: \n" + json);

    }
}
