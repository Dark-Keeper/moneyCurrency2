package com.darkkeeper.moneycurrency;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.darkkeeper.moneycurrency.preferences.Prefs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by user on 4/23/15.
 */
public class CurrencySpinner extends Spinner {

    private Context context;

    private final String[] items = new String[]{"USD","RUB","EUR","BYR"};
    String currentCurrency = new String();
    String nextCurrency = new String();

    public CurrencySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public void setAdapter(Context context, Integer spinner){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        this.setAdapter(adapter);

        if (!Prefs.getCurrentCurrency().equals("")){
            if (spinner==0){
                int currentCurrencySpinnerPosition = adapter.getPosition(Prefs.getCurrentCurrency());
                this.setSelection(currentCurrencySpinnerPosition);
            } else {
                int nextCurrencySpinnerPosition = adapter.getPosition(Prefs.getNextCurrency());
                this.setSelection(nextCurrencySpinnerPosition);
            }
        }
    }

    public void setListener(final Integer spinner){
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner==0) {
                    Prefs.currentCurrency = items[position];
                }
                else {
                    Prefs.nextCurrency = items[position];
                }
                Prefs.saveChanges();
                calculate(Prefs.getCurrentCurrency(), Prefs.getNextCurrency());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

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
            final Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     TextView textView = (TextView) activity.findViewById(R.id.currentRateTextView);
                     textView.setText("1 " + currencies[0] + " = " + currentRateString);
                }
            });
            return null;
        }
    }
}
