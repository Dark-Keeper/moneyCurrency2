package com.darkkeeper.moneycurrency;

import android.os.AsyncTask;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    String currentCurrency = new String();
    String nextCurrency = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MoneyCurrency");
        Spinner convertFrom = (Spinner)findViewById(R.id.spinner1);
        final String[] items = new String[]{"USD","RUB","EUR","BYR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        convertFrom.setAdapter(adapter);

        Spinner convertTo= (Spinner)findViewById(R.id.spinner2);
        convertTo.setAdapter(adapter);

        convertFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCurrency = items[position];
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        convertTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nextCurrency = items[position];
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // System.out.println(currentCurrencies[0] + " " + currentCurrencies[1]);
        //   convertFrom.setOnItemClickListener(this);
    }

    private void calculate(){
        if (!currentCurrency.equals("")&&!nextCurrency.equals("")){
            new RetrieveFeedTask().execute(currentCurrency,nextCurrency);
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(final String... currencies) {
            Document doc = null;
            System.out.println(currencies);
            try {
                doc = Jsoup.connect("https://www.google.com/finance/converter?a=1&from=" + currencies[0] + "&to=" + currencies[1]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements currentRate = doc.getElementsByClass("bld");
            final String currentRateString = currentRate.html();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) findViewById(R.id.currentRateTextView);
                    textView.setText("1 " + currencies[0] + " = " + currentRateString);
                }
            });
           // System.out.println(currentRateString.substring(0, currentRateString.indexOf(" ")));
            return null;
        }
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
