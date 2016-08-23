package com.androidclass.stockdbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StockDBAdapter {


    static final String SYMBOL = "stocksymbol";
    static final String NAME = "name";
    static final String CURPRICE = "curprice";
    static final String TOTVOLUME = "totvolume";
    static final String CHANGE = "change";
    static final String CHANGEDIR = "changedir";
    static final String ROWID = "_id";


    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table stocks (_id integer primary key autoincrement, "
                    + "stocksymbol text not null, name text not null, curprice text not null, totvolume text not null, change text not null, changedir text not null);";

    private static final String DATABASE_NAME = "stockdata";
    private static final String DATABASE_TABLE = "stocks";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "StockDBDataProvider";
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public StockDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public StockDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createStock(String symbol, String name, String curprice, String totvolume, String change, String changedir) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SYMBOL, symbol);
        initialValues.put(NAME, name);
        initialValues.put(CURPRICE, curprice);
        initialValues.put(TOTVOLUME, totvolume);
        initialValues.put(CHANGE, change);
        initialValues.put(CHANGEDIR, changedir);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public long createStock(String symbol) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SYMBOL, symbol);


        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean updateStock(String symbol, String name, String curprice, String totvolume, String change, String changedir) {

        ContentValues args = new ContentValues();

        args.put(NAME, name);
        args.put(CURPRICE, curprice);
        args.put(TOTVOLUME, totvolume);
        args.put(CHANGE, change);
        args.put(CHANGEDIR, changedir);

        return mDb.update(DATABASE_TABLE, args, SYMBOL + "='" + symbol + "'", null) > 0;
    }

    public boolean deleteAllStocks() {

        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteStock(long rowId) {

        return mDb.delete(DATABASE_TABLE, ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteStock(String symbol) {

        return mDb.delete(DATABASE_TABLE, SYMBOL + "='" + symbol + "'", null) > 0;
    }

    public Cursor fetchAllStocks() {

        return mDb.query(DATABASE_TABLE, new String[]{ROWID, SYMBOL, NAME, CURPRICE, TOTVOLUME, CHANGE,
                CHANGEDIR}, null, null, null, null, null);
    }

    public Cursor fetchStockById(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{ROWID, SYMBOL, NAME, CURPRICE, TOTVOLUME, CHANGE,
                                CHANGEDIR}, ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchStockBySymbol(String symbol) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{ROWID, SYMBOL, NAME, CURPRICE, TOTVOLUME, CHANGE,
                                CHANGEDIR}, SYMBOL + "='" + symbol + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchStockLikeSymbol(String symbol) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{ROWID, SYMBOL, NAME, CURPRICE, TOTVOLUME, CHANGE,
                                CHANGEDIR}, SYMBOL + " like '" + symbol + "%'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public void insertSomeStocks() {

        createStock("MSFT", "Microsoft", "27.50", "200000", "+2.5", "1");
        createStock("AMZN", "Amazon", "127.50", "50000", "-2.5", "-1");
        createStock("GOOG", "Google", "325.00", "120000", "-2.23", "-1");
        createStock("FB", "Facebook", "25.90", "13000", "2.3", "1");
        createStock("QCOM", "Qualcomm", "62.83", "88810", "1.1", "1");
        createStock("INFY", "InfoSys Ltd", "15.80", "230000", "0.0", "0");
        createStock("ORCL", "Oracle", "41.22", "324000", "0.19", "1");
        createStock("INTC", "Intel", "53.48", "90000", "0.38", "1");


    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS stocks");
            onCreate(db);
        }
    }

}
