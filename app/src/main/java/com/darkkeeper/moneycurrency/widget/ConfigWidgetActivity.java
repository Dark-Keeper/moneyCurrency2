package com.darkkeeper.moneycurrency.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.darkkeeper.moneycurrency.CurrencySpinner;
import com.darkkeeper.moneycurrency.R;
import com.darkkeeper.moneycurrency.preferences.Prefs;

/**
 * Created by user on 4/20/15.
 */
public class ConfigWidgetActivity extends Activity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";

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

        CurrencySpinner currentCurrencySpinner = (CurrencySpinner)findViewById(R.id.widgetSpinner1);
        CurrencySpinner nextCurrencySpinner= (CurrencySpinner)findViewById(R.id.widgetSpinner2);

        currentCurrencySpinner.setAdapter(this,0);
        nextCurrencySpinner.setAdapter(this,1);

        currentCurrencySpinner.setListener(0);
        nextCurrencySpinner.setListener(1);
    }


    public void onClick(View v) {

        // Записываем значения с экрана в Preferences
        Prefs.saveWidgetChanges();

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        Log.d(LOG_TAG, "finish config " + widgetID);
        finish();
    }
}
