package com.darkkeeper.moneycurrency.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.darkkeeper.moneycurrency.MainActivity;
import com.darkkeeper.moneycurrency.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by user on 4/20/15.
 */
public class WidgetProvider extends AppWidgetProvider {
    private static String LOG_TAG = "myLogs";
    static AppWidgetManager appWidgetManager;
    static Context context;
    static int widgetID;
    static PendingIntent configPendingIntent;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
/*        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        Prefs.loadPrefs(context);
        this.context = context;
        this.appWidgetManager = appWidgetManager;

        for (int id : appWidgetIds) {
            widgetID = id;
            updateWidget();
        }*/
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
/*        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
        for (int widgetID : appWidgetIds) {
            Prefs.deleteWidgetFromDb(Prefs.WIDGET_ID+widgetID);
          //  editor.remove(ConfigActivity.WIDGET_TEXT + widgetID);
          //  editor.remove(ConfigActivity.WIDGET_COLOR + widgetID);
        }*/

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    public static void updateWidget() {
/*        Log.d(LOG_TAG, "updateWidget " + widgetID);
        calculate(Prefs.widgetCurrentCurrency, Prefs.widgetNextCurrency);*/
    }

    public static void calculate(String currentCurrency, String nextCurrency){
        if (!currentCurrency.equals("")&&!nextCurrency.equals("")){
            new RetrieveCurrencyTask().execute(currentCurrency, nextCurrency);
        }
    }


    static class RetrieveCurrencyTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(final String... currencies) {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.google.com/finance/converter?a=1&from=" + currencies[0] + "&to=" + currencies[1]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements currentRate = doc.getElementsByClass("bld");
            final String currentRateString = currentRate.html();

            String result = "1 " + currencies[0] + " = " + currentRateString;
            return result;
        }
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute (String string){

            Log.d(LOG_TAG,"onPostExecute string = " + string);

            if (string == null) return;

            AppWidgetManager man = AppWidgetManager.getInstance(context);

            Intent configIntent = new Intent(context, MainActivity.class);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            Bundle bundle = configIntent.getExtras();
            Log.d(LOG_TAG,"" + bundle);
            configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0, bundle);
            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
            widgetView.setTextViewText(R.id.tv, string);
            widgetView.setOnClickPendingIntent(R.id.tv, configPendingIntent);

            appWidgetManager.updateAppWidget(widgetID, widgetView);
        }
    }
}
