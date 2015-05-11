package com.darkkeeper.moneycurrency;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.darkkeeper.moneycurrency.core.Currencies;
import com.darkkeeper.moneycurrency.core.CurrencyAdapter;
import com.darkkeeper.moneycurrency.database.Connection;
import com.darkkeeper.moneycurrency.database.DataProvider;



public class CurrencyListActivity extends ActionBarActivity {
    private ListView listView;
    private Button button;
    private Currencies currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_currency_list);

        listView = (ListView) findViewById(R.id.currencyListView);
        button = (Button) findViewById( R.id.button );

        button.setText("Done");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity( new Intent( CurrencyListActivity.this, FavoritesCurrencyListActivity.class ) );
                finish();
            }
        });


        DataProvider dataProvider = new DataProvider(Connection.getConnection(this));
        currencies = new Currencies();
        currencies.setCurrencies(dataProvider.getAll());
     //   dataProvider.addRow();




        CurrencyAdapter adapter = new CurrencyAdapter( this, currencies.getCurrencies(), true );

        listView.setAdapter(adapter);
    }


/*    public void initListView(CurrencyListView spinner, String currency, Integer id){
        spinner.listView = currency;
        spinner.id = id;
        setNewAdapter(spinner);
        setListener(spinner);
    }*/

/*    public void setNewAdapter(CurrencyListView listView){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        if (!listView.listView.equals("")){
            int currencyListViewPosition = adapter.getPosition(listView.listView);
            listView.setSelection(currencyListViewPosition);
        }
    }*/

/*
    public void setListener(final CurrencyListView listView){
        Log.d(LOGS, "id = " + listView.id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (listView.id) {
                    case 0:
                        Log.d(LOGS, "wcC");
                        Prefs.widgetCurrentCurrency = items[position];
                        break;
                    case 1:
                        Log.d(LOGS, "wnC");
                        Prefs.widgetNextCurrency = items[position];
                        break;
                    case 2:
                        Log.d(LOGS, "cC");
                        Prefs.currentCurrency = items[position];
                        break;
                    case 3:
                        Log.d(LOGS, "nC");
                        Prefs.nextCurrency = items[position];
                        break;
                }
                Prefs.saveChanges();
            }
        });
    }
*/

/*

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("name", etName.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_currency_list, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
/*        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
