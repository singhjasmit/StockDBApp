package com.androidclass.stockdbapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {


    final String[] from = {StockDBAdapter.SYMBOL, StockDBAdapter.NAME, StockDBAdapter.CURPRICE, StockDBAdapter.TOTVOLUME, StockDBAdapter.CHANGE};
    final int[] to = {R.id.txtSymbol, R.id.txtName, R.id.txtPrice, R.id.txtVolume, R.id.txtChange};
    ListView lv;
    EditText myFilter;
    private StockDBAdapter myDbHelper;
    private SimpleCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDbHelper = new StockDBAdapter(this);
        myDbHelper.open();

        //Clean all data
        myDbHelper.deleteAllStocks();
        //Add some data
        myDbHelper.insertSomeStocks();


        displayListView();


    }


    private void displayListView() {

        Cursor cursor = myDbHelper.fetchAllStocks();
        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        myAdapter = new SimpleCursorAdapter(
                this, R.layout.item_row,
                cursor,
                from,
                to,
                0);

        lv = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        lv.setAdapter(myAdapter);


        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {

                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) lv.getItemAtPosition(position);

                // Get the name of the company from this row in the database.
                String companyName =
                        cursor.getString(cursor.getColumnIndexOrThrow(StockDBAdapter.NAME));
                // We know the View is a TextView so we can cast it
                Toast.makeText(getBaseContext(), companyName + " clicked() ", Toast.LENGTH_SHORT).show();

            }
        });


        myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                myAdapter.getFilter().filter(s.toString());
            }
        });


        myAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return myDbHelper.fetchStockLikeSymbol(constraint.toString());
            }
        });

    }


}