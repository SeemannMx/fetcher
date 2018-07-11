package de.homemade.fetcher;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * calculates all data from db
 */
public class Calculator {

    public ArrayList <String> convertToKroegerRand(ArrayList<String> stringList){
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

}
