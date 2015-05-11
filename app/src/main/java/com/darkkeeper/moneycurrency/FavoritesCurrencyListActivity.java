package com.darkkeeper.moneycurrency;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

/**
 * Created by andreipiatosin on 5/8/15.
 */
public class FavoritesCurrencyListActivity extends ActionBarActivity {
    private ListView listView;
    private Button button;
    private Currencies currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_currency_list);

        listView = (ListView) findViewById(R.id.currencyListView);
        button = (Button) findViewById( R.id.button );

        button.setText("Add More");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult( new Intent( FavoritesCurrencyListActivity.this, CurrencyListActivity.class ),1 );
            }
        });

        initialize();
    }


    public void initialize (){
        DataProvider dataProvider = new DataProvider(Connection.getConnection(this));
        currencies = new Currencies();
        currencies.setCurrencies(dataProvider.getFavorites());


        CurrencyAdapter adapter = new CurrencyAdapter(this, currencies.getCurrencies(), false);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("currency", currencies.getCurrency(position).getCode());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initialize();
    }

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
