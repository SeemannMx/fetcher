package de.homemade.fetcher;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static de.homemade.fetcher.DatabaseHelper.COLUMN_1;

/**
 * calculates all data from db
 */
public class Calculator {

    public ArrayList <String> convert(ArrayList<String> stringList){
        ArrayList<String> list = new ArrayList<String>();
        DecimalFormat dformat = new DecimalFormat("##.##");

        for(int i = 0; i< stringList.size(); i++) {

            Double value = setDot(stringList.get(i)) * 31.1;
            String convertedValue = dformat.format(value);

            list.add(i, convertedValue);

        }

        return list;
    }


    // find and replace komme with dot
    private Double setDot(String stringWithKomma){
        Double value = Double.parseDouble(stringWithKomma.replace(",","."));
        return value;
    }

    public void showEquity(Context context){

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursor = dbHelper.getAllDataFromDatabase(DatabaseHelper.TABLE_NAME);
        cursor.moveToLast();

        String g = cursor.getString(cursor.getColumnIndex(COLUMN_1));
        Double v = Double.parseDouble(g) * 176.0;

        Log.i("V : ", v + " €");
        cursor.close();

    }

}
