package com.darkkeeper.moneycurrency;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Created by user on 4/20/15.
 */
public class ConfigWidgetActivity extends Activity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_CURRENT_CURRENCY = "widget_current_currency";
    public final static String WIDGET_NEXT_CURRENCY= "widget_next_currency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate config");

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.widget_configs);
    }


    public void onClick(View v) {
        int rgCurrent = ((RadioGroup) findViewById(R.id.rgCurrent))
                .getCheckedRadioButtonId();
        String current_currency = "";
        switch (rgCurrent) {
            case R.id.radioUsdCurrent:
                current_currency = "USD";
                break;
            case R.id.radioRubCurrent:
                current_currency = "RUB";
                break;
            case R.id.radioEuroCurrent:
                current_currency = "EUR";
                break;
        }
        int rgNext = ((RadioGroup) findViewById(R.id.rgNext))
                .getCheckedRadioButtonId();
        String next_currency = "";
        switch (rgNext) {
            case R.id.radioUsdNext:
                next_currency = "USD";
                break;
            case R.id.radioRubNext:
                next_currency = "RUB";
                break;
            case R.id.radioEuroNext:
                next_currency = "EUR";
                break;
        }

        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(WIDGET_CURRENT_CURRENCY + widgetID, current_currency);
        editor.putString(WIDGET_NEXT_CURRENCY + widgetID, next_currency);
        editor.commit();

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        Log.d(LOG_TAG, "finish config " + widgetID);
        finish();
    }
}
