package com.darkkeeper.moneycurrency;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.darkkeeper.moneycurrency.database.DBHelper;
import com.darkkeeper.moneycurrency.preferences.Prefs;
import com.darkkeeper.moneycurrency.widget.WidgetProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class MainActivity extends ActionBarActivity{
    private static final String LOGS = "myLogs";
    ;

    private final String[] items = new String[]{"USD","RUB","EUR","BYR"};

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    Bundle extras;
    Activity activity;
    DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Prefs.loadPrefs(this);

        activity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MoneyCurrency");

        CurrencyListView currentCurrencyListView = (CurrencyListView)findViewById(R.id.listView1);
        CurrencyListView nextCurrencyListView = (CurrencyListView)findViewById(R.id.listView2);

        Intent intent = getIntent();
        extras = intent.getExtras();
        Log.d(LOGS,"EXTRAS = " + extras);
        if (extras != null) {
            initListView(currentCurrencyListView, Prefs.widgetCurrentCurrency, 0);
            initListView(nextCurrencyListView, Prefs.widgetNextCurrency, 1);
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d(LOGS,"widgetId = " + widgetID);
            if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
            resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

            setResult(RESULT_CANCELED, resultValue);
        } else {
            initListView(currentCurrencyListView, Prefs.currentCurrency, 2);
            initListView(nextCurrencyListView, Prefs.nextCurrency, 3);
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

    public void initListView(CurrencyListView spinner, String currency, Integer id){
        spinner.listView = currency;
        spinner.id = id;
        setNewAdapter(spinner);
        setListener(spinner);
    }

    public void setNewAdapter(CurrencyListView listView){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        if (!listView.listView.equals("")){
            int currencyListViewPosition = adapter.getPosition(listView.listView);
            listView.setSelection(currencyListViewPosition);
        }
    }

    public void setListener(final CurrencyListView listView){
        Log.d(LOGS,"id = " + listView.id);
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

    public void calculate(String currentCurrency, String nextCurrency){
        if (!currentCurrency.equals("")&&!nextCurrency.equals("")){
            new RetrieveCurrencyTask().execute(currentCurrency, nextCurrency);
        }
    }


    class RetrieveCurrencyTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(final String... currencies) {
            Document doc = null;
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
            return null;
        }
    }

    public void onClick(View v){
        Log.d(LOGS,"onClick EXTRAS = " + extras);
        if (extras != null) {
            Log.d(LOGS, "WIDGETS = " + Prefs.widgetCurrentCurrency + Prefs.widgetNextCurrency);
            //calculate(Prefs.widgetCurrentCurrency, Prefs.widgetNextCurrency);

           // Prefs.saveWidgetToDb(widgetID,);
            updateMyWidgets(this);
            setResult(RESULT_OK, resultValue);

            activity.finish();
        } else {
            Log.d(LOGS, "ACTIVITY = " + Prefs.currentCurrency + Prefs.nextCurrency);
            calculate(Prefs.currentCurrency, Prefs.nextCurrency);
        }
    }

    public void updateMyWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        WidgetProvider.updateWidget();
    }
/*        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(updateIntent);
    }*/

}
