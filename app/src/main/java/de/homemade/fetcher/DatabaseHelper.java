package de.homemade.fetcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * Databasehelper simplyfies work with SQLitedatabases be autocreating
 * onCreate(.....) and onUpgrade(.....)
 *
 * Created by tkallinich on 09.08.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQL HELPER";
    private static final String ACTIVITY_CLASS = " ";

    public static final String DATABASE_NAME = "Investment_Database";
    public static final String TABLE_NAME = "price_table";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_0 = "_ID";
    public static final String COLUMN_1 = "GOLD";
    public static final String COLUMN_2 = "SILBER";
    public static final String COLUMN_3 = "PALLADIUM";
    public static final String COLUMN_4 = "PLATIN";
    public static final String COLUMN_5 = "RHODIUM";
    public static final String COLUMN_6 = "GOLDMARK";
    public static final String COLUMN_7 = "GOLDMUENZE";
    public static final String COLUMN_8 = "SILBERMUNEZE";
    public static final String COLUMN_9 = "PALLADIUMMUENZE";
    public static final String COLUMN_10 = "PLATINMUENZE";
    public static final String COLUMN_11 = "DATE";


    public static final String TABLE_NAME_PORTFOLIO ="portfolio_table";
    public static final String ID = "_ID";
    public static final String PORTFOLIO_VALUE = "PORTFOLIO_VALUE";
    public static final String DATE = "DATE";

    Context context;
    private static DatabaseHelper myInstance;

    /**
     * Constructor given context from main activity when its called
     * sets the Database given_name
     * DatabaseVersion = 1
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * creates an instance of class Database helper to guarantees that only one
     * database helper will exist across the entire application lifecycle
     * @param context
     * @return instance of database
     */
    public static DatabaseHelper getInstance(Context context){
        // use the app context which will ensure that you don't accidentlly leak Activitys context

        if(myInstance == null){
            myInstance = new DatabaseHelper((context.getApplicationContext()));
        }
        return  myInstance;
    }


    // creates database with tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "("
                + COLUMN_0 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_1 + " TEXT,"
                + COLUMN_2 + " TEXT,"
                + COLUMN_3 + " TEXT,"
                + COLUMN_4 + " TEXT,"
                + COLUMN_5 + " TEXT,"
                + COLUMN_6 + " TEXT,"
                + COLUMN_7 + " TEXT,"
                + COLUMN_8 + " TEXT,"
                + COLUMN_9 + " TEXT,"
                + COLUMN_10 + " TEXT,"
                + COLUMN_11 + " TEXT"

                + ");");

        Log.i(TAG,ACTIVITY_CLASS + " " + TABLE_NAME + " " + "created");

        sqLiteDatabase.execSQL("create table " + TABLE_NAME_PORTFOLIO + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + PORTFOLIO_VALUE + " TEXT,"
                + DATE + " TEXT"

                + ");");

        Log.i(TAG,ACTIVITY_CLASS + " " + TABLE_NAME_PORTFOLIO + " " + "created");

    }

    /**
     * upGrades an Database with ....
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    // @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PORTFOLIO);

        onCreate(sqLiteDatabase);

        Log.i(TAG, ACTIVITY_CLASS + " DB onUpgrade");

    }

    // instert data
    public boolean insertDataIntoPriceTable(String gold,
                                            String silber,
                                            String palladium,
                                            String platin,
                                            String rhodium,
                                            String goldmark,
                                            String goldmuenze,
                                            String silbermuenze,
                                            String palladiummuenze,
                                            String platinmuenze,
                                            String date){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_1, gold);
        contentValues.put(COLUMN_2, silber);
        contentValues.put(COLUMN_3, palladium);
        contentValues.put(COLUMN_4, platin);
        contentValues.put(COLUMN_5, rhodium);
        contentValues.put(COLUMN_6, goldmark);
        contentValues.put(COLUMN_7, goldmuenze);
        contentValues.put(COLUMN_8, silbermuenze);
        contentValues.put(COLUMN_9, palladiummuenze);
        contentValues.put(COLUMN_10, platinmuenze);
        contentValues.put(COLUMN_11, date);


        long result= db.insert(TABLE_NAME,null, contentValues);

        String reslutforLog = String.valueOf(result);

        // -1 means not data insterted
        if (result == -1){
            Log.i(TAG, ACTIVITY_CLASS+ " " + TABLE_NAME +
                                " DB on insterData " + reslutforLog + " false");
            return false;
        }

        return true;
    }


    public boolean insertDataIntoPortfolioTable(String portfolioValue, String date){

        // Log.i(TAG, ACTIVITY_CLASS + " enter insertDataIntoPriceTable");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(PORTFOLIO_VALUE, portfolioValue);
        contentValues.put(DATE, date);

        long result= db.insert(TABLE_NAME_PORTFOLIO,null, contentValues);

        String reslutforLog = String.valueOf(result);

        // -1 means not data insterted
        if (result == -1){
            Log.i(TAG, ACTIVITY_CLASS + " " + TABLE_NAME_PORTFOLIO +
                        " DB on insterData " + reslutforLog + " false");
            return false;
        }

        return true;
    }

    // get all data from table
    public Cursor getAllDataFromDatabase(String tableName){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from " + tableName, null);

        result.moveToFirst();
        String output = "";

        if(tableName.equals(TABLE_NAME)) {

            while (result.moveToNext()) {

                output = result.getString(result.getColumnIndex(COLUMN_0)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_11)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_1)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_2)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_3)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_4)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_5)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_6)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_7)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_8)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_9)) + " "
                        + result.getString(result.getColumnIndex(COLUMN_10));

                Log.i(TAG, ACTIVITY_CLASS + " " + output);
            }

        } else if(tableName.equals(TABLE_NAME_PORTFOLIO)){

            while (result.moveToNext()) {

                output = result.getString(result.getColumnIndex(ID)) + " "
                        + result.getString(result.getColumnIndex(PORTFOLIO_VALUE)) + " "
                        + result.getString(result.getColumnIndex(DATE));

                // Log.i(TAG, ACTIVITY_CLASS + " " + output);
            }
        }

        // Log.i(TAG, ACTIVITY_CLASS + " table: " + tableName + ", count:  " + result.getCount());

        return result;
    }

    // delete data from price table
    public void deletePriceTable(){
        Log.i(TAG, ACTIVITY_CLASS + " ENTER DELETE DB");
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);

        db.execSQL("delete from " + TABLE_NAME);
        Log.i(TAG,ACTIVITY_CLASS +  " EXIT DELETE DB");
    }

    // delete data from portfolio table
    public void deletePortfolioTable(){
        Log.i(TAG, ACTIVITY_CLASS + " ENTER DELETE DB");
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME_PORTFOLIO, null, null);

        db.execSQL("delete from " + TABLE_NAME_PORTFOLIO);
        Log.i(TAG,ACTIVITY_CLASS +  " EXIT DELETE DB");
    }

    // get all data from given colom
    public Cursor getDataFromPriceTable(String colom){
        // Log.i(TAG,ACTIVITY_CLASS + " enter getData from Database");

        SQLiteDatabase db = this.getReadableDatabase();

        // projection -> coloums im intrested in
        String[] coloums ={COLUMN_0, COLUMN_1, COLUMN_2, COLUMN_3, COLUMN_4,
                COLUMN_5, COLUMN_5, COLUMN_6, COLUMN_7, COLUMN_8, COLUMN_9,
                COLUMN_10, COLUMN_10, COLUMN_11};

        String selection = colom + " like ?";

        Cursor cursor = db.query(TABLE_NAME, coloums, selection,
                null, null, null, null);

        // if while block is useless !!!
        cursor.moveToFirst();

        // for log out put only
        String output = cursor.getString(cursor.getColumnIndex(COLUMN_0)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_11)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_1)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_11)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_1)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_2)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_3)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_4)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_5)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_6)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_7)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_8)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_9)) + " "
                + cursor.getString(cursor.getColumnIndex(COLUMN_10));

        // Log.i(TAG, ACTIVITY_CLASS + " " + data + " is " + output);

            while(cursor.moveToNext()){

                // for log out put only
                output = cursor.getString(cursor.getColumnIndex(COLUMN_0)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_11)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_1)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_11)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_1)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_2)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_3)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_4)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_5)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_6)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_7)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_8)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_9)) + " "
                        + cursor.getString(cursor.getColumnIndex(COLUMN_10));

                // Log.i(TAG, ACTIVITY_CLASS + " " + data + " is " + output);
            }

        // Log.i(TAG, ACTIVITY_CLASS + " exit getData from Database");

        return cursor;
    }

    // get all data from given colom
    public Cursor getDataFromPortfolioTable(String colom){
        // Log.i(TAG,ACTIVITY_CLASS + " enter getData from Database");

        SQLiteDatabase db = this.getReadableDatabase();

        // projection -> coloums im intrested in
        String[] coloums ={PORTFOLIO_VALUE,DATE};

        // COLUMN_5 contains employment_status_Name;
        // String selection = COLUMN_5 + " like ?"; // returns coloum im intrested in
        String selection = colom + " like ?";

        Cursor cursor = db.query(TABLE_NAME, coloums, selection,
                null, null, null, null);

        cursor.moveToFirst();

        // for log out put only
        String output = cursor.getString(cursor.getColumnIndex(ID)) + " "
                + cursor.getString(cursor.getColumnIndex(PORTFOLIO_VALUE)) + " "
                + cursor.getString(cursor.getColumnIndex(DATE));

        // Log.i(TAG, ACTIVITY_CLASS + " " + data + " is " + output);

        while(cursor.moveToNext()){

            // for log out put only
            output = cursor.getString(cursor.getColumnIndex(ID)) + " "
                    + cursor.getString(cursor.getColumnIndex(PORTFOLIO_VALUE)) + " "
                    + cursor.getString(cursor.getColumnIndex(DATE));

            // Log.i(TAG, ACTIVITY_CLASS + " " + data + " is " + output);
        }

        // Log.i(TAG, ACTIVITY_CLASS + " exit getData from Database");

        return cursor;
    }

}
