package com.darkkeeper.moneycurrency;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.darkkeeper.moneycurrency.preferences.Prefs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class MainActivity extends ActionBarActivity{;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Prefs.loadPrefs(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MoneyCurrency");

        CurrencySpinner currentCurrencySpinner = (CurrencySpinner)findViewById(R.id.spinner1);
        CurrencySpinner nextCurrencySpinner= (CurrencySpinner)findViewById(R.id.spinner2);

        currentCurrencySpinner.setAdapter(this,0);
        nextCurrencySpinner.setAdapter(this,1);

        currentCurrencySpinner.setListener(0);
        nextCurrencySpinner.setListener(1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
