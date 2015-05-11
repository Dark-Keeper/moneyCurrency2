package com.darkkeeper.moneycurrency;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darkkeeper.moneycurrency.database.Connection;
import com.darkkeeper.moneycurrency.database.Prefs;
import com.darkkeeper.moneycurrency.widget.WidgetProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class MainActivity extends ActionBarActivity{
    private static final int PICK_CURRENT_CURRENCY = 1;
    private static final int PICK_NEXT_CURRENCY = 2;
    private static final String LOG_TAG = "myLogs";

    private Button currencyFromTV;
    private Button currencyToTV;

    private ImageView currencyFromIV;
    private ImageView currencyToIV;


    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    Bundle extras;
    Activity activity;
    Connection connection;

    private int version = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Prefs.loadPrefs(this);
        setContentView(R.layout.activity_main);
        PackageInfo pInfo = null;

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        connection = new Connection(this, version);


        activity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MoneyCurrency");

        currencyFromTV = (Button) findViewById( R.id.currencyFromName );
        currencyToTV = (Button) findViewById( R.id.currencyToName );

        currencyFromTV.setText(Prefs.currentCurrency);
        currencyToTV.setText(Prefs.nextCurrency);

        currencyFromTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,FavoritesCurrencyListActivity.class), PICK_CURRENT_CURRENCY);
            }
        });

        currencyToTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, FavoritesCurrencyListActivity.class), PICK_NEXT_CURRENCY);
            }
        });

        currencyFromIV = (ImageView) findViewById( R.id.currencyFromImage );
        currencyToIV = (ImageView) findViewById( R.id.currencyToImage );





        Intent intent = getIntent();
        extras = intent.getExtras();
       // Log.d(LOGS,"EXTRAS = " + extras);
        if (extras != null) {
            calculate(Prefs.currentCurrency,Prefs.nextCurrency);
            calculate(Prefs.currentCurrency,Prefs.nextCurrency);
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        //    Log.d(LOGS,"widgetId = " + widgetID);
            if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
            resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

            setResult(RESULT_CANCELED, resultValue);
        } else {
           // initListView(currentCurrencyListView, Prefs.currentCurrency, 2);
          //  initListView(nextCurrencyListView, Prefs.nextCurrency, 3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CURRENT_CURRENCY) {
            if (resultCode == RESULT_OK) {
                Prefs.currentCurrency = data.getStringExtra("currency");
                Prefs.saveChanges();
                currencyFromTV.setText(data.getStringExtra("currency"));
                calculate(Prefs.currentCurrency,Prefs.nextCurrency);

            }
        }
        else if (requestCode == PICK_NEXT_CURRENCY){
            if (resultCode == RESULT_OK) {
                Prefs.nextCurrency = data.getStringExtra("currency");
                Prefs.saveChanges();
                currencyToTV.setText(data.getStringExtra("currency"));
                calculate(Prefs.currentCurrency,Prefs.nextCurrency);
            }
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
                    TextView textView = (TextView) findViewById(R.id.currencyResult);
                    textView.setText("1 " + currencies[0] + " = " + currentRateString);
                }
            });
            return null;
        }
    }

    public void onClick(View v){
        Log.d(LOG_TAG,"onClick EXTRAS = " + extras);
        if (extras != null) {
            Log.d(LOG_TAG, "WIDGETS = " + Prefs.widgetCurrentCurrency + Prefs.widgetNextCurrency);
            //calculate(Prefs.widgetCurrentCurrency, Prefs.widgetNextCurrency);

           // Prefs.saveWidgetToDb(widgetID,);
            updateMyWidgets(this);
            setResult(RESULT_OK, resultValue);

            activity.finish();
        } else {
            Log.d(LOG_TAG, "ACTIVITY = " + Prefs.currentCurrency + Prefs.nextCurrency);
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
