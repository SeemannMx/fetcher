package de.homemade.fetcher;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MockClass {

    String TAG = "FETCHER ";
    String CLASS = "MOCK CLASS ";

    DatabaseHelper dbHelper;

    // mock data for test case
    public MockClass(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // instert mock data in database table price_list
    public void mockDataPrice(){

        Date today = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(today);

        dbHelper.insertDataIntoPriceTable(
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                "0.0",
                date
        );

        Log.i(TAG, CLASS + " data for price_list table mocked");
    }

    // mock data in database table portfolio
    public void mockDataPortfolio(){

        dbHelper.insertDataIntoPortfolioTable("20000","11.05.1984");
        dbHelper.insertDataIntoPortfolioTable("15000","12.05.1984");
        dbHelper.insertDataIntoPortfolioTable("15000","13.05.1984");
        dbHelper.insertDataIntoPortfolioTable("25000","14.05.1984");
        dbHelper.insertDataIntoPortfolioTable("30000","15.05.1984");

        Log.i(TAG, CLASS + " data for portfilio table  mocked");

    }
}
