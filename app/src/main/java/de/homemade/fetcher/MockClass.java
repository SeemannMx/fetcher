package de.homemade.fetcher;

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

    // instert mock data in database table
    public void mockData(){

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
    }

}
